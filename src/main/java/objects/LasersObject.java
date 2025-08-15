package objects;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;

public class LasersObject extends SuperObject {

    static final int MIN_RANDOM_LASER_AMOUNT = 10;
    static final int MAX_RANDOM_LASER_AMOUNT = 50;

    public LasersObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public LasersObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.inventoryItem);
    }

    private void init() {
        this.objectType = ObjectType.LASERS;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_AMMO_LASERS_IMAGE);
        this.collision = true;
        this.inventoryItem = new InventoryItem(
            this.name,
            Utils.generateRandomInt(MIN_RANDOM_LASER_AMOUNT, MAX_RANDOM_LASER_AMOUNT), 
            false,
            false
        );
    }
}
