package objects;

import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.Weapon_Type;

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
        this.gamePanel.player.addWeapon(Weapon_Type.SWORD);
    }

    private void init() {
        this.objectType = Object_Type.SWORD;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_WEAPON_SWORD);
        this.soundPrimary = Constants.SOUND_CURSOR;
    }
}
