package objects.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import main.Constants;
import main.GamePanel;
import main.Utils;
import objects.projectiles.ArrowProjectile;
import objects.projectiles.Projectile;
import objects.projectiles.Projectile.ProjectileType;

public class CrossbowWeapon extends Weapon {

    // In milliseconds
    static final int CROSSBOW_DELAY = 1750;

    static final int HOLD_COUNT_MIN = 35;
    static final int HOLD_COUNT_MAX = 100;
    static final int SPEED_MODIFIER = 3;
    static final int MAX_ARROWS = 50;
    static final int INITALIZED_ARROWS = 20;

    public static final int PRICE = 20;

    public CrossbowWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        init();
        addToInventory();
    }

    public CrossbowWeapon(GamePanel gamePanel, Entity entity, boolean addToInventory) {
        super(gamePanel, entity);
        init();
        if (addToInventory) addToInventory();
    }

    private void init() {
        this.weaponType = WeaponType.CROSSBOW;
        this.projectileType = ProjectileType.ARROWS;
        this.sound = Constants.SOUND_ARROW;
        this.initilizedAmmo = INITALIZED_ARROWS;
        this.maxDamage = HOLD_COUNT_MAX/SPEED_MODIFIER * ArrowProjectile.DAMAGE_MODIFIER;
        this.range = true;
        this.price = PRICE;
        this.sellable = true;
    }

    public void shoot() {
        if (this.entity instanceof Player) {
            this.ammo = this.getAmmoCount();
            if (ammo <= 0) { return; }
            if (this.gamePanel.keyHandler.enterPressed || this.gamePanel.keyHandler.spacePressed) {
                this.hold++;
                this.gamePanel.player.attacking = true;
            } else {
                if (this.hold > 0 && this.hold > HOLD_COUNT_MIN) {
                    getSpeed();
                    shootArrow();
                }
                this.hold = 0;
                this.gamePanel.player.attacking = false;
            }
        } else {
            if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > CROSSBOW_DELAY) {
                this.speed = Utils.generateRandomInt(HOLD_COUNT_MIN, HOLD_COUNT_MAX) / SPEED_MODIFIER;
                shootArrow();
                entity.attacking = false;
            } else {
                entity.attacking = true;
            }
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

    public Projectile getProjectile(Entity entity) {
        return new ArrowProjectile(this.gamePanel, entity, this.speed);
    }

    private void shootArrow() {
        this.lastShot = this.gamePanel.gameTime;
        this.removeAmmo();
        this.playSound();
        this.gamePanel.projectileManager.add(getProjectile(this.entity));
        this.hold = 0;
    }

    private void getSpeed() {
        if (this.hold > HOLD_COUNT_MAX) {
            this.speed = HOLD_COUNT_MAX/SPEED_MODIFIER;
        } else {
            this.speed = hold/SPEED_MODIFIER;
        }
    }
}
