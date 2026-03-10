package objects.weapons;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.projectiles.GrenadeProjectile;
import objects.projectiles.Projectile.ProjectileType;

public class GrenadeWeapon extends ThrowWeapon {

    boolean isAttacking;

    private static final int GRENADE_DELAY = 1500;
    private static final int PRICE = 30;

    public GrenadeWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.weaponType = WeaponType.GRENADE;
        this.projectileType = ProjectileType.GRENADES;
        this.sound = Constants.SOUND_PUNCH;
        this.delay = 500;
        this.holdCountMin = 10;
        this.holdCountMax = 30;
        this.speedModifier = 3;
        this.range = true;
        this.delay = GRENADE_DELAY;
        this.price = PRICE;
        this.sellable = true;
        this.maxDamage = GrenadeProjectile.DAMAGE;
        addToInventory();
    }

    @Override
    public GrenadeProjectile getProjectile(Entity entity) {
        GrenadeProjectile grenade = new GrenadeProjectile(
            this.gamePanel,
            entity,
            (int) (Math.min(this.hold, this.holdCountMax) / this.speedModifier)
        );
        grenade.dispose = this.delay;
        return grenade;
    }
}