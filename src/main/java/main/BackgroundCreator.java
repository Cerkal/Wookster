package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BackgroundCreator {
    
    public void createBackgroundImage() {
        // Create background for parralex
        BufferedImage bufferedImage = new BufferedImage(Constants.TILE_SIZE * Constants.MAX_WORLD_COL, Constants.TILE_SIZE * Constants.MAX_WORLD_COL, BufferedImage.TYPE_INT_RGB);
        try {
            BufferedImage grass00 = ImageIO.read(getClass().getResourceAsStream(Constants.TILE_GRASS_00));
            BufferedImage grass01 = ImageIO.read(getClass().getResourceAsStream(Constants.TILE_GRASS_01));
            Graphics2D graphics2D = bufferedImage.createGraphics();
            for (int x = 0; x < Constants.TILE_SIZE * Constants.MAX_WORLD_COL; x++) {
                for (int y = 0; y < Constants.TILE_SIZE * Constants.MAX_WORLD_COL; y++) {
                    int random = Utils.generateRandomInt(0, 4);
                    if (random > 2) {
                        graphics2D.drawImage(grass01, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
                    } else {
                        graphics2D.drawImage(grass00, x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
                    }
                    y += Constants.TILE_SIZE - 2;
                }
                x += Constants.TILE_SIZE - 2;
            }   
            graphics2D.dispose();
            File outputFile = new File("src/main/resources/backgrounds/output_image.png");
            String formatName = "png";
            try {
                boolean success = ImageIO.write(bufferedImage, formatName, outputFile);
                if (success) {
                    System.out.println("Image successfully written to: " + outputFile.getAbsolutePath());
                } else {
                    System.out.println("Failed to write image.");
                }
            } catch (IOException e) {
                System.err.println("Error writing image: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}