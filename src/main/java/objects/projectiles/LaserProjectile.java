package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.weapons.BlasterWeapon;

public class LaserProjectile extends Projectile {

    public static final int DAMAGE = 20;

    public LaserProjectile(GamePanel gamePanel, Entity entity) {
        super(gamePanel, entity);
        this.speed = BlasterWeapon.BLASTER_SPEED;
        this.damage = DAMAGE;
        this.setImage(Constants.WEAPON_PROJECTILE_LASER);
        startPosition();
    }

    public LaserProjectile(GamePanel gamePanel) {
        this(gamePanel, gamePanel.player);
    }

    private void startPosition() {
        this.worldX = this.entity.worldX;
        this.worldY = this.entity.worldY;
        switch (this.entity.direction) {
            case UP:
                this.worldY -= Constants.TILE_SIZE/2;
                break;
            case DOWN:
                this.worldY += Constants.TILE_SIZE/2;
                break;
            case LEFT:
                this.worldX -= Constants.TILE_SIZE/2;
                break;
            case RIGHT:
                this.worldX += Constants.TILE_SIZE/2;
                break;
            default:
                break;
        }
    }
}
