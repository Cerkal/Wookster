package objects;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;

public class ArrowsObject extends SuperObject {

    static final int MIN_RANDOM_ARROW_AMOUNT = 10;
    static final int MAX_RANDOM_ARROW_AMOUNT = 20;

    public ArrowsObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public ArrowsObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.inventoryItem);
    }

    private void init() {
        this.objectType = ObjectType.ARROWS;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_AMMO_ARROWS_IMAGE);
        this.collision = true;
        this.inventoryItem = new InventoryItem(
            this.name,
            Utils.generateRandomInt(MIN_RANDOM_ARROW_AMOUNT, MAX_RANDOM_ARROW_AMOUNT),
            false,
            false
        );
    }
}
