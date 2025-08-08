package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class ArrowProjectile extends Projectile {

    public static final int ARROW_SPEED = 14;
    public static final int ARROW_DAMAGE = 50;

    public ArrowProjectile(GamePanel gamePanel) {
        super(gamePanel);
        this.speed = ARROW_SPEED;
        this.damage = ARROW_DAMAGE;
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }
}
