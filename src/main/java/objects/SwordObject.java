package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.WeaponType;

public class SwordObject extends SuperObject {

    public SwordObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }
    
    public SwordObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addWeapon(WeaponType.SWORD);
    }

    private void init() {
        this.objectType = ObjectType.SWORD;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_SWORD);
        this.soundPrimary = Constants.SOUND_CURSOR;
    }
}
