package objects.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entity.Entity;
import entity.Entity.Direction;
import entity.Player;
import main.Constants;
import main.GamePanel;

public class Projectile {

    protected GamePanel gamePanel;
    protected BufferedImage image;
    protected BufferedImage originalImage;

    public int worldX;
    public int worldY;
    public Direction direction;
    public boolean collisionOn = false;
    public Entity entity;

    public Rectangle solidArea = new Rectangle(Constants.TILE_SIZE/2, Constants.TILE_SIZE/2, 1, 1);
    public int solidAreaDefaultX = solidArea.x;
    public int solidAreaDefaultY = solidArea.y;

    public int speed = 14;
    public int damage = 10;

    public int dispose;
    public long lastShot;

    public int price = 1;

    public enum ProjectileType {
        ARROWS,
        LASERS
    };

    public Projectile(GamePanel gamePanel, Entity entity) {
        this.gamePanel = gamePanel;
        this.direction = entity.direction;
        this.lastShot = gamePanel.gameTime;
        this.entity = entity;
        this.worldX = entity.worldX;
        this.worldY = entity.worldY;
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
    }

    public void draw(Graphics2D graphics2D) {
        update();
        collision();
        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (
            !collisionOn
        ) {
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        } else {
            this.gamePanel.projectileManager.toRemove.add(this);
        }
        drawDebugCollision(graphics2D, screenX, screenY);
    }

    public void drawDebugCollision(Graphics2D graphics2D, int screenX, int screenY) {
        if (!this.gamePanel.debugCollision) { return; }
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(
            screenX + solidArea.x,
            screenY + solidArea.y,
            solidArea.width,
            solidArea.height
        );
    }

    public void setImage(String image) {
        try {
            this.originalImage = ImageIO.read(getClass().getResourceAsStream(image));
            this.image = this.originalImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        if (dispose > 0) {
            Long time = (this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND;
            if (time > this.dispose / 2) {
                this.gamePanel.projectileManager.toRemove.add(this);
            }
        }
        switch (this.direction) {
            case UP:
                this.worldY -= this.speed;
                this.image = this.originalImage;
                break;
            case DOWN:
                this.worldY += this.speed;
                this.image = rotateSquareImage(this.originalImage, 180);
                break;
            case LEFT:
                this.worldX -= this.speed;
                this.image = rotateSquareImage(this.originalImage, 270);
                break;
            case RIGHT:
                this.worldX += this.speed;
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
        return rotatedImage;
    }

    protected void collision() {
        this.gamePanel.collision.checkTileProjectile(this);
        Entity entity = this.gamePanel.collision.projectileCollision(this);
        if (entity != null) {
            handleEntityCollision(entity);
        }
        entity = (Player) this.gamePanel.collision.getProjectileEntity(this, this.gamePanel.player);
        if (entity != null) {
            handleEntityCollision(entity);
        }
    }

    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }
}
