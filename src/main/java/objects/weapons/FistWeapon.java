package objects.weapons;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import main.Utils;
import objects.projectiles.PunchProjectile;
import objects.projectiles.MeleeProjectile;

public class FistWeapon extends MeleeWeapon {

    boolean isAttacking;

    public FistWeapon(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.weaponType = WeaponType.FIST;
        this.sound = Constants.SOUND_PUNCH;
        this.delay = 500;
        this.holdCountMin = 10;
        this.holdCountMax = 30;
        this.speedModifier = 3;
        this.sellable = false;
        this.maxDamage = (this.holdCountMax / this.speedModifier) * MeleeProjectile.DAMAGE_MODIFIER;
        addToInventory();
    }

    @Override
    public MeleeProjectile getProjectile(Entity entity) {
        PunchProjectile punch = new PunchProjectile(
            this.gamePanel,
            entity,
            Utils.generateRandomInt(this.holdCountMin, this.holdCountMax) / this.speedModifier
        );
        punch.setPosition();
        punch.dispose = this.delay / 2;
        return punch;
    }
}