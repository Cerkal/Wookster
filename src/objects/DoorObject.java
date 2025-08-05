package objects;

import main.Constants;
import main.GamePanel;

public class DoorObject extends SuperObject {

    public final String NAME  = Constants.OBJECT_DOOR;

    public DoorObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.name = NAME;
        this.setImage(Constants.OBJECT_DOOR_IMAGE);
        this.collision = true;
        this.soundPrimary = Constants.SOUND_UNLOCK;
        this.soundSecondary = Constants.SOUND_LOCK;
    }

    public void activateObject() {
        if (this.gamePanel.player.getInventoryItem(Constants.OBJECT_KEY) > 0) {
            this.removeObject();
            this.gamePanel.player.removeInventoryItem(Constants.OBJECT_KEY);
        } else {
            this.playSecondarySound();
        }
    }
}
