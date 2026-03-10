package objects;

import java.util.List;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.InventoryManager;
import main.GamePanel.GameState;

public class ContainerObject extends SuperObject {

    public InventoryManager inventory;

    public boolean isLocked = false;

    public ContainerObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.inventory = new InventoryManager(gamePanel, false);
        init();
    }

    public void activateObject() {
        super.activateObject();
        if (this.gamePanel.keyHandler.spacePressed && !this.activated && !this.isLocked) {
            this.activated = true;
            this.gamePanel.gameState = GameState.VENDOR;
            this.setImage(Constants.OBJECT_CHEST_OPEN_IMAGE);
        }
        if (this.gamePanel.keyHandler.spacePressed && !this.activated && this.isLocked) {
            this.gamePanel.ui.displayMessage("It's locked.");
        }
    }

    public void close() {
        this.setImage(Constants.OBJECT_CHEST_IMAGE);
        this.activated = false;
    }

    private void init() {
        this.objectType = ObjectType.CHEST;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_CHEST_IMAGE);
        removeWalkableTile();
        this.collision = true;
    }

    public List<InventoryItem> getInventoryItems() {
        return this.inventory.getSellableItems();
    }

    public void addInventoryItemFromVendor(InventoryItem item) {
        this.inventory.add(item);
    }

    public boolean removeInventoryItemFromVendor(String name, int count) {
        if (!this.inventory.has(name, count)) return false;
        this.inventory.remove(name, count);
        return true;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventory.setStock(inventoryItems);
    }
}
