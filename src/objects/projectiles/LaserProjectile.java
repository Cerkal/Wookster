package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import entity.Entity.Direction;

public class LaserProjectile extends Projectile {

    public static final int DAMAGE = 20;

    public LaserProjectile(GamePanel gamePanel, int speed) {
        super(gamePanel);
        this.speed = speed;
        this.damage = 20;
        this.setImage(Constants.WEAPON_PROJECTILE_LASER);
        startPosition();
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }

    private void startPosition() {
        this.worldX = this.gamePanel.player.worldX;
        this.worldY = this.gamePanel.player.worldY;
        switch (this.gamePanel.player.direction) {
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
}
