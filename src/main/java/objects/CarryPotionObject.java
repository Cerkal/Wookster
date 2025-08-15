package objects;

import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;

public class CarryPotionObject extends SuperObject {

    public CarryPotionObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public CarryPotionObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public CarryPotionObject(GamePanel gamePanel, SuperSpell spell) {
        super(gamePanel);
        this.spell = spell;
        init();
    }

    public CarryPotionObject(GamePanel gamePanel, SuperSpell spell, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.spell = spell;
        init();
    }

    public String getPotionType() {
        if (this.spell != null) {
            try {
                return ObjectType.POTION.name() + " " + this.spell.spellType.name().split("_")[0];
            } catch (Exception e) {
                System.err.println("Potion name error splitting _");
            }
        }
        return this.objectType.name();
    }

    private void init() {
        this.objectType = ObjectType.POTION;
        this.name = getPotionType();
        this.setImage(Constants.OBJECT_PURPLE_POTION_IMAGE);
        this.soundPrimary = Constants.SOUND_LOCK;
        this.inventoryItem = new InventoryItem(this, 1, true);
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
            this.spell.drawDescription(graphics2D, x, y, clarity);
        }
    }
}
