package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        // setupLogging();
        try {
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
            gamePanel.config.titleLoader();

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
        } catch (Throwable t) {
            
            System.err.println("Fatal error during game startup:");
            t.printStackTrace();
        }
    }

    private static void setupLogging() {
        try {
            File logFile = new File(Constants.LOG_FILE);
            if (!logFile.exists()) logFile.createNewFile();

            PrintStream logStream = new PrintStream(new FileOutputStream(logFile, true), true);
            
            System.setOut(logStream);
            System.setErr(logStream);

            System.out.println("=== Wookster Log Started ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
