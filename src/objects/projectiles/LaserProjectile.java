package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import main.Utils;
import entity.Entity.Direction;

public class LaserProjectile extends Projectile {

    static final int LASER_AMMO_WEIGHT = 1;
    static final int LASER_AMMO_PRICE = 1;

    public LaserProjectile(GamePanel gamePanel, int speed) {
        super(gamePanel);
        this.speed = speed;
        this.damage = 10;
        this.projectileType = Projectile_Type.LASER;
        this.setImage(Constants.WEAPON_PROJECTILE_LASER);
        startPosition();

        // Carriable
        this.inventoryName = Utils.capitalizeString(this.projectileType.name());
        this.weight = LASER_AMMO_WEIGHT;
        this.price = LASER_AMMO_PRICE;
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }

    private void startPosition() {
        this.worldX = this.gamePanel.player.worldX;
        this.worldY = this.gamePanel.player.worldY;
        switch (this.gamePanel.player.direction) {
            case Direction.UP:
                this.worldY -= Constants.TILE_SIZE/2 + 5;
                break;
            case Direction.DOWN:
                this.worldY += Constants.TILE_SIZE/2;
                break;
            case Direction.LEFT:
                this.worldX -= Constants.TILE_SIZE/2;
                break;
            case Direction.RIGHT:
                this.worldX += Constants.TILE_SIZE/2;
                break;
            default:
                break;
        }
    } 
}
