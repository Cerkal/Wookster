package main;

import java.awt.Graphics2D;
import java.util.Objects;

import objects.SuperObject;
import objects.SuperObject.SuperObjectWrapper;
import objects.projectiles.Projectile;
import objects.weapons.Weapon;
import objects.weapons.Weapon.InventoryWeaponWrapper;
import spells.SuperSpell;

public class InventoryItem {

    public static class InventoryItemWrapper {
        public String itemName;
        public int count;
        public SuperSpell spell;
        public SuperObjectWrapper object;
        public InventoryWeaponWrapper weapon;
        public boolean usable;
        public boolean visibility;
        public boolean sellable;
        public int price;
    }

    public Weapon weapon;
    public SuperObject object;
    public Projectile projectile;
    public String name;
    public int count;
    public boolean usable;
    public boolean visibility;
    public boolean sellable;
    public int price;

    public InventoryItem(String name, int count, boolean usable, boolean visibility, boolean sellable, int price) {
        this.name = name;
        this.count = count;
        this.usable = usable;
        this.visibility = visibility;
        this.sellable = sellable;
        this.price = price;
    }

    public InventoryItem(Weapon weapon, int count, boolean usable) {
        this.weapon = weapon;
        this.name = weapon.weaponType.name();
        this.count = count;
        this.usable = usable;
        this.sellable = weapon.sellable;
        this.price = weapon.price;
    }

    public InventoryItem(SuperObject object, int count, boolean usable) {
        this.object = object;
        this.count = count;
        this.usable = usable;
        this.name = object.name;
        this.sellable = object.sellable;
        this.price = object.price;
    }

    public InventoryItem(InventoryItem other) {
        this.name = other.name;
        this.count = other.count;
        this.usable = other.usable;
        this.visibility = other.visibility;
        this.sellable = other.sellable;
        this.price = other.price;
        this.object = other.object;
        this.weapon = other.weapon;
        this.projectile = other.projectile; 
    }

    public InventoryItem copy() {
        return new InventoryItem(this);
    }

    public boolean canStackWith(InventoryItem other) {
        if (other == null) return false;
        return this.name.equals(other.name)
            && this.sellable == other.sellable
            && this.usable == other.usable
            && (this.weapon == null ? other.weapon == null : this.weapon.equals(other.weapon));
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

    public void remove() {
        if (this.object != null) {
            this.object.removeInventoryItem();
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
        inventoryItemWrapper.usable = this.usable;
        inventoryItemWrapper.visibility = this.visibility;
        inventoryItemWrapper.sellable = this.sellable;
        inventoryItemWrapper.price = this.price;
        if (this.object != null) {
            inventoryItemWrapper.object = this.object.getSuperObjectWrapper();
        }
        if (this.weapon != null) {
            inventoryItemWrapper.weapon = this.weapon.getInventoryWeaponWrapper();
        }
        return inventoryItemWrapper;
    }
}
