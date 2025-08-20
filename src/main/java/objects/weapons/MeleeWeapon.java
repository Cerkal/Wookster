package objects.weapons;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.projectiles.MeleeProjectile;
import objects.projectiles.Projectile;

public abstract class MeleeWeapon extends Weapon {

    public boolean isAttacking;

    static final int DEFAULT_DELAY = 250;
    static final int DEFAULT_HOLD_COUNT_MIN = 10;
    static final int DEFAULT_HOLD_COUNT_MAX = 30;
    static final int DEFAULT_SPEED_MODIFIER = 3;

    MeleeProjectile projectile;

    public MeleeWeapon(GamePanel gamePanel) {
        super(gamePanel);
    }

    public abstract void shoot();

    @Override
    public Projectile getProjectile(Entity entity) {
        throw new UnsupportedOperationException("Unimplemented method 'getProjectile'");
    }

    public void attack(MeleeProjectile projectile) {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > DEFAULT_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.isAttacking = true;
            this.projectile = projectile;
            this.gamePanel.projectileManager.add(this.projectile);
        }
    }

    public void playAttack() {
        if (this.isAttacking) {
            Long time = (this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND;
            try {
                if (time > DEFAULT_DELAY/2) {
                    this.gamePanel.player.attacking = false;
                    this.isAttacking = false;
                    this.gamePanel.projectileManager.toRemove.add(this.projectile);
                } else {
                    this.gamePanel.player.attacking = true;
                    MeleeProjectile projectile = (MeleeProjectile) this.gamePanel.projectileManager.projectiles.get(0);
                    projectile.setPosition();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    protected int getSpeed() {
        if (this.hold > DEFAULT_HOLD_COUNT_MAX) {
            return DEFAULT_HOLD_COUNT_MAX / DEFAULT_SPEED_MODIFIER;
        }
        if (this.hold < DEFAULT_HOLD_COUNT_MIN) {
            return DEFAULT_HOLD_COUNT_MIN / DEFAULT_HOLD_COUNT_MAX;
        }
        return this.hold / DEFAULT_SPEED_MODIFIER;
    }
}
