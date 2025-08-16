package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import effects.Effect;
import entity.Entity;
import entity.Player;
import levels.Level00;
import levels.Level01;
import levels.LevelBuilder;
import objects.SuperObject;
import objects.projectiles.Projectile;
import objects.projectiles.ProjectileManager;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    public enum GameState {
        TITLE,
        PLAY,
        PAUSE,
        DIALOGUE,
        INVENTORY,
        DEATH
    }

    Thread gameThread;

    // Debug
    public boolean debugAll = false;
    public boolean debugMap = false;
    public boolean debugMapBuilder = false;
    public boolean debugFPS = false;
    public boolean debugCollision = false;

    public TileManager tileManager = new TileManager(this);
    public GameState gameState = GameState.TITLE;
    public long gameTime = 0;
    public KeyHandler keyHandler = new KeyHandler(this);
    public UI ui = new UI(this);
    public Player player = new Player(this, keyHandler);
    public Collision collision = new Collision(this);
    public Sound sound = new Sound();
    public Config config = new Config(this);
    public LevelManager levelManager = new LevelManager(this);
    public EventHandler eventHandler;
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

    public GamePanel() {
        this.setPreferredSize(new DimensionUIResource(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        restartLevel();
        loadLevels();
        this.sound.mute = false;
        if (this.isFullScreen) {
            this.fullScreen = new BufferedImage(this.fullScreenWidth, this.fullScreenHeight, BufferedImage.TYPE_INT_ARGB);
            this.graphics = (Graphics2D) this.fullScreen.getGraphics();
            setFullScreen();
        }
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = Constants.NANO_SECOND / Constants.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long startTime = lastTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            this.gameTime = currentTime - startTime;

            if (delta >= 1) {
                update();
                if (this.isFullScreen) {
                    drawToGraphics();
                    drawToScreen();
                } else {
                    repaint();
                }
                delta--;
                drawCount++;
            }

            if (timer >= Constants.NANO_SECOND) {
                if (debugFPS) {
                    System.out.print("FPS: " + drawCount + " ");
                }
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (this.gameState == GameState.PLAY) {
            this.player.update();
            for (Entity npc : this.npcs) {
                npc.update();
            }
            levelManager.update();
        }
    }

    public void drawToGraphics() {
        switch (this.gameState) {
            case GameState.TITLE:
                titleScreen(this.graphics);
                break;
            default:
                drawGame(this.graphics);
                break;
        }
    }

    public void drawToScreen() {
        Graphics graphicsBasic = getGraphics();
        graphicsBasic.drawImage(this.fullScreen, 0, 0, this.fullScreenWidth, this.fullScreenHeight, null);
        graphicsBasic.dispose();
    }

    public void setFullScreen() {
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnv.getDefaultScreenDevice();
        graphicsDevice.setFullScreenWindow(Main.window);
        this.fullScreenWidth = Main.window.getWidth();
        this.fullScreenHeight = Main.window.getHeight();
    }

    public void paintComponent(Graphics graphics) {
        if (this.isFullScreen) {
            return;
        }
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        switch (this.gameState) {
            case GameState.TITLE:
                titleScreen(graphics2D);
                break;
            default:
                drawGame(graphics2D);
                break;
        }
        graphics2D.dispose();
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

    private void titleScreen(Graphics2D graphics2D) {
        this.ui.titleScreen(graphics2D);
    }

    private void drawGame(Graphics2D graphics2D) {
        try {
            this.tileManager.draw(graphics2D);
            for (Effect effect : effects) {
                effect.draw(graphics2D);
            }
            for (SuperObject object : this.objects) {
                object.draw(graphics2D);
            }

            entityList.add(this.player);
            entityList.addAll(this.npcs);
            Collections.sort(entityList, Comparator.comparingInt(e -> e.worldY));
            for (Entity entity : this.entityList) {
                entity.draw(graphics2D);
            }
            entityList.clear();

            for (Projectile projectile : this.projectileManager.projectiles) {
                projectile.draw(graphics2D);
            }
            this.projectileManager.removeProjectiles();

            levelManager.draw(graphics2D);

            this.ui.draw(graphics2D);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void restartLevel() {
        this.objects.clear();
        this.npcs.clear();
        this.entityList.clear();
        this.projectileManager.projectiles.clear();
        this.effects.clear();
        this.player.setDefaultValues();
        this.gameState = GameState.PLAY;
    }

    private void loadLevels() {
        // Map debugger
        if (debugMapBuilder) {
            levelManager.addLevel(new LevelBuilder(this));
            levelManager.loadLevel(0);
            return;
        }
        
        // Levels
        levelManager.addLevel(new Level00(this));
        levelManager.addLevel(new Level01(this));
        levelManager.loadLevel(0);
    }
}

