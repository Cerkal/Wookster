package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import main.Constants;
import main.GamePanel;

public class Entity {

    GamePanel gamePanel;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum Entity_Type { PLAYER, NPC, ENEMY }
    public EnumSet<Direction> attemptedDirections = EnumSet.allOf(Direction.class);

    // Location
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
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

    // Entity Values
    public Entity_Type entityType;
    public String name = "";
    public int maxHealth = 100;
    public int health = 100;
    public String[] dialogue;
    public int dialogueIndex = 0;

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
        BufferedImage image = getSpriteByDirection();
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (
            worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
            worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
            worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
            worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.worldY)
        ){
            graphics2D.drawImage(image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        }
    }

    public void update() {
        setAction();
        collisionOn = false;
        gamePanel.collision.checkTile(this);
        gamePanel.collision.getCollidEntity(this, gamePanel.player);
        gamePanel.collision.objectCollision(this, false);
    }

    public void speak() {
        if (dialogueIndex >= dialogue.length) {
            gamePanel.ui.stopDialogue();
            dialogueIndex = 0;
            return;
        }
        gamePanel.ui.displayDialog(dialogue[dialogueIndex]);
        dialogueIndex++;
    
        switch (gamePanel.player.direction) {
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

    protected BufferedImage getSpriteByDirection() {
        BufferedImage image = null;
        switch (this.direction) {
            case Direction.UP:
                if (spriteNumber == 0) {
                    image = up1;
                } else {
                    image = up2;
                }
                break;
            case Direction.DOWN:
                if (spriteNumber == 0) {
                    image = down1;
                } else {
                    image = down2;
                }
                break;
            case Direction.LEFT:
                if (spriteNumber == 0) {
                    image = left1;
                } else {
                    image = left2;
                }
                break;
            case Direction.RIGHT:
                if (spriteNumber == 0) {
                    image = right1;
                } else {
                    image = right2;
                }
                break;
        }
        return image;
    }

    public void setAction() {
        if (!this.movable) { return; }
        actionLockCounter++;
        if (this.attemptedDirections.isEmpty()) {
            this.attemptedDirections = EnumSet.allOf(Direction.class);
        }
        List<Direction> availableDirections = new ArrayList<>(this.attemptedDirections);
        if (this.collisionOn || actionLockCounter > Math.random() * 120 + 50) {
            Random random = new Random();
            int randomIndex = random.nextInt(availableDirections.size());
            direction = availableDirections.get(randomIndex);
            actionLockCounter = 0;
        }
        moveEntiy();
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
}
