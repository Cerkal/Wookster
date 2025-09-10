package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
    
    public enum GameState {
        TITLE,
        PLAY,
        PAUSE,
        DIALOGUE,
        INVENTORY,
        DEATH
    }

    public boolean debugAll = false;
    public boolean debugMap = false;
    public boolean debugMapBuilder = false;
    public boolean debugRenderTime = false;
    public boolean debugUpdateTime = false;
    public boolean debugCollision = false;

    public GameState gameState = GameState.TITLE;
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public UI ui = new UI(this);
    public Player player = new Player(this, keyHandler);
    public Collision collision = new Collision(this);
    public Sound sound = new Sound();
    public Config config = new Config(this);
    public LevelManager levelManager = new LevelManager(this);
    public EventHandler eventHandler = new EventHandler(this);
    public HashMap<String, Quest> quests = new HashMap<>();
    public QuestManager questManager = new QuestManager(this);
    public long gameTime = 0;
    public int fps;
    public BufferedImage background;

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

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setIgnoreRepaint(true);
        setFocusable(true);
        addKeyListener(this.player.keyHandler);
        loadLevels();
    }

    public void startGame() {
        if (this.config.dataWrapper.player == null) {
            newGame();
        } else {
            loadGame();
        }
    }

    public void loadGame() {
        this.config.loadConfig();
        this.restartLevel();
        this.levelManager.loadLevel(this.config.dataWrapper.currentLevelIndex);
        this.gameState = GameState.PLAY;
        this.stopMusic();
        this.playMusic(Constants.SOUND_BG_01);
        System.out.println("Loaded game.");
    }

    public void newGame() {
        this.gameState = GameState.PLAY;
        this.config.dataWrapper = new DataWrapper();
        this.restartLevel();
        this.levelManager.loadLevel(1);
        this.stopMusic();
        this.playMusic(Constants.SOUND_BG_01);
        System.out.println("New game.");
    }

    public void start() {
        if (this.running) return;
        this.running = true;
        this.sound.mute = false;
        this.gameThread = new Thread(this, "GameThread");
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

        // Create BufferStrategy safely
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
            this.gameTime += (long) elapsed;

            long updateStart, updateTime, renderStart, renderTime;

            // Update game logic at fixed timestep
            while (delta >= 1) {
                updateStart = System.nanoTime();
                update();
                updateTime = System.nanoTime() - updateStart;
                if (this.debugUpdateTime) {
                    System.out.println("Update: " + (updateTime / 1_000_000.0) + " ms");
                }
                delta--;
            }

            // Render as often as possible
            renderStart = System.nanoTime();
            render();
            renderTime = System.nanoTime() - renderStart;
            if (this.debugRenderTime) {
                System.out.println("Render: " + (renderTime / 1_000_000.0) + " ms");
            }

            frames++;

            // Maintain FPS by sleeping the thread
            long frameTime = System.nanoTime() - now;
            long sleepTime = (long) NS_PER_UPDATE - frameTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException ignored) {}
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                this.fps = frames;
                frames = 0;
                timer += 1000;
            }
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

    private void drawGame(Graphics2D graphics2D) {
        try {
            this.tileManager.draw(graphics2D);
            for (Effect effect : effects) effect.draw(graphics2D);
            for (SuperObject object : objects) object.draw(graphics2D);

            this.entityList.add(this.player);
            this.entityList.addAll(this.npcs);
            Collections.sort(this.entityList, Comparator.comparingInt(entity -> entity.worldY));
            for (Entity entity : this.entityList) entity.draw(graphics2D);
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

    private void loadLevels() {
        levelManager.addLevel(new Level00(this));
        levelManager.addLevel(new Level01(this));
        levelManager.addLevel(new Level02(this));
        levelManager.addLevel(new Level03(this));
    }
}
