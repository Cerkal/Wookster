package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.WeaponType;

public class GrenadeObject extends SuperObject {

    public GrenadeObject(GamePanel gamePanel) {
        super(gamePanel);
        init();
    }

    public GrenadeObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        this.removeObject();
        this.gamePanel.player.addWeapon(WeaponType.GRENADE);
    }

    private void init() {
        this.objectType = ObjectType.GRENADE;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_GRENADE);
        this.soundPrimary = Constants.SOUND_CURSOR;
    }
}
