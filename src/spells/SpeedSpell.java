package spells;

import entity.Player;
import main.Utils;

public class SpeedSpell extends SuperSpell {

    final int MAX_SPEED = 8;
    final int MIN_SPEED = 2;
 
    public int speed = Player.DEFAULT_SPEED;
    public SpellType spellType;

    public SpeedSpell() {
        super(SpellType.SPEED_SPELL);
        this.speed = randomSpeed();
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
    }

    private int randomSpeed() {
        return Utils.generateRandomInt(this.MIN_SPEED, this.MAX_SPEED);
    }
}
