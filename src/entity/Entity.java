package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import effects.AlertEffect;
import effects.BloodEffect;
import effects.Effect;
import main.Constants;
import main.GamePanel;
import main.Utils;

public class Entity {

    GamePanel gamePanel;
    boolean debugCollision = false;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum Entity_Type { PLAYER, NPC, ENEMY }
    public EnumSet<Direction> attemptedDirections = EnumSet.allOf(Direction.class);

    // Location
    public int worldX, worldY;
    public int speed;
    public BufferedImage image;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, dead;
    public Direction direction;

    // Sprite
    protected int spriteCounter = 0;
    protected int spriteNumber = 0;
    protected boolean isMoving = false;

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public boolean movable = true;
    public boolean invincable = false;
    public int invincableCounter;

    // Entity Values
    public Entity_Type entityType;
    public String name = "";
    public int maxHealth = 100;
    public int health = 100;
    public String[] dialogue;
    public int dialogueIndex = 0;
    public Effect effect;

    public boolean isDead = false;

    // Sounds
    protected String damageSound;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public Entity(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
        this.isMoving = true;
    }

    public void draw(Graphics2D graphics2D) {
        getSpriteByDirection();
        int screenX = this.worldX - this.gamePanel.player.worldX + this.gamePanel.player.screenX;
        int screenY = this.worldY - this.gamePanel.player.worldY + this.gamePanel.player.screenY;

        if (
            worldX + (Constants.TILE_SIZE) > (this.gamePanel.player.worldX - this.gamePanel.player.screenX) &&
            worldX - (Constants.TILE_SIZE) < (this.gamePanel.player.worldX + this.gamePanel.player.screenX) &&
            worldY + (Constants.TILE_SIZE) > (this.gamePanel.player.worldY - this.gamePanel.player.screenY) &&
            worldY - (Constants.TILE_SIZE) < (this.gamePanel.player.worldY + this.gamePanel.player.screenY)
        ){
            graphics2D.drawImage(image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);

            if (this.debugCollision) {
                graphics2D.setColor(java.awt.Color.RED);
                graphics2D.drawRect(
                    screenX + solidArea.x,
                    screenY + solidArea.y,
                    solidArea.width,
                    solidArea.height
                );
            }
        }

        drawEffect(graphics2D);
    }

    public void update() {
        setAction();
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        this.gamePanel.collision.entityCollision(this);
        this.gamePanel.collision.getCollidEntity(this, gamePanel.player);
        this.gamePanel.collision.objectCollision(this, false);
    }

    public void speak() {
        if (this.dialogueIndex >= this.dialogue.length) {
            this.gamePanel.ui.stopDialogue();
            this.dialogueIndex = 0;
            return;
        }
        this.gamePanel.ui.displayDialog(this.dialogue[this.dialogueIndex]);
        this.dialogueIndex++;
    
        switch (this.gamePanel.player.direction) {
            case Direction.UP:
                this.direction = Direction.DOWN;
                break;
            case Direction.DOWN:
                this.direction = Direction.UP;
                break;
            case Direction.LEFT:
                this.direction = Direction.RIGHT;
                break;
            case Direction.RIGHT:
                this.direction = Direction.LEFT;
                break;
        }
    }

    protected void getSpriteByDirection() {
        if (this.isDead) {
            this.image = this.dead;
            return;
        }
        switch (this.direction) {
            case Direction.UP:
                if (this.spriteNumber == 0) {
                    this.image = this.up1;
                } else {
                    this.image = this.up2;
                }
                break;
            case Direction.DOWN:
                if (this.spriteNumber == 0) {
                    this.image = this.down1;
                } else {
                    this.image = this.down2;
                }
                break;
            case Direction.LEFT:
                if (this.spriteNumber == 0) {
                    this.image = this.left1;
                } else {
                    this.image = this.left2;
                }
                break;
            case Direction.RIGHT:
                if (this.spriteNumber == 0) {
                    this.image = this.right1;
                } else {
                    this.image = this.right2;
                }
                break;
        }
    }

    public void setAction() {
        if (!this.movable) { return; }
        this.actionLockCounter++;
        if (this.attemptedDirections.isEmpty()) {
            this.attemptedDirections = EnumSet.allOf(Direction.class);
        }
        if (this.collisionOn) {
            System.out.println(this.attemptedDirections);
        }
        List<Direction> availableDirections = new ArrayList<>(this.attemptedDirections);
        if (this.collisionOn || this.actionLockCounter > Math.random() * 120 + 50) {
            int randomIndex = Utils.generateRandomInt(0, this.attemptedDirections.size() - 1);
            this.direction = availableDirections.get(randomIndex);
            switch (this.direction) {
                case Direction.UP:
                    this.worldY -= this.speed;
                    break;
                case Direction.DOWN:
                    this.worldY += this.speed;
                    break;
                case Direction.RIGHT:
                    this.worldX += this.speed;
                    break;
                case Direction.LEFT:
                    this.worldX -= this.speed;
                    break;
            }
            this.actionLockCounter = 0;
        }
        moveEntiy();
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;
            this.movable = false;
        }
        this.gamePanel.playSoundEffect(this.damageSound);
        this.gamePanel.effects.add(new BloodEffect(this.gamePanel, this.worldX, this.worldY));
        this.effect = new AlertEffect(this.gamePanel, this);
    }

    public void increaseHealth(double amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void adjustHealth(double amount) {
        if (amount > 0) {
            increaseHealth(Math.abs(amount));
        } else {
            takeDamage((int) Math.abs(amount));
        }
    }

    public int getCurrentHealth() {
        if (this.maxHealth == 0) return 0;
        return (int) ((this.health * 100.0) / this.maxHealth);
    }

    protected void moveEntiy() {
        if (!this.collisionOn) {
            switch (this.direction) {
                case Direction.UP:
                    this.worldY -= speed;
                    break;
                case Direction.DOWN:
                    this.worldY += speed;
                    break;
                case Direction.LEFT:
                    this.worldX -= speed;
                    break;
                case Direction.RIGHT:
                    this.worldX += speed;
                    break;
            }
        }
        this.spriteCounter++;
        if (this.spriteCounter > 5 && this.isMoving) {
            if (this.spriteNumber == 1) {
                this.spriteNumber = 0;
            } else if (this.spriteNumber == 0) {
                this.spriteNumber = 1;
            }
            this.spriteCounter = 0;
        }
    }

    protected void drawEffect(Graphics2D graphics2D) {
        if (this.effect != null) {
            if ((this.gamePanel.gameTime - this.effect.startTime) / Constants.MILLISECOND > 500) {
                this.effect = null;
            } else {
                this.effect.worldX = this.worldX;
                this.effect.worldY = this.worldY;
                this.effect.draw(graphics2D);
            }
        }
    }
}
