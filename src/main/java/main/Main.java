package main;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setTitle(Constants.GAME_TITLE);

        GamePanel gamePanel = new GamePanel(
            Constants.SCREEN_WIDTH,
            Constants.SCREEN_HEIGHT
        );

        gamePanel.playMusic(Constants.SOUND_TITLE_SCREEN);
        gamePanel.config.loadConfig();

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.requestFocus();
        gamePanel.start();

        // Game loop to be added to EDT
        javax.swing.SwingUtilities.invokeLater(gamePanel::start);

        // Debug visual pipelines bro
        System.out.println("Java2D pipeline: " + System.getProperty("sun.java2d.opengl"));
        System.out.println("Java2D D3D: " + System.getProperty("sun.java2d.d3d"));
    }
}
