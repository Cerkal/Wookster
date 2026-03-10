package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.projectiles.GrenadeProjectile;

public abstract class ThrowWeapon extends Weapon {

    protected int holdCountMin = 20;
    protected int holdCountMax = 40;
    protected double speedModifier = 3;

    protected GrenadeProjectile projectile;

    public ThrowWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.range = false;
        this.ammo = 0;
        this.delay = 1000;
        this.initilizedAmmo = 1;
    }

    public void shoot() {
        if (
            this.gamePanel.keyHandler.enterPressed ||
            this.gamePanel.keyHandler.spacePressed || 
            this.gamePanel.mouseHandler.holding 
        ){
            this.hold++;
        } else {
            if (this.hold > 0) {
                attack();
            }
            this.hold = 0;
        }
        playAttack();
    }

    public void shoot(Entity entity) {
        attack();
        playAttack();
    }

    public abstract GrenadeProjectile getProjectile(Entity entity);

    public void attack() {
        if (canShoot() && getAmmoCount() > 0) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.entity.attacking = true;
            this.projectile = getProjectile(this.entity);
            this.gamePanel.projectileManager.add(this.projectile);
            if (getAmmoCount() == 0) {
                this.gamePanel.player.removeWeapon(this.weaponType);
            }
        }
    }

    public void playAttack() {
        if (this.entity.attacking) {
            Long time = (this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND;
            try {
                if (time > this.projectile.dispose) {
                    this.entity.attacking = false;
                } else {
                    this.entity.attacking = true;
                }
            } catch (Exception e) {
                //
            }
        }
    }

    protected int getSpeed() {
        if (this.hold > this.holdCountMax) {
            return (int) (this.holdCountMax / this.speedModifier);
        }
        if (this.hold < this.holdCountMin) {
            return this.holdCountMin / this.holdCountMax;
        }
        return (int) (this.hold / this.speedModifier);
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        int width = Constants.TILE_SIZE * 2;
        int height = Constants.TILE_SIZE / 4;

        int currentHold = this.hold;

        float holdPercent = Math.max(0, Math.min(1f, (float) currentHold / this.holdCountMax));
        int holdBarWidth = (int) (width * holdPercent);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.PINK);
        if (currentHold < this.holdCountMin) {
            graphics2D.setColor(Color.LIGHT_GRAY);
        }
        graphics2D.fillRect(x, y, holdBarWidth, height);

        this.ammo = getAmmoCount();
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name() + ": " + Integer.toString(this.ammo), x, y - 10);
    }
}
