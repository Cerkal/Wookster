package objects.weapons;

import main.Constants;
import main.GamePanel;
import objects.projectiles.ArrowProjectile;

public class CrossbowWeapon extends Weapon {

    // In milliseconds
    public final static int CROSSBOW_DELAY = 500;

    public CrossbowWeapon(GamePanel gamePanel) {
        super(gamePanel);
    }

    public void shoot() {
        if ((this.gamePanel.gameTime - this.lastShot) / Constants.MILLISECOND > CROSSBOW_DELAY) {
            this.lastShot = this.gamePanel.gameTime;
            this.gamePanel.projectiles.add(new ArrowProjectile(this.gamePanel));
            this.gamePanel.playSoundEffect(Constants.SOUND_ARROW);
        }
    }
}
