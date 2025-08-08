package entity;

import main.KeyHandler;
import objects.SuperObject;
import objects.weapons.CrossbowWeapon;
import objects.weapons.Weapon;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;
import tile.Tile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public static final int DEFAULT_SPEED = 4;

    public HashMap<SuperSpell.SpellType, SuperSpell> spells = new HashMap<>();
    public HashMap<String, Integer> inventory = new HashMap<>();
    public Entity entityInDialogue;
    public Weapon weapon;
    public Entity collisionEntity;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = Constants.SCREEN_WIDTH/2 - (Constants.TILE_SIZE/2);
        screenY = Constants.SCREEN_HEIGHT/2 - (Constants.TILE_SIZE/2);

        this.solidArea = new Rectangle();
        this.solidArea.x = Constants.TILE_SIZE/4;
        this.solidArea.y = Constants.TILE_SIZE/2;
        this.solidArea.width = Constants.TILE_SIZE/2;
        this.solidArea.height = Constants.TILE_SIZE/2;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;

        this.damageSound = Constants.SOUND_HURT;

        setDefaultValues();
        getPlayerImage();

        // Weapon test
        this.weapon = new CrossbowWeapon(gamePanel);
    }

    public void setDefaultValues() {
        this.worldX = Constants.TILE_SIZE * 23;
        this.worldY = Constants.TILE_SIZE * 21;
        this.speed = DEFAULT_SPEED;
        this.direction = Direction.DOWN;
        this.entityType = Entity_Type.PLAYER;
    }

    public void update() {
        isMoving = false;
        if (
            (
                (
                    keyHandler.upPressed ||
                    keyHandler.downPressed ||
                    keyHandler.leftPressed ||
                    keyHandler.rightPressed
                ) && !isDead
            )
        ){
            isMoving = true;

            if (keyHandler.upPressed) {
                direction = Direction.UP;
            }
            if (keyHandler.downPressed) {
                direction = Direction.DOWN;
            }
            if (keyHandler.leftPressed) {
                direction = Direction.LEFT;
            }
            if (keyHandler.rightPressed) {
                direction = Direction.RIGHT;
            }
            collision();
            moveEntiy();            
        }
        spellCheck();

        if (this.collisionEntity == null && this.weapon != null) {
            if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
                this.weapon.shoot();
            }
        }
    }


    public void addInventoryItem(String objectType) {
        this.gamePanel.ui.displayMessage(objectType + Constants.INVENTORY_ADDED_MESSAGE);
        if (this.inventory.containsKey(objectType)) {
            Integer quantity = inventory.get(objectType);
            quantity++;
            this.inventory.put(objectType, quantity);
        } else {
            this.inventory.put(objectType, 1);
        }
    }

    public void removeInventoryItem(String objectType) {
        if (this.inventory.containsKey(objectType)) {
            int quantity = inventory.get(objectType);
            if (quantity == 1) {
                this.inventory.remove(objectType);
            } else {
                quantity--;
                this.inventory.put(objectType, quantity);
            }
        }
    }

    public int getInventoryItem(String objectType) {
        if (this.inventory.get(objectType) == null) return 0;
        return this.inventory.get(objectType);
    }

    public void draw(Graphics2D graphics2D) {
        getSpriteByDirection();
        graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        drawSpellEffect(graphics2D);
        drawEffect(graphics2D);
    }

    public void getPlayerImage() {
        try {
            this.up1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_0));
            this.up2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_1));
            this.down1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_0));
            this.down2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_1));
            this.left1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_0));
            this.left2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_1));
            this.right1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_0));
            this.right2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_1));
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DEAD));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    private void drawSpellEffect(Graphics2D graphics2D) {
        if (this.spells.size() == 0) return;
        Tile sparkle = new Tile();
        try {
            sparkle.imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.SPELL_EFFECT_SPARKLE_0)));
            sparkle.imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.SPELL_EFFECT_SPARKLE_1)));
            sparkle.imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.SPELL_EFFECT_SPARKLE_2)));
            sparkle.collision = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sparkle != null) {
            BufferedImage sparkleImage = sparkle.getCurrentImage(this.gamePanel.gameTime);
            graphics2D.drawImage(sparkleImage, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        }
    }

    private void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        obectCollision();
        entityCollision();
        this.gamePanel.eventHandler.checkEvent();
    }

    private void obectCollision() {
        SuperObject collisionObject = gamePanel.collision.objectCollision(this, true);
        if (collisionObject == null) return;
        collisionObject.activateObject();
    }

    private void entityCollision() {
        this.entityInDialogue = null;
        this.collisionEntity = this.gamePanel.collision.entityCollision(this);
        if (this.collisionEntity != null) {
            if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
                if (this.collisionEntity.entityType == Entity_Type.NPC) {
                    this.gamePanel.gameState = GamePanel.GameState.DIALOGUE;
                    this.collisionEntity.speak();
                    this.entityInDialogue = collisionEntity;
                }
            }
        }
    }

    private void spellCheck() {
        brokenKey();
        timedSpell();
        speedSpell();
        healthSpell();
    }

    private void brokenKey() {
        if (this.spells.get(SpellType.KEY_SPELL) == null) return;
        KeySpell currentKeySpell = (KeySpell) spells.get(SpellType.KEY_SPELL);
        if (currentKeySpell.brokeDirection == this.direction) {
            this.speed = 0;
            currentKeySpell.brokenSet = EnumSet.noneOf(Direction.class);
        } else {
            this.speed = DEFAULT_SPEED;
            currentKeySpell.brokenSet.add(direction);
        }
        if (currentKeySpell.brokenSet.size() == KeySpell.KEY_COUNT) {
            currentKeySpell.removeSpell(this);
        }
    }

    private void timedSpell() {
        try {
            for (SpellType spellName : this.spells.keySet()) {
                SuperSpell spell = this.spells.get(spellName);
                if (spell.startTime != 0) {
                    long diff = this.gamePanel.gameTime - spell.startTime;
                    if ((diff / Constants.NANO_SECOND) > spell.spellTime) {
                        spell.removeSpell(this);
                    }
                }
            }
        } catch (Exception e) {
            // Spell was removed, fail silently
        }
    }

    private void speedSpell() {
        try {
            SpeedSpell currentSpeedSpell = (SpeedSpell) spells.get(SpellType.SPEED_SPELL);
            if (currentSpeedSpell != null) {
                this.speed = currentSpeedSpell.speed;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void healthSpell() {
        try {
            if (this.spells.get(SpellType.HEALTH_SPELL) == null) {
                return;
            };
            HealthSpell currentHealthSpell = (HealthSpell) spells.get(SpellType.HEALTH_SPELL);
            if (currentHealthSpell.spellTime == 0) {
                adjustHealth(currentHealthSpell.healthAmount);
                currentHealthSpell.removeSpell(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
