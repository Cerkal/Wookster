package spells;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Player;
import main.Utils;

public class SpeedSpell extends SuperSpell {

    final int MAX_SPEED = 8;
    final int MIN_SPEED = 2;
 
    public SpeedSpell() {
        super(SpellType.SPEED_SPELL);
        randomSpeed();
        this.spellTime = 10;
        init();
    }

    public SpeedSpell(int speed) {
        super(SpellType.SPEED_SPELL);
        this.speed = speed;
        this.spellTime = 10;
        init();
    }

    public SpeedSpell(int speed, int time) {
        super(SpellType.SPEED_SPELL);
        this.speed = speed;
        this.spellTime = time;
        init();
    }

    public void removeSpell(Player player) {
        super.removeSpell(player);
        player.speed = Player.DEFAULT_SPEED;
    }

    private void init() {
        if (this.speed == 0) {
            throw new IllegalArgumentException("Speed cannot be 0");
        }
        if (this.speed > Player.DEFAULT_SPEED) {
            this.positiveSpell = true;
        }
        this.descriptionText.add("Changes player's speed.");
    }
    
    public void randomSpeedSlow() {
        this.speed = Utils.generateRandomInt(this.MIN_SPEED, Player.DEFAULT_SPEED);
        if (speed == Player.DEFAULT_SPEED) {
            this.speed -= 2;
        }
    }

    private int randomSpeed() {
        int speed = Utils.generateRandomInt(this.MIN_SPEED, this.MAX_SPEED);
        if (speed == Player.DEFAULT_SPEED) {
            speed += 2;
        }
        return speed;
    }

    public int drawDescription(Graphics2D graphics2D, int x, int y, boolean clarity) {
        y = super.drawDescription(graphics2D, x, y, clarity);
        if (!clarity) return y;
        graphics2D.setColor(Color.YELLOW);
        int speedDiff = Math.abs(Player.DEFAULT_SPEED - this.speed);
        graphics2D.drawString(
            (positiveSpell ? "Increases" : "Decreases") +
            " player's speed by " + speedDiff, x, y
        );
        graphics2D.setColor(Color.WHITE);
        return y;
    }
}
