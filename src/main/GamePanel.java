package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import objects.SuperObject;
import objects.projectiles.Projectile;
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
    public boolean debugFPS = false;
    public boolean debugCollision = false;

    public TileManager tileManager = new TileManager(this);
    public GameState gameState = GameState.TITLE;
    public long gameTime = 0;
    public KeyHandler keyHandler = new KeyHandler(this);
    public UI ui = new UI(this);
    public Player player = new Player(this, keyHandler);
    public Collision collision = new Collision(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public Sound sound = new Sound();
    public EventHandler eventHandler = new EventHandler(this);

    public List<SuperObject> objects = new ArrayList<>();
    public List<Entity> npcs = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();
    public List<Effect> effects = new ArrayList<>();

    BufferedImage deathScreen;

    public GamePanel() {
        this.setPreferredSize(new DimensionUIResource(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        this.assetSetter.setLevel();

        // MUTE IT!
        this.sound.mute = false;
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
                repaint();
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
        }
    }

    public void paintComponent(Graphics graphics) {
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
        this.tileManager.draw(graphics2D);

        for (Effect effect : effects) {
            effect.draw(graphics2D);
        }

        for (SuperObject object : this.objects) {
            object.draw(graphics2D);
        }

        // Compare Y Values for drawing
        entityList.add(this.player);
        entityList.addAll(this.npcs);
        
        Collections.sort(entityList, new Comparator<Entity>() {
            @Override
            public int compare(Entity entity1, Entity entity2) {
                int result = Integer.compare(entity1.worldY, entity2.worldY);
                return result;
            }
        });

        for (Entity entity : this.entityList) {
            entity.draw(graphics2D);
        }

        try {
            for (Projectile projectile : this.projectiles) {
                projectile.draw(graphics2D);
            }    
        } catch (Exception e) { }

        entityList.clear();

        this.ui.draw(graphics2D);
        graphics2D.dispose();
    }

    public void getGraphicsSnapshot() {
        this.deathScreen = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = this.deathScreen.createGraphics();
        this.paint(g2d);
        g2d.dispose();
    }
    
    public void restartLevel() {
        this.objects.clear();
        this.npcs.clear();
        this.entityList.clear();
        this.projectiles.clear();
        this.effects.clear();
        this.player.setDefaultValues();
        this.assetSetter.setLevel();
        this.gameState = GameState.PLAY;
    }
}

