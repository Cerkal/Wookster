package tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Constants;

public class Tile {

    public BufferedImage image;
    public List<BufferedImage> imageSequence = new ArrayList<BufferedImage>();
    public int imageIndex = 0;
    public boolean collision = false;

    public BufferedImage getCurrentImage(long gameTime) {
        if (imageSequence != null && imageSequence.size() > 0) {
            long halfSecond = Constants.NANO_SECOND / 2;
            int imageIndex = (int) (gameTime / halfSecond) % imageSequence.size();
            return imageSequence.get(imageIndex);
        }
        return image;
    }
}
