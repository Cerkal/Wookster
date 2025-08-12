package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Player;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import spells.HealthSpell;
import spells.SpeedSpell;
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

    @Override
    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        super.drawDetails(graphics2D, x, y);
        if (this.spell != null) {
            if (this.spell.spellTime > 0) {
                y += Constants.NEW_LINE_SIZE;
                graphics2D.drawString("Spell Time: " + String.valueOf(this.spell.spellTime) + "s", x, y);
            }
            for (String description : this.spell.descriptionText) {
                y += Constants.NEW_LINE_SIZE;
                graphics2D.drawString(description, x, y);
            }

            if (this.gamePanel.player.spells.containsKey(SpellType.CLARITY_SPELL)) {
                graphics2D.setColor(Color.YELLOW);
                String description = "Increases";
                if (!this.spell.positiveSpell) {
                    description = "Decreases";
                }
                if (this.spell.spellType == SpellType.SPEED_SPELL) {
                    SpeedSpell speedSpell = (SpeedSpell) this.spell;
                    y += Constants.NEW_LINE_SIZE;
                    int speedDiff = Math.abs(Player.DEFAULT_SPEED - speedSpell.speed);
                    graphics2D.drawString(description + " player's speed by " + String.valueOf(speedDiff), x, y);
                }
                if (this.spell.spellType == SpellType.HEALTH_SPELL) {
                    HealthSpell speedSpell = (HealthSpell) this.spell;
                    y += Constants.NEW_LINE_SIZE;
                    graphics2D.drawString(description + " player's health by " + String.valueOf(speedSpell.healthAmount), x, y);
                }
                graphics2D.setColor(Color.WHITE);
            }
        }
    }
}
