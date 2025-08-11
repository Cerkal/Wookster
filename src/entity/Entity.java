package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import effects.AlertEffect;
import effects.BloodEffect;
import effects.Effect;
import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon;

public class Entity {

    GamePanel gamePanel;

    public static final int SOLID_AREA_X = 10;
    public static final int SOLID_AREA_Y = 5;
    public static final int SOLID_AREA_WIDTH = 28;
    public static final int SOLID_AREA_HEIGHT = 42;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum Entity_Type { PLAYER, NPC, ENEMY }
    List<Direction> availableDirections = new ArrayList<>(EnumSet.allOf(Direction.class));

    // Location
    public int worldX, worldY;
    public int speed;
    public Direction direction;

    // Sprite
    protected int spriteCounter = 0;
    protected int spriteNumber = 0;
    protected boolean isMoving = false;
    public HashMap<Direction, List<BufferedImage>> imageMap = new HashMap<>();
    public HashMap<Direction, List<BufferedImage>> imageMapDefault = new HashMap<>();
    public BufferedImage image;
    public BufferedImage dead;

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public boolean movable = true;
    public boolean invincable = false;
    public int invincableCounter;
    public boolean isDead;

    // Entity Values
    public Entity_Type entityType;
    public String name = "";
    public int maxHealth = 100;
    public int health = 100;
    public String[] dialogue;
    public int dialogueIndex = 0;
    public Effect effect;
    public Weapon weapon;

    // Sounds
    protected String damageSound;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.solidArea = new Rectangle();
        this.solidArea.x = SOLID_AREA_X;
        this.solidArea.y = SOLID_AREA_Y;
        this.solidArea.width = SOLID_AREA_WIDTH;
        this.solidArea.height = SOLID_AREA_HEIGHT;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }

    public Entity(GamePanel gamePanel, int worldX, int worldY) {
        this(gamePanel);
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
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
            drawDebugCollision(graphics2D, screenX, screenY);
        }
        drawEffect(graphics2D);
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

    public void update() {
        setAction();
        collision();
    }

    public void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        this.gamePanel.collision.entityCollision(this);
        checkPlayerCollision();
    }

    public void checkPlayerCollision() {
        Entity entity = this.gamePanel.collision.getCollidEntity(this, this.gamePanel.player);
        if (entity != null) {
            handlePlayerCollision(this.gamePanel.player);
        }
    }

    public void handlePlayerCollision(Player player) {
        // for override in sub class
    }

    public void speak() {
        if (this.dialogue == null) { return; }
        this.gamePanel.gameState = GamePanel.GameState.DIALOGUE;
        if (this.dialogueIndex >= this.dialogue.length) {
            this.gamePanel.ui.stopDialogue();
            this.dialogueIndex = 0;
            return;
        }
        this.gamePanel.ui.displayDialog(this.dialogue[this.dialogueIndex]);
        this.dialogueIndex++;
    
        this.direction = getOppositeDirection(this.gamePanel.player.direction);
    }

    public Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case Direction.UP:
                return Direction.DOWN;
            case Direction.DOWN:
                return Direction.UP;
            case Direction.LEFT:
                return Direction.RIGHT;
            case Direction.RIGHT:
                return Direction.LEFT;
        }
        return null;
    }

    protected void getSpriteByDirection() {
        if (this.isDead) {
            this.image = this.dead;
            return;
        }
        this.image = this.imageMap.get(this.direction).get(this.spriteNumber);
    }

    public void setAction() {
        if (!this.movable) { return; }
        this.actionLockCounter++;
        if (this.collisionOn || this.actionLockCounter > Math.random() * 120 + 50) {
            Random random = new Random();
            int randomIndex = random.nextInt(availableDirections.size());
            this.direction = availableDirections.get(randomIndex);
            this.actionLockCounter = 0;
        }
        moveEntiy();
    }

    public void takeDamage(int amount) {
        if (this.invincable) { return; }
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;
            this.movable = false;
            this.weapon = null;
        }
        this.gamePanel.playSoundEffect(this.damageSound);
        this.gamePanel.effects.add(new BloodEffect(this.gamePanel, this.worldX, this.worldY));
        this.effect = new AlertEffect(this.gamePanel, this);
        System.out.println(this.entityType + ": " + getCurrentHealth());
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
