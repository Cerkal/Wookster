package entity;

import main.KeyHandler;
import main.Utils;
import main.InventoryItem.InventoryItemWrapper;
import objects.ContainerObject;
import objects.GameMap;
import objects.SuperObject;
import objects.weapons.Weapon;
import objects.weapons.Weapon.WeaponType;
import spells.HealthSpell;
import spells.InvincibilitySpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;

public class Player extends Entity {

    public static class PlayerWrapper {
        public int worldX = Constants.TILE_SIZE * 23; // Set from level base
        public int worldY = Constants.TILE_SIZE * 21; 
        public int speed;
        public Direction direction;
        public int maxHealth;
        public int health;
        public WeaponType weapon;
        public HashMap<String, InventoryItemWrapper> inventory;
        public HashMap<SuperSpell.SpellType, SuperSpell> spells = new HashMap<>();
    }

    public KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public static final int DEFAULT_SPEED = 4;

    public HashMap<SuperSpell.SpellType, SuperSpell> spells = new HashMap<>();
    public Entity entityInDialogue;
    public Entity collisionEntity;
    public SuperObject collisionObject;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {

        super(gamePanel);
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.damageSound = Constants.SOUND_HURT;

        this.screenX = Constants.SCREEN_WIDTH/2 - (Constants.TILE_SIZE/2);
        this.screenY = Constants.SCREEN_HEIGHT/2 - (Constants.TILE_SIZE/2);

        setDefaultValues();
    }

    public void setDefaultValues() {
        this.speed = DEFAULT_SPEED;
        this.direction = Direction.DOWN;
        this.entityType = EntityType.PLAYER;
        this.health = this.maxHealth;
        this.invincable = false;
        this.weapon = null;
        this.isDead = false;
        this.inventory = new HashMap<>();
        this.weapons = new HashMap<>();
        this.spells.clear();

        addWeapon(WeaponType.FIST);
        GameMap gameMap = new GameMap(this.gamePanel);
        addInventoryItem(gameMap.inventoryItem);
        addCredits(Utils.generateRandomInt(50, 100));
        giveAllWeapons();
    }

    private void giveAllWeapons() {
        if (this.gamePanel.debugAllWeapons) {
            addWeapon(WeaponType.BLASTER);
            addWeapon(WeaponType.CROSSBOW);
            addWeapon(WeaponType.SWORD);
        }
    }

    public void update() {
        this.isMoving = false;
        if (((
                this.keyHandler.upPressed ||
                this.keyHandler.downPressed ||
                this.keyHandler.leftPressed ||
                this.keyHandler.rightPressed ||
                this.keyHandler.enterPressed
            )
            && !this.isDead
        )){
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
            moveEntity();            
        }
        collision();
        spellCheck();
        invincableCheck();
        weapon();
        checkDeath();
    }

    public void draw(Graphics2D graphics2D) {
        getSpiteImage();
        drawPlayerSprite(graphics2D);
        drawSpellEffect(graphics2D);
        drawEffect(graphics2D);
        drawDebugCollision(graphics2D, screenX, screenY);
    }

    public void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        obectCollision();
        entityCollision();
        this.gamePanel.eventHandler.checkEvent();
    }

    public void removeInventoryItem(InventoryItem item) {
        if (this.inventory.containsKey(item.name)) {
            List<InventoryItem> list = this.inventory.get(item.name);
            if (item.count > 1) {
                item.count--;
            } else {
                list.remove(0);
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
            InventoryItem firstCopy = new InventoryItem(items.get(0));
            firstCopy.count = totalCount;
            if (firstCopy.usable || firstCopy.visibility) {
                selectableMap.put(key, firstCopy);
            }
        }
        return selectableMap;
    }

    public PlayerWrapper getPlayerSaveState() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.worldX = getRawX();
        playerWrapper.worldY = getRawY();
        playerWrapper.speed = this.speed;
        playerWrapper.direction = this.direction;
        playerWrapper.maxHealth = this.maxHealth;
        playerWrapper.health = this.health;
        if (this.weapon != null) playerWrapper.weapon = this.weapon.weaponType;
        playerWrapper.inventory = getInventoryItemsForSave();
        playerWrapper.spells = this.spells;
        return playerWrapper;
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

    public List<InventoryItem> getInventoryItems() {
        List<InventoryItem> weaponList = new ArrayList<>();
        List<InventoryItem> otherList = new ArrayList<>();
        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);
            int totalCount = 0;
            for (InventoryItem item : items) {
                totalCount += item.count;
            }
            InventoryItem first = items.get(0);
            InventoryItem item = first.copy();
            item.count = totalCount;
            if (item.usable || item.visibility) {
                if (item.weapon != null) {
                    weaponList.add(item);
                } else {
                    otherList.add(item);
                }
            }
        }
        weaponList.sort(Comparator.comparing(weapon -> weapon.name));
        otherList.sort(Comparator.comparing(other -> other.name));
        List<InventoryItem> selectableList = new ArrayList<>();
        selectableList.addAll(weaponList);
        selectableList.addAll(otherList);
        return selectableList;
    }

    public HashMap<String, InventoryItemWrapper> getInventoryItemsForSave() {
        HashMap<String, InventoryItemWrapper> inventoryMap = new HashMap<>();
        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);
            int totalCount = 0;
            for (InventoryItem item : items) {
                totalCount += item.count;
                if (item.count == 1) {
                    inventoryMap.put(key, item.getInventoryWrapper());
                    continue;
                }
            }
            InventoryItem first = items.get(0);
            InventoryItemWrapper firstWrapper = first.getInventoryWrapper();
            firstWrapper.count = totalCount;
            inventoryMap.put(key, firstWrapper);
        }
        return inventoryMap;
    }

    public void addWeapon(WeaponType weaponType) {
        Weapon weapon = Weapon.WeaponType.create(gamePanel, weaponType, this);
        this.weapons.put(weaponType, weapon);
        switchWeapon(weaponType);
    }

    public void switchWeapon(WeaponType weaponType) {
        if (!this.weapons.containsKey(weaponType)) { return; }
        this.weapon = this.weapons.get(weaponType);
    }

    public void closeContainer() {
        if (this.collisionObject != null && this.collisionObject instanceof ContainerObject) {
            ContainerObject chest = (ContainerObject) this.collisionObject;
            chest.close();
        }
    }

    public String getContainerName() {
        String name = "Container";
        if (this.collisionObject != null && this.collisionObject instanceof ContainerObject) {
            name = this.collisionObject.name;
        }
        return name;
    }

    protected void moveEntity() {
        if (!this.collisionOn) {
            switch (this.direction) {
                case UP:
                    this.worldY -= speed;
                    break;
                case DOWN:
                    this.worldY += speed;
                    break;
                case LEFT:
                    this.worldX -= speed;
                    break;
                case RIGHT:
                    this.worldX += speed;
                    break;
            }
        }
    }

    public void weapon() {
        if (this.weapon == null) { return; }
        if (
            this.collisionEntity == null ||
            (this.collisionEntity != null && this.collisionEntity.entityType == EntityType.ENEMY) ||
            (this.collisionEntity != null && this.collisionEntity.entityType == EntityType.ANIMAL)
        ){
            this.weapon.shoot();
        }
    }

    private void checkDeath() {
        if (this.isDead) {
            this.gamePanel.death();
        }
    }

    private void drawSpellEffect(Graphics2D graphics2D) {
        if (this.spells.size() == 0) return;
        Sprite effectSprite = this.spriteManager.getSprite(this, SpriteAnimation.EFFECT.name());
        graphics2D.drawImage(effectSprite.image, screenX, screenY, null);
    }

    private void drawPlayerSprite(Graphics2D graphics2D) {
        graphics2D.drawImage(
            this.sprite.image,
            screenX - this.sprite.xAdjust,
            screenY - this.sprite.yAdjust,
            null
        );
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
        this.collisionObject = gamePanel.collision.objectCollision(this, true);
        if (this.collisionObject == null) return;
        this.collisionObject.activateObject();
    }

    private void entityCollision() {
        this.entityInDialogue = null;
        this.collisionEntity = this.gamePanel.collision.entityCollision(this);
        if (
            this.collisionEntity != null &&
            this.collisionEntity.isFriendly &&
            this.collisionEntity.entityType == EntityType.NPC
        ){
            if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
                this.entityInDialogue = collisionEntity;
                this.collisionEntity.speak();
            }
        }
    }

    private void spellCheck() {
        timedSpell();
        keySpell();
        speedSpell();
        healthSpell();
        invincibilitySpell();
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

    private void invincibilitySpell() {
        try {
            if (this.spells.get(SpellType.INVINCIBILITY_SPELL) == null) {
                return;
            };
            this.invincable = true;
            InvincibilitySpell currentInvincibilitySpell = (InvincibilitySpell) spells.get(SpellType.INVINCIBILITY_SPELL);
            if (currentInvincibilitySpell.spellTime == 0) {
                this.invincable = false;
                currentInvincibilitySpell.removeSpell(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadSprites() {

        String e = SpriteAnimation.EFFECT.name();
        spriteManager.setSprite(e, new Sprite(null, Constants.SPELL_EFFECT_SPARKLE_0));
        spriteManager.setSprite(e, new Sprite(null, Constants.SPELL_EFFECT_SPARKLE_1));
        spriteManager.setSprite(e, new Sprite(null, Constants.SPELL_EFFECT_SPARKLE_2));

        String m = SpriteAnimation.MOVE.name();
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_1));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_1));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_1));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_1));

        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));

        String crossbow = WeaponType.CROSSBOW.name();
        spriteManager.setSprite(crossbow, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_1));

        String blaster = WeaponType.BLASTER.name();
        spriteManager.setSprite(blaster, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_BLASTER_UP_0));
        spriteManager.setSprite(blaster, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_BLASTER_UP_1));
        spriteManager.setSprite(blaster, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_BLASTER_DOWN_0));
        spriteManager.setSprite(blaster, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_BLASTER_DOWN_1));
        spriteManager.setSprite(blaster, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_BLASTER_LEFT_0));
        spriteManager.setSprite(blaster, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_BLASTER_LEFT_1));
        spriteManager.setSprite(blaster, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_BLASTER_RIGHT_0));
        spriteManager.setSprite(blaster, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_BLASTER_RIGHT_1));

        String fist = WeaponType.FIST.name();
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_FIST_UP_0));
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_FIST_UP_1));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_FIST_DOWN_0));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_FIST_DOWN_1));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_FIST_LEFT_0));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_FIST_LEFT_1));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_FIST_RIGHT_0));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_FIST_RIGHT_1));

        String sword = WeaponType.SWORD.name();
        spriteManager.setSprite(sword, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_SWORD_UP_0));
        spriteManager.setSprite(sword, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_SWORD_UP_1));
        spriteManager.setSprite(sword, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_SWORD_DOWN_0));
        spriteManager.setSprite(sword, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_SWORD_DOWN_1));
        spriteManager.setSprite(sword, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_SWORD_LEFT_0));
        spriteManager.setSprite(sword, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_SWORD_LEFT_1));
        spriteManager.setSprite(sword, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_SWORD_RIGHT_0));
        spriteManager.setSprite(sword, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_SWORD_RIGHT_1));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PLAYER_IMAGE_DEAD));
    }

    public void loadPlayerSaveState(PlayerWrapper playerWrapper) {
        this.speed = DEFAULT_SPEED;
        this.direction = playerWrapper.direction;
        this.maxHealth = playerWrapper.maxHealth;
        this.health = playerWrapper.health;
    }
}
