package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.GamePanel.GameState;

public class ContainerObject extends SuperObject {

    List<InventoryItem> inventoryItems = new ArrayList<>();
    public HashMap<String, List<InventoryItem>> inventory = new HashMap<>();

    public ContainerObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        if (this.gamePanel.keyHandler.spacePressed && !this.activated) {
            this.activated = true;
            this.gamePanel.gameState = GameState.VENDOR;
            this.setImage(Constants.OBJECT_CHEST_OPEN_IMAGE);
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
        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);

            int totalCount = items.stream()
                .mapToInt(item -> item.count)
                .sum();

            if (totalCount > 0) {
                InventoryItem first = items.get(0);
                InventoryItem item = new InventoryItem(first);
                item.count = totalCount;

                inventoryItems.add(item);
            }
        }
        return inventoryItems;
    }

    public void addInventoryItemFromVendor(InventoryItem item) {
        if (item == null || item.count <= 0) return;
        List<InventoryItem> itemList = inventory.computeIfAbsent(item.name, k -> new ArrayList<>());
        for (InventoryItem existing : itemList) {
            if (existing.canStackWith(item)) {
                existing.count += item.count;
                return;
            }
        }
        InventoryItem copy = new InventoryItem(item);
        copy.count = item.count;
        itemList.add(copy);
    }

    public boolean removeInventoryItemFromVendor(String name, int count) {
        List<InventoryItem> itemList = inventory.get(name);
        if (itemList == null) return false;
        int remaining = count;
        for (Iterator<InventoryItem> it = itemList.iterator(); it.hasNext() && remaining > 0;) {
            InventoryItem current = it.next();
            if (current.count > remaining) {
                current.count -= remaining;
                remaining = 0;
            } else {
                remaining -= current.count;
                it.remove();
            }
        }
        if (itemList.isEmpty()) {
            inventory.remove(name);
        }
        return remaining <= 0;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        for (InventoryItem item : inventoryItems) {
            this.inventory.put(item.name, new ArrayList<>(List.of(item)));
        }
    }
}
