package objects;

import main.Constants;
import main.GamePanel;

public class DoorObject extends SuperObject {

    public DoorObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void activateObject() {
        super.activateObject();
        String key = Object_Type.KEY.name();
        if (this.gamePanel.player.getInventoryItem(key) > 0) {
            this.removeObject();
            this.gamePanel.player.removeInventoryItem(key);
        } else {
            this.playSecondarySound();
        }
    }

    private void init() {
        this.objectType = Object_Type.DOOR;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_DOOR_IMAGE);
        this.collision = true;
        this.soundPrimary = Constants.SOUND_UNLOCK;
        this.soundSecondary = Constants.SOUND_LOCK;
    }
}
