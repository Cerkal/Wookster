package entity;

import main.KeyHandler;
import objects.SuperObject;
import objects.projectiles.Projectile.Projectile_Type;
import objects.weapons.BlasterWeapon;
import objects.weapons.CrossbowWeapon;
import objects.weapons.Weapon;
import objects.weapons.Weapon.Weapon_Type;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;
import tile.Tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public static final int DEFAULT_SPEED = 4;

    // Weapons
    public int hold = 0;
    public boolean attacking = false;
    public Weapon weapon;
    HashMap<Weapon_Type, HashMap<Direction, List<BufferedImage>>> imageMapWeapons = new HashMap<>();

    public HashMap<SuperSpell.SpellType, SuperSpell> spells = new HashMap<>();
    public HashMap<String, Integer> inventory = new HashMap<>();
    public Entity entityInDialogue;
    public Entity collisionEntity;
    
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.damageSound = Constants.SOUND_HURT;

        screenX = Constants.SCREEN_WIDTH/2 - (Constants.TILE_SIZE/2);
        screenY = Constants.SCREEN_HEIGHT/2 - (Constants.TILE_SIZE/2);

        setDefaultValues();
        getPlayerImage();

        // Weapon test
        this.weapon = new CrossbowWeapon(gamePanel);
        this.inventory.put(this.weapon.projectileType.toString(), 10);

        this.weapon = new BlasterWeapon(gamePanel);
        this.inventory.put(this.weapon.projectileType.toString(), 50);
    }

    public void setDefaultValues() {
        this.worldX = Constants.TILE_SIZE * 23;
        this.worldY = Constants.TILE_SIZE * 21;
        this.speed = DEFAULT_SPEED;
        this.direction = Direction.DOWN;
        this.entityType = Entity_Type.PLAYER;
    }

    public void update() {
        this.isMoving = false;
        if (
            (
                (
                    this.keyHandler.upPressed ||
                    this.keyHandler.downPressed ||
                    this.keyHandler.leftPressed ||
                    this.keyHandler.rightPressed ||
                    this.keyHandler.enterPressed
                ) && !this.isDead
            )
        ){
            this.isMoving = true;

            if (this.keyHandler.upPressed) {
                this.direction = Direction.UP;
            }
            if (this.keyHandler.downPressed) {
                this.direction = Direction.DOWN;
            }
            if (this.keyHandler.leftPressed) {
                this.direction = Direction.LEFT;
            }
            if (this.keyHandler.rightPressed) {
                this.direction = Direction.RIGHT;
            }
            collision();
            moveEntiy();            
        }
        spellCheck();
        invincableCheck();
        weapon();
    }


    public void addInventoryItem(String objectType) {
        addInventoryItem(objectType, 1);
    }

    public void addInventoryItem(String objectType, int count) {
        if (count > 1) {
            this.gamePanel.ui.displayMessage(count + " " + objectType.toLowerCase() + Constants.MESSGE_INVENTORY_ADDED);
        } else {
            this.gamePanel.ui.displayMessage(objectType+ Constants.MESSGE_INVENTORY_ADDED);
        }
        if (this.inventory.containsKey(objectType)) {
            Integer quantity = inventory.get(objectType);
            quantity += 10;
            this.inventory.put(objectType, quantity);
        } else {
            this.inventory.put(objectType, count);
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
        getCurrentSpriteSet();
        getSpriteByDirection();
        graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        drawSpellEffect(graphics2D);
        drawEffect(graphics2D);
        drawDebugCollision(graphics2D, screenX, screenY);
    }

    public void getPlayerImage() {
        try {
            // Default
            this.imageMapDefault.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_1))
            )));
            this.imageMapDefault.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_1))
            )));
            this.imageMapDefault.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_1))
            )));
            this.imageMapDefault.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_1))
            )));

            // Crossbow
            HashMap<Direction, List<BufferedImage>> imageMapCrossBow = new HashMap<>();
            imageMapCrossBow.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_UP_1))
            )));
            imageMapCrossBow.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_DOWN_1))
            )));
            imageMapCrossBow.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_LEFT_1))
            )));
            imageMapCrossBow.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_1))
            )));
            imageMapWeapons.put(Weapon_Type.CROSSBOW, imageMapCrossBow);

            // Blaster
            HashMap<Direction, List<BufferedImage>> imageMapBlaster = new HashMap<>();
            imageMapBlaster.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_UP_1))
            )));
            imageMapBlaster.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_DOWN_1))
            )));
            imageMapBlaster.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_LEFT_1))
            )));
            imageMapBlaster.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_BLASTER_RIGHT_1))
            )));
            imageMapWeapons.put(Weapon_Type.BLASTER, imageMapBlaster);

            this.imageMap = this.imageMapDefault;
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DEAD));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    public void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        obectCollision();
        entityCollision();
        this.gamePanel.eventHandler.checkEvent();
    }

    private void weapon() {
        if (this.collisionEntity == null && this.weapon != null) {
            this.weapon.shoot();
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

    private void getCurrentSpriteSet() {
        if (this.attacking) {
            if (this.weapon != null && this.imageMapWeapons.get(this.weapon.weaponType) != null) {
                this.imageMap = this.imageMapWeapons.get(this.weapon.weaponType);
            }
        } else {
            this.imageMap = this.imageMapDefault;
        }
    }

    private void invincableCheck() {
        if (this.invincable) {
            invincableCounter++;
            if (invincableCounter > Constants.FPS) {
                invincable = false;
                invincableCounter = 0;
            }
        }
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
                } else {
                    this.attacking = true;
                }
            }
        }
    }

    private void spellCheck() {
        timedSpell();
        keySpell();
        speedSpell();
        healthSpell();
    }

    private void keySpell() {
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
