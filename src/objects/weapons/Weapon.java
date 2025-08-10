package objects.weapons;

import java.awt.Graphics2D;

import entity.Player;
import main.GamePanel;
import main.InventoryItem;
import objects.projectiles.Projectile.Projectile_Type;

public class Weapon {

    int speed = 10;
    long lastShot = 0;
    GamePanel gamePanel;
    Player player;
    
    public int hold;
    public Weapon_Type weaponType;
    public Projectile_Type projectileType;
    public String sound;
    public InventoryItem inventoryItem;
    public int ammo;
    public boolean range = false;
    public boolean longSprite = false;
    public int maxDamage;

    public enum Weapon_Type {
        CROSSBOW,
        BLASTER,
        FIST,
        SWORD
    }

    public Weapon(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.player = gamePanel.player;
    }

    public void shoot() {
        if (this.sound != null) {
            this.gamePanel.playSoundEffect(this.sound);
        }
    }

    public void removeAmmo() {
        if (this.projectileType != null) {
            this.gamePanel.player.removeInventoryItem(this.projectileType.name());
        }
    }

    public void playSound() {
        this.gamePanel.playSoundEffect(this.sound);
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        // For subclass
    }

    public int getAmmoCount() {
        if (!this.gamePanel.player.inventory.containsKey(this.projectileType.name())) {
            return 0;
        }
        return this.gamePanel.player.getInventoryItem(this.projectileType.name());
    }

    public void select() {
        this.gamePanel.player.switchWeapon(this.weaponType);
    }
}
