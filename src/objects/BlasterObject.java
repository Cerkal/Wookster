package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.Weapon_Type;

public class BlasterObject extends SuperObject {

    public BlasterObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public BlasterObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addWeapon(Weapon_Type.BLASTER);
    }

    private void init() {
        this.objectType = Object_Type.BLASER;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_BLASTER);
        this.soundPrimary = Constants.SOUND_CURSOR;
    }
}
