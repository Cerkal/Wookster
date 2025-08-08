package objects;

import main.Constants;
import main.GamePanel;
import main.Utils;
import objects.projectiles.Projectile.Projectile_Type;

public class ArrowsObject extends SuperObject {

    static final int MIN_RANDOM_ARROW_AMOUNT = 10;
    static final int MAX_RANDOM_ARROW_AMOUNT = 20;

    public final String NAME  = Utils.capitalizeString(Projectile_Type.ARROWS.name());

    public ArrowsObject(GamePanel gamePanel) {
        super(gamePanel);
        System.out.println(worldX / Constants.TILE_SIZE + ", " + worldY / Constants.TILE_SIZE);
        init();
    }
    
    public ArrowsObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addInventoryItem(this.NAME, Utils.generateRandomInt(MIN_RANDOM_ARROW_AMOUNT, MAX_RANDOM_ARROW_AMOUNT));
    }

    private void init() {
        this.name = NAME;
        this.setImage(Constants.OBJECT_AMMO_ARROWS_IMAGE);
        this.collision = true;
    }
}
