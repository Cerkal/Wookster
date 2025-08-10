package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import spells.SpeedSpell;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;
import tile.TileManager.TileLocation;

public class SuperObject {

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
    public Object_Type objectType;

    public enum Object_Type {
        ARROWS,
        CHEST,
        DOOR,
        KEY,
        LASERS,
        POTION,
        SIGN
    }

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
        this.collision = false;
        this.visibility = false;
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
        graphics2D.drawString(this.objectType.name(), x, y);
        if (this.spell != null) {
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString(this.name, x, y);
            if (this.spell.spellTime > 0) {
                y += Constants.NEW_LINE_SIZE;
                graphics2D.drawString("Spell Time: " + String.valueOf(this.spell.spellTime) + "s", x, y);
            }
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString(this.spell.description, x, y);
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString(String.valueOf(this.spell.positiveSpell), x, y);

            if (this.spell.spellType == SpellType.SPEED_SPELL) {
                SpeedSpell speedSpell = (SpeedSpell) this.spell;
                y += Constants.NEW_LINE_SIZE;
                graphics2D.drawString(String.valueOf(speedSpell.speed), x, y);
            }
            
        }
    }

    protected void setImage(String imagePath) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setSpell() {
        if (this.spell != null) {
            if (this.spell.spellTime > 0) {
                this.spell.startTime = this.gamePanel.gameTime;
            }
            this.gamePanel.player.spells.put(spell.spellType, this.spell);
            if (this.spell.message != null) {
                this.gamePanel.ui.displayDialog(this.spell.message);
            }
        }
    }
}
