package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import objects.projectiles.MeleeProjectile;
import objects.projectiles.SwordProjectile;

public class SwordWeapon extends Weapon {

    // In milliseconds
    static final int FIST_DELAY = 250;

    static final int HOLD_COUNT_MIN = 20;
    static final int HOLD_COUNT_MAX = 50;
    static final int SPEED_MODIFIER = 3;

    boolean isAttacking;
    SwordProjectile swing;

    public SwordWeapon(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public void shoot() {
        if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
            this.hold++;
        } else {
            if (this.hold > 0) {
                swing();
            }
            this.hold = 0;
        }
        playSwing();
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        int width = Constants.TILE_SIZE * 2;
        int height = Constants.TILE_SIZE / 4;

        int currentHold = this.hold;

        float holdPercent = Math.max(0, Math.min(1f, (float) currentHold / HOLD_COUNT_MAX));
        int holdBarWidth = (int) (width * holdPercent);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.PINK);
        if (currentHold < HOLD_COUNT_MIN) {
            graphics2D.setColor(Color.LIGHT_GRAY);
        }
        graphics2D.fillRect(x, y, holdBarWidth, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name(), x, y - 10);
    }

    private void init() {
        this.weaponType = WeaponType.SWORD;
        this.sound = Constants.SOUND_SWORD;
        this.range = false;
        this.longSprite = true;
        this.maxDamage = (HOLD_COUNT_MAX / SPEED_MODIFIER) * MeleeProjectile.DAMAGE_MODIFIER;
        this.ammo = 0;
        if (this.gamePanel.player != null) {
            this.gamePanel.player.addInventoryItem(new InventoryItem(this, 1, true));
        }
    }

    private void swing() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > FIST_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.isAttacking = true;
            int punchSpeed = getSpeed();
            this.swing = new SwordProjectile(this.gamePanel, punchSpeed);
            this.gamePanel.projectileManager.add(this.swing);
        }
    }

    private void playSwing() {
        if (this.isAttacking) {
            Long time = (this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND;
            try {
                if (time > FIST_DELAY/2) {
                    this.gamePanel.player.attacking = false;
                    this.isAttacking = false;
                    this.gamePanel.projectileManager.toRemove.add(this.swing);
                } else {
                    this.gamePanel.player.attacking = true;
                    SwordProjectile swing = (SwordProjectile) this.gamePanel.projectileManager.projectiles.get(0);
                    swing.setPosition();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    private int getSpeed() {
        if (this.hold > HOLD_COUNT_MAX) {
            return HOLD_COUNT_MAX/SPEED_MODIFIER;
        }
        if (this.hold < HOLD_COUNT_MIN) {
            return HOLD_COUNT_MIN/SPEED_MODIFIER;
        }
        return this.hold/SPEED_MODIFIER;
    }
}
