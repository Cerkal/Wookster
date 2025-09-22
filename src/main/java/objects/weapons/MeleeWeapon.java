package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.projectiles.MeleeProjectile;

public abstract class MeleeWeapon extends Weapon {

    protected int holdCountMin = 10;
    protected int holdCountMax = 30;
    protected double speedModifier = 3;

    protected MeleeProjectile projectile;

    public MeleeWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.range = false;
        this.ammo = 0;
        this.delay = 500;
    }

    public void shoot() {
        if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
            this.hold++;
        } else {
            if (this.hold > 0) {
                attack();
            }
            this.hold = 0;
        }
        playAttack();
        if (this.projectile != null) {
            this.projectile.setPosition();
        }
    }

    public void shoot(Entity entity) {
        attack();
        playAttack();
        if (this.projectile != null) {
            this.projectile.setPosition();
        }
    }

    public abstract MeleeProjectile getProjectile(Entity entity);

    public void attack() {
        if (canShoot()) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.entity.attacking = true;
            this.projectile = getProjectile(this.entity);
            this.gamePanel.projectileManager.add(this.projectile);
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

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name(), x, y - 10);
    }
}
