package objects.weapons;

import entity.Player;
import main.Constants;
import main.GamePanel;

public class Crossbow {

    int speed = 10;
    long lastShot = 0;
    GamePanel gamePanel;
    Player player;

    // In milliseconds
    public final static int CROSSBOW_DELAY = 500;

    public boolean isEquipped;

    public Crossbow(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.player = gamePanel.player;
    }

    public void shoot() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > CROSSBOW_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.gamePanel.projectiles.add(new Arrow(this.gamePanel));
            this.gamePanel.playSoundEffect(Constants.SOUND_ARROW);
        }
    }
}
