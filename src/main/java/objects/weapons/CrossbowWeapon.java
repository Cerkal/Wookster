package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Player;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import objects.projectiles.ArrowProjectile;
import objects.projectiles.Projectile.Projectile_Type;

public class CrossbowWeapon extends Weapon {

    // In milliseconds
    static final int CROSSBOW_DELAY = 500;

    static final int HOLD_COUNT_MIN = 20;
    static final int HOLD_COUNT_MAX = 100;
    static final int SPEED_MODIFIER = 3;
    static final int MAX_ARROWS = 50;
    static final int INITALIZED_ARROWS = 20;

    public CrossbowWeapon(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public CrossbowWeapon(GamePanel gamePanel, Player player) {
        super(gamePanel);
        this.player = player;
        init();
    }

    public void shoot() {
        this.ammo = this.getAmmoCount();
        if (ammo <= 0) { return; }
        if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
            this.hold++;
            this.gamePanel.player.attacking = true;
        } else {
            if (this.hold > 0) {
                shootArrow();
            }
            this.hold = 0;
            this.gamePanel.player.attacking = false;
        }
    }

    public void drawWeaponInfo(Graphics2D graphics2D, int y) {
        int x = Constants.TILE_SIZE / 6;
        int width = Constants.TILE_SIZE * 2;
        int height = Constants.TILE_SIZE / 4;

        int currentHold = this.hold;
        int maxHold = 100;

        float holdPercent = Math.max(0, Math.min(1f, (float) currentHold / maxHold));
        int holdBarWidth = (int) (width * holdPercent);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(x, y, width, height);

        graphics2D.setColor(Color.PINK);
        if (currentHold < 20) {
            graphics2D.setColor(Color.LIGHT_GRAY);
        }
        graphics2D.fillRect(x, y, holdBarWidth, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(this.weaponType.name() + ": " + Integer.toString(this.ammo), x, y - 10);
    }

    private void init() {
        this.weaponType = Weapon_Type.CROSSBOW;
        this.projectileType = Projectile_Type.ARROWS;
        this.sound = Constants.SOUND_ARROW;
        this.ammo = INITALIZED_ARROWS;
        this.maxDamage = HOLD_COUNT_MAX/SPEED_MODIFIER * ArrowProjectile.DAMAGE_MODIFIER;
        this.range = true;
        if (this.player != null) {
            this.player.addInventoryItem(new InventoryItem(this, 1, true));
            this.player.addInventoryItem(new InventoryItem(this.projectileType.name(), INITALIZED_ARROWS, false, false));
        }
    }

    private void shootArrow() {
        if (this.hold > HOLD_COUNT_MIN) {
            getSpeed();
            if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > CROSSBOW_DELAY) {
                this.lastShot = this.gamePanel.gameTime;
                this.removeAmmo();
                this.playSound();
                this.gamePanel.projectiles.add(new ArrowProjectile(this.gamePanel, this.speed));
            }
        }
    }

    private void getSpeed() {
        if (this.hold > HOLD_COUNT_MAX) {
            this.speed = HOLD_COUNT_MAX/SPEED_MODIFIER;
        } else {
            this.speed = hold/SPEED_MODIFIER;
        }
    }
}
