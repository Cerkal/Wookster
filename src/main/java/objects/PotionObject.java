package objects;

import main.Constants;
import main.GamePanel;
import spells.SuperSpell;

public class PotionObject extends SuperObject {

    public static final String COLOR = "green";

    public PotionObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
        this.spell = generateRandomSpell();
    }

    public PotionObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
        this.spell = generateRandomSpell();
    }

    public PotionObject(GamePanel gamePanel, SuperSpell spell) {
        super(gamePanel);
        init();
        this.spell = spell;
    }

    public PotionObject(GamePanel gamePanel, SuperSpell spell, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
        this.spell = spell;
    }

    private void init() {
        this.objectType = ObjectType.POTION;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_GREEN_POTION_IMAGE);
        this.soundPrimary = Constants.SOUND_LOCK;
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
    }

    @Override
    public void removeObject() {
        super.removeObject();
        setSpell();
    }
}
