package objects.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import entity.Player;
import main.Constants;
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

    public HashMap<Weapon_Type, String> iconImages = Constants.WEAPON_ICONS;

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
        return this.gamePanel.player.getInventoryItem(this.projectileType.name());
    }

    public void select() {
        this.gamePanel.player.switchWeapon(this.weaponType);
    }

    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        try {
            BufferedImage icon = ImageIO.read(getClass().getResourceAsStream(this.iconImages.get(this.weaponType)));
            this.gamePanel.ui.drawInventoryIcon(graphics2D, x, y, icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        graphics2D.drawString(this.weaponType.name(), x, y);
        if (this.range) {
            y += Constants.NEW_LINE_SIZE;
            graphics2D.drawString("Ammo: " + String.valueOf(this.ammo) + " " + this.projectileType.name(), x, y);
        }
        y += Constants.NEW_LINE_SIZE;
        graphics2D.drawString("Max Damage: " + String.valueOf(this.maxDamage), x, y);
    }

}
