package objects;

import main.Constants;
import main.GamePanel;

public class LevelDoorObject extends SuperObject {

    public int levelIndex = -1;

    public LevelDoorObject(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        init();
    }

    public LevelDoorObject(GamePanel gamePanel, int worldX, int worldY, int levelIndex) {
        this(gamePanel, worldX, worldY);
        this.levelIndex = levelIndex;
    }

    public void setDifferentImage(String path) {
        if (path != null) {
            this.setImage(path);
        }
    }

    @Override
    public void activateObject() {
        this.gamePanel.levelManager.loadNextLevel();
    }

    private void init() {
        this.objectType = ObjectType.LEVEL_DOOR;
        this.name = this.objectType.name();
        this.collision = true;
        this.isSpecial = true;
        this.soundPrimary = Constants.SOUND_UNLOCK;
        this.soundSecondary = Constants.SOUND_LOCK;
    }
}
