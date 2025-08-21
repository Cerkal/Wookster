package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.projectiles.LaserProjectile;
import objects.projectiles.Projectile;
import objects.projectiles.Projectile.ProjectileType;

public class BlasterWeapon extends Weapon {

    // In milliseconds
    static final int BLASTER_DELAY = 500;
    static final int MAX_AMMO = 150;
    public static final int BLASTER_SPEED = 20;
    static final int INITALIZED_LASERS = 50;

    public BlasterWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.weaponType = WeaponType.BLASTER;
        this.projectileType = ProjectileType.LASERS;
        this.sound = Constants.SOUND_LASER;
        this.initilizedAmmo = INITALIZED_LASERS;
        this.maxDamage = LaserProjectile.DAMAGE;
        this.range = true;
        addToInventory();
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

    public void shoot(Entity entity) {
        shootLaser();
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name() + ": " + Integer.toString(this.ammo), x, y - 10);
    }

    public Projectile getProjectile(Entity entity) {
        return new LaserProjectile(this.gamePanel, entity);
    }

    private void shootLaser() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > BLASTER_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.gamePanel.projectileManager.add(getProjectile(this.entity));
        }
    }
}
