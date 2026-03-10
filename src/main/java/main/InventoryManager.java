package main;

import java.util.*;
import entity.Player;
import main.InventoryItem.InventoryRecord;
import main.InventoryItem.InventoryRecord.WeaponRecord;
import main.GamePanel.GameState;
import objects.SuperObject;
import objects.SuperObject.ObjectType;
import objects.SuperObject.SuperObjectWrapper;
import objects.weapons.Weapon.WeaponType;
import spells.SuperSpell;
import spells.SuperSpell.SpellType;

public class InventoryManager {

    private final HashMap<String, InventoryItem> items = new HashMap<>();
    private final GamePanel gamePanel;
    private final boolean notifyOnAdd;

    public InventoryManager(GamePanel gamePanel, boolean notifyOnAdd) {
        this.gamePanel = gamePanel;
        this.notifyOnAdd = notifyOnAdd;
    }

    // --- Core Mutation ---

    public void add(InventoryItem item) {
        if (item == null) return;
        InventoryItem existing = items.get(item.name);
        if (existing != null && existing.canStackWith(item)) {
            existing.count += item.count;
        } else {
            items.put(item.name, item);
        }
        if (notifyOnAdd) displayMessage(item);
    }

    public void remove(String name) {
        remove(name, 1);
    }

    public void remove(String name, int count) {
        InventoryItem item = items.get(name);
        if (item == null) return;
        item.count -= count;
        if (item.count <= 0) items.remove(name);
    }

    public boolean transfer(String name, int count, InventoryManager target) {
        InventoryItem item = items.get(name);
        if (item == null || item.count < count) return false;
        InventoryItem single = item.copy();
        single.count = count;
        remove(name, count);
        target.add(single);
        return true;
    }

    // --- Query ---

    public InventoryItem get(String name) {
        return items.get(name);
    }

    public int getCount(String name) {
        InventoryItem item = items.get(name);
        return item != null ? item.count : 0;
    }

    public boolean has(String name) {
        return items.containsKey(name);
    }

    public boolean has(String name, int count) {
        return getCount(name) >= count;
    }

    public Collection<InventoryItem> getAll() {
        return Collections.unmodifiableCollection(items.values());
    }

    // --- Filtered Views ---

    public List<InventoryItem> getDisplayItems() {
        List<InventoryItem> weapons = new ArrayList<>();
        List<InventoryItem> others = new ArrayList<>();
        for (InventoryItem item : items.values()) {
            if (item.usable || item.visibility) {
                InventoryItem copy = item.copy();
                (copy.weapon != null ? weapons : others).add(copy);
            }
        }
        weapons.sort(Comparator.comparing(w -> w.name));
        others.sort(Comparator.comparing(o -> o.name));
        List<InventoryItem> result = new ArrayList<>(weapons);
        result.addAll(others);
        return result;
    }

    public List<InventoryItem> getSellableItems() {
        List<InventoryItem> weapons = new ArrayList<>();
        List<InventoryItem> others = new ArrayList<>();
        for (InventoryItem item : items.values()) {
            if (item.sellable) {
                InventoryItem copy = item.copy();
                (copy.weapon != null ? weapons : others).add(copy);
            }
        }
        weapons.sort(Comparator.comparing(w -> w.name));
        others.sort(Comparator.comparing(o -> o.name));
        List<InventoryItem> result = new ArrayList<>(weapons);
        result.addAll(others);
        return result;
    }

    // --- Credits ---

    public void addCredits(int amount) {
        add(new InventoryItem(Constants.CREDITS, amount, false, true, false, 1));
    }

    public void removeCredits(int amount) {
        remove(Constants.CREDITS, amount);
    }

    public int getCredits() {
        return getCount(Constants.CREDITS);
    }

    public boolean canAfford(int price) {
        return getCredits() >= price;
    }

    // --- Vendor / Container Setup ---

    public void setStock(List<InventoryItem> stock) {
        items.clear();
        for (InventoryItem item : stock) {
            items.put(item.name, item);
        }
    }

    // --- Serialization ---

    public HashMap<String, InventoryRecord> toRecords() {
        HashMap<String, InventoryRecord> records = new HashMap<>();
        for (String key : items.keySet()) {
            records.put(key, items.get(key).toRecord());
        }
        return records;
    }

    /**
     * Load from saved records. Returns weapon types that need to be added to the player
     * separately (caller is responsible for calling player.addWeapon for each).
     */
    public List<WeaponType> loadFromRecords(HashMap<String, InventoryRecord> records) {
        items.clear();
        List<WeaponType> weaponTypes = new ArrayList<>();
        if (records == null) return weaponTypes;

        for (InventoryRecord record : records.values()) {
            try {
                if (record.object != null) {
                    SuperObject.SuperObjectWrapper wrapper = record.object;
                    SuperObject object = ObjectType.create(gamePanel, wrapper);
                    if (object != null && object.inventoryItem != null) {
                        object.inventoryItem.count = record.count;
                        add(object.inventoryItem);
                    }
                } else if (record.weapon != null && record.weapon.weaponType != null) {
                    weaponTypes.add(record.weapon.weaponType);
                } else {
                    add(new InventoryItem(
                        record.itemName, record.count, record.usable,
                        record.visibility, record.sellable, record.price
                    ));
                }
            } catch (Exception e) {
                System.err.println("Skipping unrestorable inventory record '" + record.itemName + "': " + e.getMessage());
            }
        }
        return weaponTypes;
    }

    public void clear() {
        items.clear();
    }

    // --- Internal ---

    private void displayMessage(InventoryItem item) {
        if (gamePanel == null || gamePanel.ui == null) return;
        if (gamePanel.gameState == GameState.VENDOR) return;
        String msg = item.count > 1
            ? item.count + " " + item.name.toLowerCase() + Constants.MESSGE_INVENTORY_ADDED
            : item.name + Constants.MESSGE_INVENTORY_ADDED;
        gamePanel.ui.displayMessage(msg);
    }
}
