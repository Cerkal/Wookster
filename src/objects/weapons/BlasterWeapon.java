package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.Utils;
import objects.projectiles.LaserProjectile;
import objects.projectiles.Projectile.Projectile_Type;

public class BlasterWeapon extends Weapon {

    // In milliseconds
    static final int BLASTER_DELAY = 250;
    static final int BLASTER_SPEED = 20;
    static final int BLASTER_WEIGHT = 5;
    static final int BLASTER_PRICE = 30;
    static final int BLASTER_MAX_AMMO = 50;

    int ammo = 10;

    public BlasterWeapon(GamePanel gamePanel) {
        super(gamePanel);
        this.weaponType = Weapon_Type.BLASTER;
        this.projectileType = Projectile_Type.LASER;
        this.sound = Constants.SOUND_LASER;

        // Carriable
        this.inventoryName = Utils.capitalizeString(this.weaponType.name());
        this.weight = BLASTER_WEIGHT;
        this.count = 1;
        this.price = BLASTER_PRICE;
        this.condition = 100;
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
        graphics2D.drawString(Integer.toString(this.ammo), x, y - 10);
    }

    private void shootLaser() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > BLASTER_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.gamePanel.projectiles.add(new LaserProjectile(this.gamePanel, BLASTER_SPEED));
        }
    }
}
