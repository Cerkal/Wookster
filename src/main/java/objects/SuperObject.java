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

    public enum ObjectType {
        ARROWS((gp, obj) -> new ArrowsObject(gp, obj.worldX, obj.worldY)),
        CHEST((gp, obj) -> new ChestObject(gp, obj.worldX, obj.worldY)),
        DOOR((gp, obj) -> new DoorObject(gp, obj.worldX, obj.worldY)),
        KEY((gp, obj) -> new KeyObject(gp, obj.worldX, obj.worldY)),
        LASERS((gp, obj) -> new LasersObject(gp, obj.worldX, obj.worldY)),
        SIGN((gp, obj) -> new SignObject(gp, obj.worldX, obj.worldY, obj.message)),
        BLASER((gp, obj) -> new BlasterObject(gp, obj.worldX, obj.worldY)),
        CROSSBOW((gp, obj) -> new CrossbowObject(gp, obj.worldX, obj.worldY)),
        SWORD((gp, obj) -> new SwordObject(gp, obj.worldX, obj.worldY)),
        JERMEY((gp, obj) -> new JermeyObject(gp, obj.worldX, obj.worldY, null)),
        MAP((gp, obj) -> new GameMap(gp)),
        POTION((gp, obj) -> obj.carriable
            ? new CarryPotionObject(gp, obj.spell, obj.worldX, obj.worldY)
            : new PotionObject(gp, obj.spell, obj.worldX, obj.worldY));

        @FunctionalInterface
        public interface ObjectCreator {
            SuperObject create(GamePanel gp, SuperObjectWrapper obj);
        }

        private final ObjectCreator creator;

        ObjectType(ObjectCreator creator) {
            this.creator = creator;
        }

        public SuperObject create(GamePanel gp, SuperObjectWrapper source) {
            return creator.create(gp, source);
        }
    }


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
        return objectWrapper;
    }

    public int getRawX() {
        return this.worldX / Constants.TILE_SIZE;
    }

    public int getRawY() {
        return this.worldY / Constants.TILE_SIZE;
    }
}
