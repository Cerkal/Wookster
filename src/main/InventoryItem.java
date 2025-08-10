package main;

import java.awt.Graphics2D;

import objects.SuperObject;
import objects.projectiles.Projectile;
import objects.weapons.Weapon;

public class InventoryItem {

    public Weapon weapon;
    public SuperObject object;
    public Projectile projectile;
    public String name;
    public int count;
    public boolean usable;
    public boolean visibility;
    public boolean unique = false;

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
        this.unique = true;
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

    public void drawInfo(Graphics2D graphics2D) {
        if (this.weapon != null) {
            this.weapon.drawDetails(graphics2D, 400, 300);
        }
        if (this.object != null) {
            this.object.drawDetails(graphics2D, 400, 300);
        }
    }
}
