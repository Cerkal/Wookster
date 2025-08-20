package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import objects.projectiles.PunchProjectile;
import objects.projectiles.MeleeProjectile;
import objects.projectiles.Projectile;

public class FistWeapon extends MeleeWeapon {

    // In milliseconds
    static final int DEFAULT_DELAY = 250;

    static final int DEFAULT_HOLD_COUNT_MIN = 10;
    static final int DEFAULT_HOLD_COUNT_MAX = 30;
    static final int DEFAULT_SPEED_MODIFIER = 3;

    boolean isAttacking;

    public FistWeapon(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public void shoot() {
        if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
            this.hold++;
        } else {
            if (this.hold > 0) {
                punch();
            }
            this.hold = 0;
        }
        playAttack();
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        int width = Constants.TILE_SIZE * 2;
        int height = Constants.TILE_SIZE / 4;

        int currentHold = this.hold;

        float holdPercent = Math.max(0, Math.min(1f, (float) currentHold / DEFAULT_HOLD_COUNT_MAX));
        int holdBarWidth = (int) (width * holdPercent);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.PINK);
        if (currentHold < DEFAULT_HOLD_COUNT_MIN) {
            graphics2D.setColor(Color.LIGHT_GRAY);
        }
        graphics2D.fillRect(x, y, holdBarWidth, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name(), x, y - 10);
    }

    public Projectile getProjectile(Entity entity) {
        return new PunchProjectile(
            this.gamePanel,
            entity,
            Utils.generateRandomInt(DEFAULT_HOLD_COUNT_MIN, DEFAULT_HOLD_COUNT_MAX) / DEFAULT_SPEED_MODIFIER
        );
    }

    private void init() {
        this.weaponType = WeaponType.FIST;
        this.sound = Constants.SOUND_PUNCH;
        this.range = false;
        this.maxDamage = (DEFAULT_HOLD_COUNT_MAX / DEFAULT_SPEED_MODIFIER) * MeleeProjectile.DAMAGE_MODIFIER;
        this.ammo = 0;
        if (this.gamePanel.player != null) {
            this.gamePanel.player.addInventoryItem(new InventoryItem(this, 1, true));
        }
    }

    private void punch() {
        int punchSpeed = getSpeed();
        this.projectile = new PunchProjectile(this.gamePanel, punchSpeed);
        this.attack(this.projectile);
    }
}