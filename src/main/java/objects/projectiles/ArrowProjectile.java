package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

public class ArrowProjectile extends Projectile {

    public static final int DAMAGE_MODIFIER = 2;

    public ArrowProjectile(GamePanel gamePanel, Entity entity, int speed) {
        super(gamePanel, entity);
        this.speed = speed;
        this.damage = getDamageFromSpeed(speed);
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
        startPosition();
    }

    public ArrowProjectile(GamePanel gamePanel, int speed) {
        this(gamePanel, gamePanel.player, speed);
    }

    private int getDamageFromSpeed(int speed) {
        return speed * DAMAGE_MODIFIER;
    }

    private void startPosition() {
        if (this.entity == null) { return; }
        this.worldX = this.entity.worldX;
        this.worldY = this.entity.worldY;
        switch (this.entity.direction) {
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
