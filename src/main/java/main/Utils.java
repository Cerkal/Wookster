package main;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Utils {

    public static int generateRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than Max.");
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String capitalizeString(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static BufferedImage scaleImage(BufferedImage original) {
        return scaleImage(original, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(original, 0, 0, width, height, null);
        graphics2D.dispose();

        // Ash cache em'
        return toCompatibleImage(scaledImage);
    }

    private static BufferedImage toCompatibleImage(BufferedImage image) {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        if (image.getColorModel().equals(gfxConfig.getColorModel())) {
            return image;
        }

        BufferedImage newImage = gfxConfig.createCompatibleImage(
            image.getWidth(), image.getHeight(),
            image.getTransparency());
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return newImage;
    }
}
