package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.Weapon_Type;

public class CrossbowObject extends SuperObject {

    public CrossbowObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public CrossbowObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addWeapon(Weapon_Type.CROSSBOW);
    }

    private void init() {
        this.objectType = Object_Type.CROSSBOW;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_CROSSBOW);
        this.soundPrimary = Constants.SOUND_CURSOR;
        System.out.println("(" + this.worldX / Constants.TILE_SIZE + ", " + this.worldY / Constants.TILE_SIZE + ")");
    }
}
