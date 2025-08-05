package objects;

import main.Constants;
import main.GamePanel;
import spells.SuperSpell;

public class PotionObject extends SuperObject {

    public final String NAME = Constants.OBJECT_GREEN_POTION;

    public PotionObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public PotionObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public PotionObject(GamePanel gamePanel, int worldX, int worldY, SuperSpell spell) {
        super(gamePanel, worldX, worldY);
        init();
        this.spell = spell;
    }

    public PotionObject(GamePanel gamePanel, SuperSpell spell) {
        super(gamePanel);
        init();
        this.spell = spell;
    }

    private void init() {
        this.name = NAME;
        this.setImage(Constants.OBJECT_GREEN_POTION_IMAGE);
        this.soundPrimary = Constants.SOUND_LOCK;
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
    }
}
