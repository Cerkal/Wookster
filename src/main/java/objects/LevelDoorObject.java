package objects;

import main.Constants;
import main.GamePanel;

public class LevelDoorObject extends SuperObject {

    public LevelDoorObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public void setDifferentImage(String path) {
        if (path != null) {
            this.image = null;
        }
    }

    public void activateObject() {
        this.gamePanel.levelManager.loadNextLevel();
    }

    private void init() {
        this.objectType = ObjectType.DOOR;
        this.name = this.objectType.name();
        this.setImage(Constants.OBJECT_DOOR_IMAGE);
        this.collision = true;
        this.soundPrimary = Constants.SOUND_UNLOCK;
        this.soundSecondary = Constants.SOUND_LOCK;
    }
}
