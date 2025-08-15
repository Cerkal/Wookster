package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;

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
