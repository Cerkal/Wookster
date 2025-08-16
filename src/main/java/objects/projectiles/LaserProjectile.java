package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import objects.weapons.BlasterWeapon;

public class LaserProjectile extends Projectile {

    public static final int DAMAGE = 20;

    public LaserProjectile(GamePanel gamePanel) {
        super(gamePanel);
        this.speed = BlasterWeapon.BLASTER_SPEED;
        this.damage = DAMAGE;
        this.setImage(Constants.WEAPON_PROJECTILE_LASER);
        startPosition();
    }

    public LaserProjectile(GamePanel gamePanel, Entity entity) {
        this(gamePanel);
        this.direction = entity.direction;
        startPosition(entity);
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }

    private void startPosition(Entity entity) {
        this.worldX = entity.worldX;
        this.worldY = entity.worldY;
        switch (entity.direction) {
            case UP:
                this.worldY -= Constants.TILE_SIZE/2 + 5;
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

    private void startPosition() {
        startPosition(this.gamePanel.player);
    }
}
