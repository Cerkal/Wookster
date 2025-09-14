package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import objects.projectiles.MeleeProjectile;
import objects.projectiles.SwordProjectile;

public class SwordWeapon extends MeleeWeapon {

    // In milliseconds
    static final int FIST_DELAY = 250;

    static final int HOLD_COUNT_MIN = 20;
    static final int HOLD_COUNT_MAX = 50;
    static final int SPEED_MODIFIER = 3;

    public static final int PRICE = 10;

    SwordProjectile swing;

    public SwordWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
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

    public MeleeProjectile getProjectile(Entity entity) {
        return new SwordProjectile(
            this.gamePanel,
            entity,
            Utils.generateRandomInt(HOLD_COUNT_MIN, HOLD_COUNT_MAX) / SPEED_MODIFIER
        );
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
        this.price = PRICE;
        this.sellable = true;
        if (this.gamePanel.player != null) {
            this.gamePanel.player.addInventoryItem(new InventoryItem(this, 1, true));
        }
    }

    private void swing() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > FIST_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.removeAmmo();
            this.playSound();
            this.entity.attacking = true;
            int punchSpeed = getSpeed();
            this.swing = new SwordProjectile(this.gamePanel, punchSpeed);
            this.gamePanel.projectileManager.add(this.swing);
        }
    }

    private void playSwing() {
        if (this.entity.attacking) {
            Long time = (this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND;
            try {
                if (time > FIST_DELAY/2) {
                    this.gamePanel.player.attacking = false;
                    this.entity.attacking = false;
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
}
