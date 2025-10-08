package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.weapons.BlasterWeapon;

public class LaserProjectile extends Projectile {

    public static final int DAMAGE = 25;
    public static final int PRICE = 1;

    public LaserProjectile(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.speed = BlasterWeapon.BLASTER_SPEED;
        this.price = PRICE;
        this.damage = DAMAGE;
        this.setImage(Constants.WEAPON_PROJECTILE_LASER);
        init();
    }

    public LaserProjectile(GamePanel gamePanel) {
        this(gamePanel, gamePanel.player);
    }
}
