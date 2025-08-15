package main;

import java.awt.Graphics2D;

import objects.SuperObject;
import objects.projectiles.Projectile;
import objects.weapons.Weapon;
import spells.SuperSpell;

public class InventoryItem {

    public static class InventoryItemWrapper {
        public String itemName;
        public int count;
        public SuperSpell spell;
    }

    public Weapon weapon;
    public SuperObject object;
    public Projectile projectile;
    public String name;
    public int count;
    public boolean usable;
    public boolean visibility;

    public InventoryItem(String name, int count, boolean usable, boolean visibility) {
        this.name = name;
        this.count = count;
        this.usable = usable;
        this.visibility = visibility;
    }

    public InventoryItem(Weapon weapon, int count, boolean usable) {
        this.weapon = weapon;
        this.name = weapon.weaponType.name();
        this.count = count;
        this.usable = usable;
    }

    public InventoryItem(SuperObject object, int count, boolean usable) {
        this.object = object;
        this.count = count;
        this.usable = usable;
        this.name = object.name;
    }

    public InventoryItem(InventoryItem other) {
        this.weapon = other.weapon;
        this.object = other.object;
        this.projectile = other.projectile;
        this.name = other.name;
        this.count = other.count;
        this.usable = other.usable;
        this.visibility = other.visibility;
    }

    public InventoryItem copy() {
        return new InventoryItem(this);
    }

    public void updateCount(int count) {
        this.count = count;
    }

    public void select() {
        if (this.weapon != null) {
            this.weapon.select();
        }
        if (this.object != null) {
            this.object.useObject();
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.count);
    }

    public void drawInfo(Graphics2D graphics2D, int x, int y) {
        if (this.weapon != null) {
            this.weapon.drawDetails(graphics2D, x, y);
        }
        if (this.object != null) {
            this.object.drawDetails(graphics2D, x, y);
        }
    }

    public InventoryItemWrapper getInventoryWrapper() {
        InventoryItemWrapper inventoryItemWrapper = new InventoryItemWrapper();
        inventoryItemWrapper.count = this.count;
        inventoryItemWrapper.itemName = this.name;
        if (this.object != null) {
            inventoryItemWrapper.spell = this.object.spell;
        }
        return inventoryItemWrapper;
    }
}
