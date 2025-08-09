package objects;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;

public class KeyObject extends SuperObject {

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
        this.gamePanel.player.addInventoryItem(this.inventoryItem);
    }

    private void init() {
        this.objectType = Object_Type.KEY;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_KEY_IMAGE);
        this.soundPrimary = Constants.SOUND_COIN;
        this.inventoryItem = new InventoryItem(this.name, 1, false, true);
    }
}
