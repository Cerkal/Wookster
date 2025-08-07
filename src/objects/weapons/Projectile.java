package objects.weapons;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entity.Entity.Direction;
import main.Constants;
import main.GamePanel;

public class Projectile {

    GamePanel gamePanel;
    BufferedImage image;
    BufferedImage originalImage;
    
    public int worldX;
    public int worldY;
    public Direction direction;
    public boolean collisionOn = false;

    // Collision
    protected Rectangle SOLID_AREA_FULL  = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    protected Rectangle solidAreaUp    = SOLID_AREA_FULL;
    protected Rectangle solidAreaDown  = SOLID_AREA_FULL;
    protected Rectangle solidAreaRight = SOLID_AREA_FULL;
    protected Rectangle solidAreaLeft  = SOLID_AREA_FULL;
    
    public Rectangle solidArea = solidAreaUp;
    public int speed = 14;
    public int damage = 10;

    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public Projectile(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.direction = gamePanel.player.direction;
        this.worldX = this.gamePanel.player.worldX;
        this.worldY = this.gamePanel.player.worldY;
        this.solidAreaDefaultX = this.worldX;
        this.solidAreaDefaultY = this.worldY;
        setImage(Constants.WEAPON_PROJECTILE_ARROW);
    }

    public void draw(Graphics2D graphics2D) {
        getDirection();
        this.gamePanel.collision.checkTileProjectile(this);
        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (
            this.worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
            this.worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
            this.worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
            this.worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.screenY) &&
            collisionOn == false
        ){
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        } else {
            this.gamePanel.projectiles.remove(0);
        }
    }

    private void getDirection() {
        switch (this.direction) {
            case Direction.UP:
                this.worldY -= this.speed;
                this.solidArea = this.solidAreaUp;
                this.image = this.originalImage;
                break;
            case Direction.DOWN:
                this.worldY += this.speed;
                this.solidArea = this.solidAreaDown;
                this.image = rotateSquareImage(this.originalImage, 180);
                break;
            case Direction.LEFT:
                this.worldX -= this.speed;
                this.solidArea = this.solidAreaLeft;
                this.image = rotateSquareImage(this.originalImage, 270);
                break;
            case Direction.RIGHT:
                this.worldX += this.speed;
                this.solidArea = this.solidAreaRight;
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

    protected void setImage(String image) {
        try {
            this.originalImage = ImageIO.read(getClass().getResourceAsStream(image));
            this.image = this.originalImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
