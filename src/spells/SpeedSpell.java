package spells;

import entity.Player;

public class SpeedSpell extends SuperSpell {

    public int speed = 0;

    public SpellType spellType;

    public SpeedSpell(int speed) {
        super(SpellType.SPEED_SPELL);
        this.speed = speed;
        if (this.speed == 0) {
            throw new IllegalArgumentException("Speed cannot be 0");
        }
        if (this.speed > Player.DEFAULT_SPEED) {
            this.positiveSpell = true;
        }
    }

    public SpeedSpell(int speed, int time) {
        this(speed);
        this.spellTime = time;
    }
}
