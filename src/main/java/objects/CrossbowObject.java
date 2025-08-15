package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.WeaponType;

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
        this.gamePanel.player.addWeapon(WeaponType.CROSSBOW);
    }

    private void init() {
        this.objectType = ObjectType.CROSSBOW;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_CROSSBOW);
        this.soundPrimary = Constants.SOUND_CURSOR;
    }
}
