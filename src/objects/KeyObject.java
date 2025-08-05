package objects;

import main.Constants;
import main.GamePanel;

public class KeyObject extends SuperObject {

    public final String NAME = Constants.OBJECT_KEY;

    public KeyObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public KeyObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.NAME);
    }

    private void init() {
        this.name = NAME;
        this.setImage(Constants.OBJECT_KEY_IMAGE);
        this.soundPrimary = Constants.SOUND_COIN;
    }
}
