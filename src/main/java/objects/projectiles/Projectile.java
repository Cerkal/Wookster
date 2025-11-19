package objects.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;

import entity.Entity;
import entity.Entity.Direction;
import entity.Player;
import main.Constants;
import main.GamePanel;
import main.Utils;

public class Projectile {

    protected GamePanel gamePanel;
    protected BufferedImage image;
    protected BufferedImage originalImage;

    public int worldX;
    public int worldY;
    public double offset = 0;
    public Direction direction;
    public boolean collisionOn = false;
    public Entity entity;

    public Rectangle solidArea = new Rectangle(Constants.TILE_SIZE/2, Constants.TILE_SIZE/2, 2, 2);
    public int solidAreaDefaultX = solidArea.x;
    public int solidAreaDefaultY = solidArea.y;

    public double npcDamage = .6;
    public int speed = 14;
    public int damage = 10;

    public int dispose;
    public long lastShot;

    public int price = 1;

    protected double velocityX = 0;
    protected double velocityY = 0;
    protected Entity target;

    boolean mouseAimSet;

    private HashMap<Double, BufferedImage> rotatedImages = new HashMap<>();

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
    }

    public void init() {
        mouseAimSet = isMouseAimSet();

        int targetX = 0;
        int targetY = 0;
        this.target = entity.getAttackingTarget();

        // PLAYER
        if (entity instanceof Player) {
            // MOUSR AIM
            if (mouseAimSet) {
                targetX = (int) (this.gamePanel.mouseHandler.target.getX()
                    - this.gamePanel.player.screenX
                    + this.gamePanel.player.worldX);
                targetY = (int) (this.gamePanel.mouseHandler.target.getY()
                    - this.gamePanel.player.screenY
                    + this.gamePanel.player.worldY);
            // NO MOUSE AIM
            } else {
                switch (this.direction) {
                    case UP:    this.velocityY = -this.speed; break;
                    case DOWN:  this.velocityY = this.speed; break;
                    case LEFT:  this.velocityX = -this.speed; break;
                    case RIGHT: this.velocityX = this.speed; break;
                    default: break;
                }
                return;
            }
        // ENTITY
        } else {
            // TARGET NULL
            if (this.target == null) {
                switch (this.direction) {
                    case UP:    this.velocityY = -this.speed; break;
                    case DOWN:  this.velocityY = this.speed; break;
                    case LEFT:  this.velocityX = -this.speed; break;
                    case RIGHT: this.velocityX = this.speed; break;
                    default: break;
                }
                return;
            }
            // TARGET SET
            targetX = this.target.worldX;
            targetY = this.target.worldY;
        }
        
        int diff = Math.min(
            Math.abs(entity.worldX - targetX),
            Math.abs(entity.worldY - targetY)
        );
        diff = (int) (Utils.generateRandomInt(diff * -1, diff) * (100 - entity.accuracy) * .01);
        double dx = targetX - entity.worldX;
        double dy = targetY - entity.worldY;
        switch (this.direction) {
            case UP, DOWN:
                dx = (targetX + diff) - entity.worldX;
                break;
            case LEFT, RIGHT:
                dy = (targetY + diff) - entity.worldY;
                break;
        }
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance != 0) {
            this.velocityX = (dx / distance) * this.speed;
            this.velocityY = (dy / distance) * this.speed;
        }
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
        if (this.image != null) { return; }
        try {
            this.originalImage = ImageIO.read(getClass().getResourceAsStream(image));
            this.image = this.originalImage;
            // Pre-rotate images for cardinal directions
            rotatedImages.put(0.0, this.originalImage);
            rotatedImages.put(90.0, rotateImage(this.originalImage, 90));
            rotatedImages.put(180.0, rotateImage(this.originalImage, 180));
            rotatedImages.put(270.0, rotateImage(this.originalImage, 270));
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

        if (this instanceof MeleeProjectile) {
            this.worldX = (int) (this.entity.worldX + (this.velocityX * 1.5));
            this.worldY = (int) (this.entity.worldY + (this.velocityY * 1.5));
        } else {
            this.worldX += this.velocityX;
            this.worldY += this.velocityY;
        }

        if (this.entity instanceof Player && !mouseAimSet) {
            switch (this.direction) {
                case UP:    this.image = rotatedImages.get(0.0); break;
                case DOWN:  this.image = rotatedImages.get(180.0); break;
                case LEFT:  this.image = rotatedImages.get(270.0); break;
                case RIGHT: this.image = rotatedImages.get(90.0); break;
            }
            return;
        }

        double angle = Math.toDegrees(Math.atan2(this.velocityY, this.velocityX)) + 90;
        angle = ((angle % 360) + 360) % 360; // Normalize angle to [0, 360]
        BufferedImage cached = rotatedImages.get(angle);
        if (cached == null) {
            cached = rotateImage(this.originalImage, angle);
            rotatedImages.put(angle, cached);
        }
        this.image = cached;
    }

    private static BufferedImage rotateImage(BufferedImage originalImage, double angleDegrees) {
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
        if (entity instanceof Player) {
            switch (this.gamePanel.difficulty) {
                case EASY:
                    adjustDamage(.5);
                    break;
                case MEDIUM:
                    adjustDamage(.75);
                    break;
                case HARD:
                    adjustDamage(1);
                    break;
                default:
                    adjustDamage(.5);
                    break;
            }
        }
        entity.takeDamage(this.damage, this.entity);
    }

    public void adjustDamage(double amount) {
        this.damage *= amount;
    }

    private boolean isMouseAimSet() {
        return this.gamePanel.mouseAim && this.gamePanel.mouseHandler.target != null;
    }
}
