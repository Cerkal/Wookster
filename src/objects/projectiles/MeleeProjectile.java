package objects.projectiles;

import entity.Entity;
import main.Constants;
import main.GamePanel;
import entity.Entity.Direction;

public class MeleeProjectile extends Projectile {

    public static final int DAMAGE_MODIFIER = 2;

    public MeleeProjectile(GamePanel gamePanel, int hold) {
        super(gamePanel);
        this.speed = 0;
        this.damage = getDamageFromHold(hold);
        this.setImage(Constants.EFFECT_ALERT);
        this.solidArea.x = Constants.TILE_SIZE/4;
        this.solidArea.y = Constants.TILE_SIZE/4;
        this.solidArea.height = Constants.TILE_SIZE/2;
        this.solidArea.width = Constants.TILE_SIZE/2;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        setPosition();
    }

    @Override
    protected void handleEntityCollision(Entity entity) {
        entity.takeDamage(this.damage);
    }

    private int getDamageFromHold(int speed) {
        return speed * DAMAGE_MODIFIER;
    }

    public void setPosition() {
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
