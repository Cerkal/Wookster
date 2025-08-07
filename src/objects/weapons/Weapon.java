package objects.weapons;

import entity.Player;
import main.GamePanel;

public class Weapon {

    int speed = 10;
    long lastShot = 0;
    GamePanel gamePanel;
    Player player;

    public Weapon(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.player = gamePanel.player;
    }

    public void shoot() {
        // For subclass
    }
}
