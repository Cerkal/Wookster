package objects.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import entity.Player;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import objects.projectiles.Projectile.ProjectileType;

public class Weapon {

    public static class InventoryWeaponWrapper {
        public WeaponType weaponType;
        public ProjectileType projectileType;
    }

    int speed = 10;
    long lastShot = 0;
    GamePanel gamePanel;
    Player player;
    
    public int hold;
    public WeaponType weaponType;
    public ProjectileType projectileType;
    public String sound;
    public InventoryItem inventoryItem;
    public int ammo;
    public boolean range = false;
    public boolean longSprite = false;
    public int maxDamage;
    public BufferedImage icon;

    public HashMap<WeaponType, String> iconImages = Constants.WEAPON_ICONS;

    public enum WeaponType {
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
        return this.gamePanel.player.getInventoryItem(this.projectileType.name());
    }

    public void select() {
        this.gamePanel.player.switchWeapon(this.weaponType);
    }

    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        if (this.icon == null) {
            setWeaponIcon();
        }
        this.gamePanel.ui.drawInventoryIcon(graphics2D, x, y, this.icon);
        graphics2D.drawString(this.weaponType.name(), x, y);
        if (this.range) {
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString("Ammo: " + String.valueOf(this.ammo) + " " + this.projectileType.name(), x, y);
        }
        y += Constants.NEW_LINE_SIZE;
        graphics2D.drawString("Max Damage: " + String.valueOf(this.maxDamage), x, y);
    }

    public InventoryWeaponWrapper getInventoryWeaponWrapper() {
        InventoryWeaponWrapper inventoryWeaponWrapper = new InventoryWeaponWrapper();
        inventoryWeaponWrapper.projectileType = this.projectileType;
        inventoryWeaponWrapper.weaponType = this.weaponType;
        return inventoryWeaponWrapper;
    }

    protected void setWeaponIcon() {
        try {
            this.icon = ImageIO.read(getClass().getResourceAsStream(this.iconImages.get(this.weaponType)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
