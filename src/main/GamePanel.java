package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import entity.Entity;
import entity.Player;
import objects.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    public enum GameState {
        TITLE,
        PLAY,
        PAUSE,
        DIALOGUE
    }

    boolean fpsDebug = false;
    Thread gameThread;

    public TileManager tileManager = new TileManager(this);
    public GameState gameState = GameState.TITLE;
    public long gameTime = 0;
    public KeyHandler keyHandler = new KeyHandler(this);
    public Player player = new Player(this, keyHandler);
    public Collision collision = new Collision(this);
    public List<SuperObject> objects = new ArrayList<>();
    public List<Entity> npcs = new ArrayList<>();
    public AssetSetter assetSetter = new AssetSetter(this);
    public Sound sound = new Sound();
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    public GamePanel() {
        this.setPreferredSize(new DimensionUIResource(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        this.assetSetter.setObject();
        this.assetSetter.setNPCs();
        playMusic(Constants.SOUND_TITLE_SCREEN);
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
                if (fpsDebug) {
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
                runGame(graphics2D);
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

    private void runGame(Graphics2D graphics2D) {
        this.tileManager.draw(graphics2D);

        for (SuperObject object : objects) {
            object.draw(graphics2D);
        }

        for (Entity npc : npcs) {
            npc.draw(graphics2D);
        }

        this.player.draw(graphics2D);

        this.ui.draw(graphics2D);
        graphics2D.dispose();
    }
}

