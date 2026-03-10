package objects.projectiles;

import entity.Entity;
import entity.Player;
import main.Constants;
import main.GamePanel;

public class GrenadeProjectile extends Projectile {

    public static final int DAMAGE = 100;
    public static final int PRICE = 2;

    public GrenadeProjectile(GamePanel gamePanel, Entity entity, int speed) {
        super(gamePanel, entity);
        this.speed = speed;
        this.price = PRICE;
        this.damage = DAMAGE;
        this.setImage(Constants.WEAPON_PROJECTILE_GRENADE);
        init();
    }

    public GrenadeProjectile(GamePanel gamePanel, int speed) {
        this(gamePanel, gamePanel.player, speed);
    }

    @Override
    protected void collision() {
        this.gamePanel.collision.checkTileProjectile(this);

        if (this.collisionOn) {
            explode();
            return;
        }

        Entity entity = this.gamePanel.collision.projectileCollision(this);
        if (entity != null) {
            explode();
            return;
        }

        entity = (Player) this.gamePanel.collision.getProjectileEntity(this, this.gamePanel.player);
        if (entity != null) {
            explode();
            return;
        }
    }

    private void explode() {
        this.gamePanel.projectileManager.toRemove.add(this);
        this.gamePanel.effects.add(
            new effects.ExplosionEffect(this.gamePanel, this)
        );
    }
}
