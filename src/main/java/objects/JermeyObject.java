package objects;

import levels.Level01;
import main.Constants;
import main.GamePanel;

public class JermeyObject extends SuperObject {

    private long lastPlayTime;
    private static final String START = "start";

    public JermeyObject(GamePanel gamePanel, int worldX, int worldY, String sound) {
        super(gamePanel, worldX, worldY);
        this.soundPrimary = sound;
        this.lastPlayTime = 0;
        this.objectType = ObjectType.JERMEY;
        this.name = this.objectType.name();
        this.setImage(Constants.TILE_TREE_FALL);
        this.isSpecial = true;
    }

    public JermeyObject(GamePanel gamePanel, int worldX, int worldY, String sound, boolean isDoor) {
        this(gamePanel, worldX, worldY, sound);
        this.collision = true;
        this.setImage(Constants.OBJECT_DOOR_IMAGE);
    }

    public void activateObject() {
        super.activateObject();

        long currentTime = this.gamePanel.gameTime;
        long elapsed = currentTime - lastPlayTime;

        if (elapsed > Constants.NANO_SECOND * 2.5) {
            this.gamePanel.sound.playSoundEffectDamp(this.soundPrimary);
            lastPlayTime = currentTime;
        }

        if (this.gamePanel.levelManager.currentLevelIndex != 1) { return; }
        Level01 level = (Level01) this.gamePanel.levelManager.getCurrentLevel();
        if (!this.soundPrimary.contains(START) && !level.jermeyCount.containsKey(this.soundPrimary)) {
            level.jermeyCount.put(this.soundPrimary, level.jermeyCount.size());
        }

        if (this.soundPrimary.contains(START)) {
            if (
                level.jermeyCount.size() == 2 &&
                level.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_04) != null &&
                level.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_04) == 0 &&
                level.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_01) != null &&
                level.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_01) == 1
            ){
                this.removeObject();
            } else {
                level.jermeyCount.clear();
            }
        }
    }
}
