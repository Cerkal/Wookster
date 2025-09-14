package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.KeyHandler;
import main.Utils;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;

public class FoodObject extends SuperObject {

    public FoodObject(GamePanel gamePanel, SuperSpell spell, String name) {
        super(gamePanel);
        this.spell = spell;
        this.name = name;
        init();
    }

    public FoodObject(GamePanel gamePanel, SuperSpell spell, String name, int worldX, int worldY) {
        this(gamePanel, spell, name);
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
        init();
    }

    private void init() {
        this.objectType = ObjectType.FOOD;
        setFoodType();
        this.soundPrimary = Constants.SOUND_LOCK; // change
        if (this.spell.sellable) {
            this.sellable = this.spell.sellable;
            this.price = this.spell.price;
        }
        this.inventoryItem = new InventoryItem(this, 1, true);
    }

    private void setFoodType() {
        switch (this.name.toUpperCase()) {
            case "BERRIES":
                setIcon(Constants.OBJECT_FOOD_BERRIES_IMAGE);
                this.spell.message = Constants.MESSAGE_BERRIES_GOOD;
                break;
        }
    }

    protected void setIcon(String imagePath) {
        try {
            if (imagePath == null) { return; }
            this.inventoryIcon = ImageIO.read(getClass().getResourceAsStream(imagePath));
            int doubleTile = Constants.TILE_SIZE * 2;
            this.inventoryIcon = Utils.scaleImage(this.inventoryIcon, doubleTile, doubleTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.inventoryItem);
    }

    @Override
    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        super.drawDetails(graphics2D, x, y);
        if (this.spell != null) {
            boolean clarity = this.gamePanel.player.spells.containsKey(SpellType.CLARITY_SPELL);
            y = this.spell.drawDescription(graphics2D, x, y, clarity);
        }
        y += Constants.NEW_LINE_SIZE;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Press " + KeyHandler.R + " to drop.", x, y);
        graphics2D.setColor(Color.YELLOW);
        graphics2D.drawString(" ".repeat(6) + KeyHandler.R, x, y);
        
        y += Constants.NEW_LINE_SIZE;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Press " + KeyHandler.SPACEBAR + " to use.", x, y);
        graphics2D.setColor(Color.YELLOW);
        graphics2D.drawString(" ".repeat(6) + KeyHandler.SPACEBAR, x, y);
    }
}
