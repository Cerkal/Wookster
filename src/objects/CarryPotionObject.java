package objects;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import spells.SuperSpell;

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
            switch (this.spell.spellType) {
                case HEALTH_SPELL:
                    return "HEALTH POTION";
                case SPEED_SPELL:
                    return "SPEED POTION";
                case CLARITY_SPELL:
                    return "CLARITY POTION";
                default:
                    break;
            }
        }
        return this.objectType.name();
    }

    private void init() {
        this.objectType = Object_Type.POTION;
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
}
