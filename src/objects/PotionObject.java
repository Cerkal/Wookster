package objects;

import main.Constants;
import main.GamePanel;
import spells.SuperSpell;

public class PotionObject extends SuperObject {

    public final String NAME = Constants.OBJECT_GREEN_POTION;

    public PotionObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.name = NAME;
        this.setImage(Constants.OBJECT_GREEN_POTION_IMAGE);
        this.soundPrimary = Constants.SOUND_LOCK;
    }

    public PotionObject(GamePanel gamePanel, int worldx, int worldY, SuperSpell spell) {
        this(gamePanel, worldx, worldY);
        this.spell = spell;
    }

    public void activateObject() {
        this.removeObject();
    }
}
