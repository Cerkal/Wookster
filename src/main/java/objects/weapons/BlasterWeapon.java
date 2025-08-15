package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import objects.projectiles.LaserProjectile;
import objects.projectiles.Projectile.ProjectileType;

public class BlasterWeapon extends Weapon {

    // In milliseconds
    static final int BLASTER_DELAY = 250;
    static final int MAX_AMMO = 150;
    static final int BLASTER_SPEED = 20;
    static final int INITALIZED_LASERS = 50;

    public BlasterWeapon(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public void shoot() {
        this.ammo = this.getAmmoCount();
        if (ammo <= 0) { return; }
        if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
            this.gamePanel.player.attacking = true;
            shootLaser();
        } else {
            this.gamePanel.player.attacking = false;
        }
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name() + ": " + Integer.toString(this.ammo), x, y - 10);
    }

    private void init() {
        this.weaponType = WeaponType.BLASTER;
        this.projectileType = ProjectileType.LASERS;
        this.sound = Constants.SOUND_LASER;
        this.ammo = INITALIZED_LASERS;
        this.maxDamage = LaserProjectile.DAMAGE;
        this.range = true;
        if (this.gamePanel.player != null) {
            this.gamePanel.player.addInventoryItem(new InventoryItem(this, 1, true));
            if (this.gamePanel.player.getInventoryItem(this.projectileType.name()) == 0) {
                this.gamePanel.player.addInventoryItem(new InventoryItem(
                    this.projectileType.name(), INITALIZED_LASERS, false, false)
                );
            }
        }
    }

    private void shootLaser() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > BLASTER_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.gamePanel.projectileManager.projectiles.add(new LaserProjectile(this.gamePanel, BLASTER_SPEED));
        }
    }
}
