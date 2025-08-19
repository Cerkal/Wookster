package tile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main.Constants;
import main.Utils;

public class Tile {

    public String tileType = "Blank";
    public int x;
    public int y;
    public BufferedImage image;
    public BufferedImage imageAlt;
    public List<BufferedImage> imageSequence = new ArrayList<BufferedImage>();
    public int imageIndex = 0;
    public boolean collision = false;
    public boolean projectileCollision = false;
    public int randomFrequency = 0;
    public int indexNumber;
    public boolean tileHighlight = false;
    public boolean discovered = false;

    public Tile(){}

    public Tile(
        String tileType,
        String image
    ){
        this.tileType = tileType;
        this.image = getImage(image);
        setCollision();
    }

    public Tile(
        String tileType,
        String image,
        boolean collision,
        boolean projectileCollision
    ){
        this(tileType, image);
        this.collision = collision;
        this.projectileCollision = projectileCollision;
    }

    public Tile(
        String tileType,
        String image,
        List<String> imageSequence
    ){
        this(tileType, image);
        this.imageSequence = getImageSequence(imageSequence);
    }

    public Tile(
        String tileType,
        String image,
        List<String> imageSequence,
        int randomFrequency
    ){
        this(tileType, image, imageSequence);
        this.randomFrequency = randomFrequency;
    }

     public Tile(Tile other) {
        this.tileType = other.tileType;
        this.image = other.image;
        this.imageAlt = other.imageAlt;
        this.imageSequence = other.imageSequence;
        this.randomFrequency = other.randomFrequency;
        this.imageIndex = other.imageIndex;
        this.collision = false;
        this.collision = other.collision;
        this.projectileCollision = false;
        this.indexNumber = other.indexNumber;
    }

    public BufferedImage getCurrentImage(long gameTime) {
        if (this.randomFrequency > 0) { return this.image; }
        if (this.imageSequence != null && this.imageSequence.size() > 0) {
            long halfSecond = (Constants.NANO_SECOND / 2);
            int imageIndex = (int) (gameTime / halfSecond) % this.imageSequence.size();
            return this.imageSequence.get(imageIndex);
        }
        return this.image;
    }

    public void randomize() {
        if (randomFrequency == 0) { return; }
        int random = Utils.generateRandomInt(0, randomFrequency);
        if (random == 0) {
            int randomImageIndex = Utils.generateRandomInt(0, imageSequence.size() - 1);
            this.image = imageSequence.get(randomImageIndex);
        }
    }

    private BufferedImage getImage(String path) {
        if (path == null) { return null; }
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private List<BufferedImage> getImageSequence(List<String> paths) {
        List<BufferedImage> imageSequence = new ArrayList<BufferedImage>();
        if (paths == null) { return imageSequence; }
        for (String path : paths) {
            BufferedImage image = getImage(path);
            imageSequence.add(image);
        }
        return imageSequence;
    }

    private void setCollision() {
        switch (this.tileType) {
            case "Water":
                this.collision = true;
                this.projectileCollision = false;
                break;
            case "Tree":
                this.collision = true;
                this.projectileCollision = true;
                break;
            case "Wall":
                this.collision = true;
                this.projectileCollision = true;
                break;
            case "Clear":
                this.collision = true;
                this.projectileCollision = false;
                break;
            case "Large Tree":
                this.collision = true;
                this.projectileCollision = true;
                break;
            case "Leaves":
                this.collision = true;
                this.projectileCollision = true;
                break;
            default:
                break;
        }
    }

    public static BufferedImage rotateSquareImage(BufferedImage originalImage, double angleDegrees) {
        int size = originalImage.getWidth();
        BufferedImage rotatedImage = new BufferedImage(size, size, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angleDegrees), size / 2.0, size / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(originalImage, 0, 0, null);
        return rotatedImage;
    }
}
