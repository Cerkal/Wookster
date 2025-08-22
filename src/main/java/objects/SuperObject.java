package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
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

    public HashMap<ObjectType, String> objectIcons = Constants.OBJECT_ICONS;

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
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
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

    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        try {
            BufferedImage icon = ImageIO.read(getClass().getResourceAsStream(this.objectIcons.get(this.objectType)));
            this.gamePanel.ui.drawInventoryIcon(graphics2D, x, y, icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphics2D.drawString(this.name, x, y);
    }

    protected void setImage(String imagePath) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
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
}
