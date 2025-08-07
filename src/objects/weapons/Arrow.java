package objects.weapons;

import java.awt.Rectangle;

import main.Constants;
import main.GamePanel;

public class Arrow extends Projectile {

    // Collision
    static final Rectangle SOLID_AREA_UP    = new Rectangle(Constants.TILE_SIZE/2, 0, 1, 1);
    static final Rectangle SOLID_AREA_DOWN  = new Rectangle(Constants.TILE_SIZE/2, Constants.TILE_SIZE, 1, 1);
    static final Rectangle SOLID_AREA_RIGHT = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE/2, 1, 1);
    static final Rectangle SOLID_AREA_LEFT  = new Rectangle(0, Constants.TILE_SIZE/2, 1, 1);
    
    public Rectangle solidArea = SOLID_AREA_UP;
    public static final int ARROW_SPEED = 14;

    public Arrow(GamePanel gamePanel) {
        super(gamePanel);
        this.speed = ARROW_SPEED;
        this.solidAreaUp = SOLID_AREA_UP;
        this.solidAreaDown = SOLID_AREA_DOWN;
        this.solidAreaRight = SOLID_AREA_RIGHT;
        this.solidAreaLeft = SOLID_AREA_LEFT;
        this.setImage(Constants.WEAPON_PROJECTILE_ARROW);
    }
}
