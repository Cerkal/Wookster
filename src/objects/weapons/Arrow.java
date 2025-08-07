package objects.weapons;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entity.Entity.Direction;
import main.Constants;
import main.GamePanel;

public class Arrow {

    GamePanel gamePanel;
    BufferedImage image;
    BufferedImage originalImage;
    int worldX;
    int worldY;
    Direction direction;
    
    static final int ARROW_SPEED = 14;

    public Arrow(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.direction = gamePanel.player.direction;
        try {
            this.originalImage = ImageIO.read(getClass().getResourceAsStream(Constants.WEAPON_PROJECTILE_ARROW));
            this.image = this.originalImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.worldX = this.gamePanel.player.worldX;
        this.worldY = this.gamePanel.player.worldY;
    }

    public void draw(Graphics2D graphics2D) {
        getDirection();
        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (
            this.worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
            this.worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
            this.worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
            this.worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.worldY)
        ){
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        } else {
            this.gamePanel.arrows.remove(0);
        }
    }

    private void getDirection() {
        switch (this.direction) {
            case Direction.UP:
                this.worldY -= ARROW_SPEED;
                break;
            case Direction.DOWN:
                this.worldY += ARROW_SPEED;
                this.image = rotateSquareImage(this.originalImage, 180);
                break;
            case Direction.LEFT:
                this.worldX -= ARROW_SPEED;
                this.image = rotateSquareImage(this.originalImage, 270);
                break;
            case Direction.RIGHT:
                this.worldX += ARROW_SPEED;
                this.image = rotateSquareImage(this.originalImage, 90);
                break;
        }
    }

    private static BufferedImage rotateSquareImage(BufferedImage originalImage, double angleDegrees) {
        int size = originalImage.getWidth();
        BufferedImage rotatedImage = new BufferedImage(size, size, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angleDegrees), size / 2.0, size / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();
        return rotatedImage;
    }
}
