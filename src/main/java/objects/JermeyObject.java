package objects;

import levels.Level00;
import main.Constants;
import main.GamePanel;

public class JermeyObject extends SuperObject {

    private long lastPlayTime;
    private static final String START = "start";

    public JermeyObject(GamePanel gamePanel, int worldX, int worldY, String sound) {
        super(gamePanel, worldX, worldY);
        this.soundPrimary = sound;
        this.lastPlayTime = 0;
        this.objectType = Object_Type.JERMEY;
        this.name = this.objectType.name();
        this.setImage(Constants.TILE_TREE_FALL);
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

        if (this.gamePanel.levelManager.currentLevelIndex != 0) { return; }
        Level00 level00 = (Level00) this.gamePanel.levelManager.getCurrentLevel();
        if (!this.soundPrimary.contains(START) && !level00.jermeyCount.containsKey(this.soundPrimary)) {
            level00.jermeyCount.put(this.soundPrimary, level00.jermeyCount.size());
        }
    
        if (this.soundPrimary.contains(START)) {
            if (
                level00.jermeyCount.size() == 2 &&
                level00.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_02) != null &&
                level00.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_02) == 0 &&
                level00.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_03) != null &&
                level00.jermeyCount.get(Constants.LEVEL_00_JERMEY_SOUND_03) == 1
            ){
                this.removeObject();
            } else {
                level00.jermeyCount.clear();
            }
        }
    }
}
