package entity;

import main.KeyHandler;
import main.GamePanel.GameState;
import objects.SuperObject;
import objects.weapons.BlasterWeapon;
import objects.weapons.CrossbowWeapon;
import objects.weapons.FistWeapon;
import objects.weapons.SwordWeapon;
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
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;

public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public static final int DEFAULT_SPEED = 4;

    // Weapons
    public int hold = 0;
    public boolean attacking = false;
    public HashMap<Weapon_Type, Weapon> weapons = new HashMap<>();
    HashMap<Weapon_Type, HashMap<Direction, List<BufferedImage>>> imageMapWeapons = new HashMap<>();

    public HashMap<SuperSpell.SpellType, SuperSpell> spells = new HashMap<>();
    public HashMap<String, List<InventoryItem>> inventory = new HashMap<>();
    public Entity entityInDialogue;
    public Entity collisionEntity;
    
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.damageSound = Constants.SOUND_HURT;

        this.screenX = Constants.SCREEN_WIDTH/2 - (Constants.TILE_SIZE/2);
        this.screenY = Constants.SCREEN_HEIGHT/2 - (Constants.TILE_SIZE/2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.worldX = Constants.TILE_SIZE * 23;
        this.worldY = Constants.TILE_SIZE * 21;
        this.speed = DEFAULT_SPEED;
        this.direction = Direction.DOWN;
        this.entityType = Entity_Type.PLAYER;
        this.health = this.maxHealth;
        this.invincable = false;
        this.weapon = null;
        this.isDead = false;
        this.inventory = new HashMap<>();
        this.weapons = new HashMap<>();
        this.spells.clear();

        addWeapon(Weapon_Type.FIST);
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
        checkDeath();
    }

    public void draw(Graphics2D graphics2D) {
        getCurrentSpriteSet();
        getSpriteByDirection();
        drawPlayerSprite(graphics2D);
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

            // Fist
            HashMap<Direction, List<BufferedImage>> imageMapFist = new HashMap<>();
            imageMapFist.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_UP_1))
            )));
            imageMapFist.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_DOWN_1))
            )));
            imageMapFist.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_LEFT_1))
            )));
            imageMapFist.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_FIST_RIGHT_1))
            )));
            imageMapWeapons.put(Weapon_Type.FIST, imageMapFist);

            // Sword
            HashMap<Direction, List<BufferedImage>> imageMapSword = new HashMap<>();
            imageMapSword.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_UP_1))
            )));
            imageMapSword.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_DOWN_1))
            )));
            imageMapSword.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_LEFT_1))
            )));
            imageMapSword.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_SWORD_RIGHT_1))
            )));
            imageMapWeapons.put(Weapon_Type.SWORD, imageMapSword);

            this.imageMap = this.imageMapDefault;
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DEAD));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    public void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        obectCollision();
        entityCollision();
        this.gamePanel.eventHandler.checkEvent();
    }

    public void addInventoryItem(InventoryItem item) {
        if (item.count > 1) {
            this.gamePanel.ui.displayMessage(item.count + " " + item.name.toLowerCase() + Constants.MESSGE_INVENTORY_ADDED);
        } else {
            this.gamePanel.ui.displayMessage(item.name + Constants.MESSGE_INVENTORY_ADDED);
        }
        this.inventory.computeIfAbsent(item.name, k -> new ArrayList<>()).add(item);
    }

    public void removeInventoryItem(InventoryItem item) {
        if (this.inventory.containsKey(item.name)) {
            List<InventoryItem> list = this.inventory.get(item.name);
            if (item.count > 1) {
                item.count--;
            } else {
                list.remove(item);
            }
            if (list.isEmpty()) {
                this.inventory.remove(item.name);
            }
        }
    }

    public void removeInventoryItem(String name) {
        if (this.inventory.containsKey(name)) {
            List<InventoryItem> list = this.inventory.get(name);
            InventoryItem first = list.get(0);
            if (first.count > 1) {
                first.count--;
            } else {
                list.remove(0);
            }
            if (list.isEmpty()) {
                this.inventory.remove(name);
            }
        }
    }

    public int getInventoryItem(String objectType) {
        if (!this.inventory.containsKey(objectType)) { return 0; }
        int total = 0;
        for (InventoryItem item : this.inventory.get(objectType)) {
            total += item.count;
        }
        return total;
    }

    public HashMap<String, InventoryItem> getInventory() {
        HashMap<String, InventoryItem> selectableMap = new HashMap<>();
        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);
            int totalCount = 0;
            for (InventoryItem item : items) {
                totalCount += item.count;
            }
            InventoryItem firstCopy = items.get(0).copy();
            firstCopy.count = totalCount;
            if (firstCopy.usable || firstCopy.visibility) {
                selectableMap.put(key, firstCopy);
            }
        }
        return selectableMap;
    }

    public List<String> getInventoryString() {
        List<String> weaponList = new ArrayList<>();
        List<String> otherList = new ArrayList<>();
        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);
            int totalCount = 0;
            for (InventoryItem item : items) {
                totalCount += item.count;
            }
            InventoryItem first = items.get(0);
            if (first.usable || first.visibility) {
                String name = first.name;
                if (totalCount > 1) {
                    name += " (" + totalCount + ")";
                }
                if (first.weapon != null) {
                    weaponList.add(name);
                } else {
                    otherList.add(name);
                }
            }
        }
        Collections.sort(weaponList);
        Collections.sort(otherList);
        List<String> selectableList = new ArrayList<>();
        selectableList.addAll(weaponList);
        selectableList.addAll(otherList);
        return selectableList;
    }


    public void addWeapon(Weapon_Type weaponType) {
        switch (weaponType) {
            case Weapon_Type.BLASTER:
                this.weapons.put(weaponType, new BlasterWeapon(gamePanel, this));
                break;
            case Weapon_Type.CROSSBOW:
                this.weapons.put(weaponType, new CrossbowWeapon(gamePanel, this));
                break;
            case Weapon_Type.FIST:
                this.weapons.put(weaponType, new FistWeapon(gamePanel, this));
                break;
            case Weapon_Type.SWORD:
                this.weapons.put(weaponType, new SwordWeapon(gamePanel, this));
                break;
            default:
                break;
        }
        switchWeapon(weaponType);
    }

    public void switchWeapon(Weapon_Type weaponType) {
        if (!this.weapons.containsKey(weaponType)) { return; }
        this.weapon = this.weapons.get(weaponType);
    }

    private void weapon() {
        if (this.weapon == null) { return; }
        if (
            this.collisionEntity == null ||
            (this.collisionEntity != null && this.collisionEntity.entityType == Entity_Type.ENEMY)
        ){
            this.weapon.shoot();
        }
    }

    private void checkDeath() {
        if (this.isDead) {
            this.gamePanel.gameState = GameState.DEATH;
            this.gamePanel.config.saveConfig();
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

    private void drawPlayerSprite(Graphics2D graphics2D) {
        int screenX = this.screenX;
        int screenY = this.screenY;
        int width = Constants.TILE_SIZE;
        int height = Constants.TILE_SIZE;
        if (this.weapon != null && this.weapon.longSprite && this.attacking) {
            switch (this.direction) {
                case Direction.UP:
                    screenY -= Constants.TILE_SIZE;
                    height *= 2;
                    break;
                case Direction.DOWN:
                    height *= 2;
                    break;
                case Direction.RIGHT:
                    width *= 2;
                    break;
                case Direction.LEFT:
                    screenX -= Constants.TILE_SIZE;
                    width *= 2;
                    break;
            }
        }
        graphics2D.drawImage(this.image, screenX, screenY, width, height, null);
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
                    this.collisionEntity.speak();
                    this.entityInDialogue = collisionEntity;
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
