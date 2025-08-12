package objects;

import main.Constants;
import main.GamePanel;

public class JermeyObject extends SuperObject {

    private long lastPlayTime;

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

        long currentTime = gamePanel.gameTime;
        long elapsed = currentTime - lastPlayTime;

        if (elapsed > Constants.NANO_SECOND * 2.5) {
            this.gamePanel.sound.playSoundEffectDamp(this.soundPrimary);
            lastPlayTime = currentTime;
        }

        if (!this.soundPrimary.contains("start") && !this.gamePanel.jermeyTest.containsKey(this.soundPrimary)) {
            this.gamePanel.jermeyTest.put(this.soundPrimary, this.gamePanel.jermeyTest.size());
        }
        
        if (this.soundPrimary.contains("start")) {
            if (
                this.gamePanel.jermeyTest.size() == 2 &&
                this.gamePanel.jermeyTest.get("/res/sounds/jermey_02.wav") != null &&
                this.gamePanel.jermeyTest.get("/res/sounds/jermey_02.wav") == 0 &&
                this.gamePanel.jermeyTest.get("/res/sounds/jermey_03.wav") != null &&
                this.gamePanel.jermeyTest.get("/res/sounds/jermey_03.wav") == 1
            ){
                this.removeObject();
            } else {
                this.gamePanel.jermeyTest.clear();
            }
        }
    }
}
