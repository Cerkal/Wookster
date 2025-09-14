package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class ArrowProjectile extends Projectile {

    public static final int DAMAGE_MODIFIER = 2;
    public static final int PRICE = 2;

    public ArrowProjectile(GamePanel gamePanel, Entity entity, int speed) {
        super(gamePanel, entity);
        this.speed = speed;
        this.price = PRICE;
        this.damage = getDamageFromSpeed(speed);
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
    }

    public ArrowProjectile(GamePanel gamePanel, int speed) {
        this(gamePanel, gamePanel.player, speed);
    }

    private int getDamageFromSpeed(int speed) {
        return speed * DAMAGE_MODIFIER;
    }
}
