package objects.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.Player;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import objects.projectiles.Projectile;
import objects.projectiles.Projectile.ProjectileType;

public abstract class Weapon {

    public static class InventoryWeaponWrapper {
        public WeaponType weaponType;
        public ProjectileType projectileType;
    }

    GamePanel gamePanel;
    Entity entity;
    
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
    public int initilizedAmmo = 10;
    public long lastShot = 0;
    public int speed = 10;

    public HashMap<WeaponType, String> iconImages = Constants.WEAPON_ICONS;

    public Weapon(GamePanel gamePanel, Entity entity) {
        this.gamePanel = gamePanel;
        this.entity = entity;
    }

    public void shoot() {
        if (this.sound != null) {
            this.gamePanel.playSoundEffect(this.sound);
        }
    }

    public void shoot(Entity entity) {
        shoot();
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
            this.icon = Utils.scaleImage(
                this.icon,
                Constants.INVENTORY_ICON_SIZE,
                Constants.INVENTORY_ICON_SIZE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum WeaponType {
        CROSSBOW((gamePanel, entity) -> new CrossbowWeapon(gamePanel, entity)),
        BLASTER((gamePanel, entity) -> new BlasterWeapon(gamePanel, entity)),
        FIST((gamePanel, entity) -> new FistWeapon(gamePanel, entity)),
        SWORD((gamePanel, entity) -> new SwordWeapon(gamePanel, entity));

        @FunctionalInterface
        public static interface ObjectCreator {
            Weapon create(GamePanel gamePanel, Entity entity);
        }

        private final ObjectCreator creator;

        WeaponType(ObjectCreator creator) {
            this.creator = creator;
        }

        public static Weapon create(GamePanel gamePanel, WeaponType weaponType, Entity entity) {
            return weaponType.creator.create(gamePanel, entity);
        }
    }

    public abstract Projectile getProjectile(Entity entity);

    protected void addToInventory() {
        if (this.entity instanceof Player && this.gamePanel.player != null) {
            this.gamePanel.player.addInventoryItem(new InventoryItem(this, 1, true));
            if (this.projectileType == null) { return; }
            if (this.gamePanel.player.getInventoryItem(this.projectileType.name()) == 0) {
                this.gamePanel.player.addInventoryItem(new InventoryItem(
                    this.projectileType.name(), this.initilizedAmmo, false, false)
                );
            }
        }
    }
}
