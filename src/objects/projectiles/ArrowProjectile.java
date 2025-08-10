package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import entity.Entity.Direction;

public class ArrowProjectile extends Projectile {

    public static final int DAMAGE_MODIFIER = 2;

    public ArrowProjectile(GamePanel gamePanel, int speed) {
        super(gamePanel);
        this.speed = speed;
        this.damage = getDamageFromSpeed(speed);
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
        startPosition();
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }

    private int getDamageFromSpeed(int speed) {
        return speed * DAMAGE_MODIFIER;
    }

    private void startPosition() {
        if (this.gamePanel.player == null) { return; }
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
