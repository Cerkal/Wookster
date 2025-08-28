package objects;

import main.Constants;
import main.GamePanel;

public class DoorObject extends SuperObject {

    private long lastPlayTime;

    public DoorObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.lastPlayTime = 0;
        init();
    }

    public void activateObject() {
        super.activateObject();

        String key = ObjectType.KEY.name();
        if (this.gamePanel.player.getInventoryItem(key) > 0) {
            this.removeObject();
            this.gamePanel.player.removeInventoryItem(key);
            addWalkableTile();
        } else {
            long currentTime = this.gamePanel.gameTime;
            long elapsed = currentTime - lastPlayTime;

            if (elapsed > Constants.NANO_SECOND * 2.5) {
                this.playSecondarySound();
                lastPlayTime = currentTime;
            }
        }
    }

    private void init() {
        this.objectType = ObjectType.DOOR;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_DOOR_IMAGE);
        this.collision = true;
        this.soundPrimary = Constants.SOUND_UNLOCK;
        this.soundSecondary = Constants.SOUND_LOCK;
        removeWalkableTile();
    }
}
