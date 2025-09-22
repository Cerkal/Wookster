package main;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import effects.Effect;
import entity.Entity;
import entity.Player;
import levels.Level01;
import levels.Level02;
import levels.Level03;
import levels.Level00;
import objects.SuperObject;
import objects.projectiles.ProjectileManager;
import tile.TileManager;

public class GamePanel extends Canvas implements Runnable {

    private Thread gameThread;
    private boolean running = false;
    private BufferStrategy bufferStrategy;
    
    private static final String GAME_THREAD = "GameThread";
    
    public enum GameState {
        LOADING,
        SAVING,
        TITLE,
        PLAY,
        PAUSE,
        DIALOGUE,
        INVENTORY,
        VENDOR,
        DEATH
    }

    public boolean debugAll = false;
    public boolean debugMap = false;
    public boolean debugMapBuilder = false;
    public boolean debugRenderTime = false;
    public boolean debugUpdateTime = false;
    public boolean debugCollision = false;
    public boolean debugAllWeapons = false;
    
    // Mouse Aim
    public boolean mouseAim = true;
    public Cursor targetCursor;
    public Cursor targetCursorWide;
    public Cursor defaultCursor;
    private boolean cursorHidden = false;

    public GameState gameState = GameState.TITLE;
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public MouseHandler mouseHandler = new MouseHandler(this);
    public MouseMoveHandler mouseMoveHandler = new MouseMoveHandler(this);
    public QuestManager questManager = new QuestManager(this);
    public Sound sound = new Sound();
    public Config config = new Config(this);
    public UI ui = new UI(this);
    public Player player = new Player(this, keyHandler);
    public Collision collision = new Collision(this);
    public LevelManager levelManager = new LevelManager(this);
    public EventHandler eventHandler = new EventHandler(this);
    public HashMap<String, Quest> quests = new HashMap<>();
    public long gameTime = 0;
    public int fps;
    public BufferedImage background;
    public boolean loaded = false;

    public List<SuperObject> objects = new ArrayList<>();
    public List<Entity> npcs = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ProjectileManager projectileManager = new ProjectileManager();
    public List<Effect> effects = new ArrayList<>();

    Graphics2D graphics;
    BufferedImage fullScreen;
    boolean isFullScreen = false;
    int fullScreenWidth = Constants.FULL_SCREEN_WIDTH;
    int fullScreenHeight = Constants.FULL_SCREEN_HEIGHT;

    static final int NEW_GAME_LEVEL_INDEX = 0;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setIgnoreRepaint(true);
        setFocusable(true);
        addKeyListener(this.player.keyHandler);
        addMouseListener(this.mouseHandler);
        addMouseMotionListener(this.mouseMoveHandler);
        setDefaultCursor();
        loadLevels();
    }

    public void startGame() {
        if (this.config.dataWrapper.player == null) {
            newGame();
        } else {
            loadGame();
        }
        this.loaded = true;
    }

    public void loadGame(int levelIndex) {
        this.config.loadConfig();
        this.restartLevel();
        this.levelManager.loadLevel(
            levelIndex,
            true
        );
        this.stopMusic();
        this.playMusic(Constants.SOUND_BG_01);
        System.out.println("Loaded game.");
    }

    public void loadGame() {
        loadGame(this.config.dataWrapper.currentLevelIndex);
    }

    public void newGame() {
        this.gameState = GameState.PLAY;
        this.config.dataWrapper = new DataWrapper();
        this.restartLevel();
        this.levelManager.loadLevel(NEW_GAME_LEVEL_INDEX, false);
        this.stopMusic();
        this.playMusic(Constants.SOUND_BG_01);
        System.out.println("New game.");
    }

    public void start() {
        if (this.running) return;
        this.running = true;
        this.sound.muteEffects = false;
        this.gameThread = new Thread(this, GAME_THREAD);
        this.gameThread.start();
    }

    public void stop() {
        this.running = false;
        try {
            if (this.gameThread != null) {
                this.gameThread.join();
            }
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        // Wait until the Canvas is displayable
        while (!isDisplayable()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }

        createBufferStrategy(3);
        this.bufferStrategy = getBufferStrategy();

        final int TARGET_FPS = 60;
        final double NS_PER_UPDATE = 1_000_000_000.0 / TARGET_FPS;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            double elapsed = now - lastTime; // elapsed nanoseconds since last loop
            delta += elapsed / NS_PER_UPDATE;
            lastTime = now;
            if (this.gameState == GameState.PLAY) {
                this.gameTime += (long) elapsed;
            }

            long updateStart, updateTime, renderStart, renderTime;

            // Update
            while (delta >= 1) {
                updateStart = System.nanoTime();
                update();
                updateTime = System.nanoTime() - updateStart;
                if (this.debugUpdateTime) {
                    System.out.println("Update: " + (updateTime / 1_000_000.0) + " ms");
                }
                delta--;
            }

            // Render
            renderStart = System.nanoTime();
            render();
            renderTime = System.nanoTime() - renderStart;
            if (this.debugRenderTime) {
                System.out.println("Render: " + (renderTime / 1_000_000.0) + " ms");
            }

            frames++;

            if (System.currentTimeMillis() - timer >= 1000) {
                this.fps = frames;
                frames = 0;
                timer += 1000;
            }

            // Maintain FPS by sleeping the thread
            // long frameTime = System.nanoTime() - now;
            // long sleepTime = (long) NS_PER_UPDATE - frameTime;
            // if (sleepTime > 0) {
            //     try {
            //         Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
            //     } catch (InterruptedException ignored) {}
            // }
        }
    }

    private void update() {
        if (this.gameState == GameState.PLAY) {
            this.player.update();
            for (Entity npc : new ArrayList<>(this.npcs)) {
                npc.update();
            }
            this.levelManager.update();
        }
    }

    private void render() {
        if (this.bufferStrategy == null) return;
        do {
            do {
                Graphics2D graphics2D = null;
                try {
                    graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
                    graphics2D.clearRect(0, 0, getWidth(), getHeight());
                    switch (this.gameState) {
                        case TITLE -> drawTitleScreen(graphics2D);
                        case LOADING -> drawLoadingScreen(graphics2D);
                        case SAVING -> drawSavingScreen(graphics2D);
                        default -> drawGame(graphics2D);
                    }

                } finally {
                    if (graphics2D != null) graphics2D.dispose();
                }
            } while (this.bufferStrategy.contentsRestored());

            this.bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync(); // IMPORTANT: reduces tearing on Windows
        } while (this.bufferStrategy.contentsLost());
    }

    private void drawTitleScreen(Graphics2D graphics2D) {
        this.ui.titleScreen(graphics2D);
    }

    private void drawLoadingScreen(Graphics2D graphics2D) {
        this.ui.drawLoadingScreen(graphics2D);
    }

    private void drawSavingScreen(Graphics2D graphics2D) {
        this.ui.drawSavingScreen(graphics2D);
    }

    public void death() {
        long loadingStartTime = System.currentTimeMillis();
        this.gameState = GameState.DEATH;
        new Thread(() -> {
            long elapsed = System.currentTimeMillis() - loadingStartTime;
            if (elapsed < Constants.DEAD_LOADING) {
                try {
                    Thread.sleep(Constants.DEAD_LOADING - elapsed);
                } catch (InterruptedException ignored) {}
            }
            this.loadGame();
        }).start();
    }

    private void drawGame(Graphics2D graphics2D) {
        try {
            this.tileManager.draw(graphics2D);
            for (Effect effect : new ArrayList<>(effects)) effect.draw(graphics2D);
            for (SuperObject object : new ArrayList<>(objects)) object.draw(graphics2D);

            this.entityList.add(this.player);
            this.entityList.addAll(this.npcs);
            Collections.sort(this.entityList, Comparator.comparingInt(entity -> entity.worldY));
            for (Entity entity : new ArrayList<>(this.entityList)) entity.draw(graphics2D);
            this.entityList.clear();

            this.projectileManager.draw(graphics2D);
            this.levelManager.draw(graphics2D);
            this.ui.draw(graphics2D);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playMusic(String soundFile) {
        this.sound.playMusic(soundFile);
    }

    public void stopMusic() {
        this.sound.stopMusic();
    }

    public void playSoundEffect(String soundFile) {
        this.sound.playSoundEffect(soundFile);
    }

    public void restartLevel() {
        this.objects.clear();
        this.npcs.clear();
        this.entityList.clear();
        this.projectileManager.clear();
        this.effects.clear();
        this.player.setDefaultValues();
    }

    public void quit() {
        running = false;
        if (SwingUtilities.getWindowAncestor(this) != null) {
            SwingUtilities.getWindowAncestor(this).dispose();
        }
        System.exit(0);
    }

    private void loadLevels() {
        levelManager.addLevel(new Level00(this));
        levelManager.addLevel(new Level01(this));
        levelManager.addLevel(new Level02(this));
        levelManager.addLevel(new Level03(this));
    }

    private void setDefaultCursor() {
        try {
            defaultCursor = Cursor.getDefaultCursor();
            BufferedImage cursorImg = ImageIO.read(getClass().getResourceAsStream(Constants.TARGET_CURSOR));
            targetCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(8, 8), Constants.TARGET_CURSOR);
            
            BufferedImage cursorImgWide = ImageIO.read(getClass().getResourceAsStream(Constants.TARGET_SMALL_CURSOR));
            targetCursorWide = Toolkit.getDefaultToolkit().createCustomCursor(cursorImgWide, new Point(8, 8), Constants.TARGET_SMALL_CURSOR);
        } catch (Exception e) {}
    }

    public void targetMouse() {
        if (!mouseAim) return;
        setCursor(this.mouseMoveHandler.currentCursor);
        cursorHidden = true;
    }

    public void showMouse() {
        if (!cursorHidden) return;
        setCursor(defaultCursor);
        cursorHidden = false;
    }

    public void toggleMouse() {
        if (cursorHidden) {
            showMouse();
        } else {
            targetMouse();
        }
    }
}
