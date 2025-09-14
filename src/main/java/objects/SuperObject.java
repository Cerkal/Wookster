package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import spells.ClaritySpell;
import spells.HealthSpell;
import spells.InvincibilitySpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;
import tile.TileManager.TileLocation;

public class SuperObject {

    public class SuperObjectWrapper {
        public String name;
        public int worldX;
        public int worldY;
        public ObjectType objectType;
        public SuperSpell spell;
        public String message;
        public boolean carriable;
    }

    GamePanel gamePanel;

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public boolean visibility = true;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public String soundPrimary;
    public String soundSecondary;
    public SuperSpell spell;
    public InventoryItem inventoryItem;
    public ObjectType objectType;
    public boolean isSpecial;
    public String message;
    public boolean carriable;
    public boolean sellable;
    public int price = 3;

    public HashMap<ObjectType, String> objectIcons = Constants.OBJECT_ICONS;
    private BufferedImage inventoryIcon;

    public SuperObject(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        TileLocation tileLocation = this.gamePanel.tileManager.getRandomTileLocation();
        this.worldX = tileLocation.worldX * Constants.TILE_SIZE;
        this.worldY = tileLocation.worldY * Constants.TILE_SIZE;
    }

    public SuperObject(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
    }

    public void draw(Graphics2D graphics2D) {
        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        if (
            this.worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
            this.worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
            this.worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
            this.worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.screenY) &&
            this.visibility == true
        ){
            graphics2D.drawImage(this.image, screenX, screenY, null);
        }
    }

    public void removeObject() {
        this.gamePanel.objects.remove(this);
        playPrimarySound();
    }

    public void playPrimarySound() {
        if (soundPrimary != null) {
            this.gamePanel.sound.playSoundEffect(soundPrimary);
        }
    }

    public void playSecondarySound() {
        if (soundSecondary != null) {
            this.gamePanel.sound.playSoundEffect(soundSecondary);
        }
    }

    public void activateObject() {
        this.gamePanel.ui.stopDialogue();
    }

    public void useObject() {
        setSpell();
        this.gamePanel.player.removeInventoryItem(this.inventoryItem);
    }

    public void removeInventoryItem() {
        if (this.objectType != SuperObject.ObjectType.POTION) return;
        for (int x = this.gamePanel.player.getRawX() - 1; x < this.gamePanel.player.getRawX() + 1; x++) {
            for (int y = this.gamePanel.player.getRawY() - 1; y < this.gamePanel.player.getRawY() + 1; y++) {
                if (this.gamePanel.tileManager.walkableTiles[x][y]) {
                    System.out.println(x);
                    System.out.println(y);
                    this.worldX = x * Constants.TILE_SIZE;
                    this.worldY = y * Constants.TILE_SIZE;
                    this.gamePanel.objects.add(this);
                    this.gamePanel.player.removeInventoryItem(this.inventoryItem);
                    return;
                }
            }
        }
    }

    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        if (this.inventoryIcon == null) {
            setIcon();
        }
        try {
            this.gamePanel.ui.drawInventoryIcon(graphics2D, x, y, this.inventoryIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphics2D.drawString(this.name, x, y);
    }

    public SuperObject shallowCopy() {
        SuperObject copy = new SuperObject(this.gamePanel);
        copy.name = this.name;
        copy.worldX = this.worldX;
        copy.worldY = this.worldY;
        copy.objectType = this.objectType;
        copy.spell = this.spell != null ? this.spell.copy() : null;
        copy.carriable = this.carriable;
        copy.sellable = this.sellable;
        copy.price = this.price;
        copy.inventoryItem = null;
        return copy;
    }

    protected void setIcon() {
        try {
            String imagePath = this.objectIcons.get(this.objectType);
            if (imagePath == null) { return; }
            this.inventoryIcon = ImageIO.read(getClass().getResourceAsStream(imagePath));
            int doubleTile = Constants.TILE_SIZE * 2;
            this.inventoryIcon = Utils.scaleImage(this.inventoryIcon, doubleTile, doubleTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setImage(String imagePath) {
        if (imagePath == null) { return; }
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            this.image = Utils.scaleImage(this.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpell() {
        if (this.spell != null) {
            this.spell.setSpell(this.gamePanel);
            if (this.spell.message != null) {
                this.gamePanel.ui.displayDialog(this.spell.message);
            }
        }
    }

    public SuperObjectWrapper getSuperObjectWrapper() {
        SuperObjectWrapper objectWrapper = new SuperObjectWrapper();
        objectWrapper.name = this.name;
        objectWrapper.objectType = this.objectType;
        objectWrapper.worldX = getRawX();
        objectWrapper.worldY = getRawY();
        objectWrapper.spell = this.spell;
        objectWrapper.carriable = (this.inventoryItem != null);
        return objectWrapper;
    }

    public int getRawX() {
        return this.worldX / Constants.TILE_SIZE;
    }

    public int getRawY() {
        return this.worldY / Constants.TILE_SIZE;
    }

    public enum ObjectType {
        ARROWS      ((gamePanel, object) -> new ArrowsObject(gamePanel, object.worldX, object.worldY)),
        CHEST       ((gamePanel, object) -> new ChestObject(gamePanel, object.worldX, object.worldY)),
        DOOR        ((gamePanel, object) -> new DoorObject(gamePanel, object.worldX, object.worldY)),
        LEVEL_DOOR  ((gamePanel, object) -> new LevelDoorObject(gamePanel, object.worldX, object.worldY)),
        KEY         ((gamePanel, object) -> new KeyObject(gamePanel, object.worldX, object.worldY)),
        LASERS      ((gamePanel, object) -> new LasersObject(gamePanel, object.worldX, object.worldY)),
        SIGN        ((gamePanel, object) -> new SignObject(gamePanel, object.worldX, object.worldY, object.message)),
        BLASER      ((gamePanel, object) -> new BlasterObject(gamePanel, object.worldX, object.worldY)),
        CROSSBOW    ((gamePanel, object) -> new CrossbowObject(gamePanel, object.worldX, object.worldY)),
        SWORD       ((gamePanel, object) -> new SwordObject(gamePanel, object.worldX, object.worldY)),
        JERMEY      ((gamePanel, object) -> new JermeyObject(gamePanel, object.worldX, object.worldY, null)),
        MAP         ((gamePanel, object) -> new GameMap(gamePanel)),
        POTION      ((gamePanel, object) -> object.carriable
                        ? new CarryPotionObject(gamePanel, SpellType.create(object.spell), object.worldX, object.worldY)
                        : new PotionObject(gamePanel, SpellType.create(object.spell), object.worldX, object.worldY));

        @FunctionalInterface
        public static interface ObjectCreator {
            SuperObject create(GamePanel gamePanel, SuperObjectWrapper object);
        }

        private final ObjectCreator creator;

        ObjectType(ObjectCreator creator) {
            this.creator = creator;
        }

        public static SuperObject create(GamePanel gamePanel, SuperObjectWrapper source) {
            return source.objectType.creator.create(gamePanel, source);
        }
    }

    public void removeWalkableTile() {
        this.gamePanel.tileManager.removeWalkableTile(this.getRawX(), this.getRawY());
    }

    public void addWalkableTile() {
        this.gamePanel.tileManager.addWalkableTile(this.getRawX(), this.getRawY());
    }

    protected SuperSpell generateRandomSpell() {
        List<SpellType> spellList = Arrays.asList(SpellType.values());
        int index = Utils.generateRandomInt(0, spellList.size() - 1);
        SpellType spellType = spellList.get(index);
        switch (spellType) {
            case SpellType.HEALTH_SPELL:
                return new HealthSpell();
            case SpellType.KEY_SPELL:
                return new KeySpell();
            case SpellType.CLARITY_SPELL:
                return new ClaritySpell();
            case SpellType.INVINCIBILITY_SPELL:
                return new InvincibilitySpell();
            case SpellType.SPEED_SPELL:
            default:
                return new SpeedSpell();
        }
    }
}
