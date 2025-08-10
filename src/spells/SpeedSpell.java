package spells;

import entity.Player;

public class SpeedSpell extends SuperSpell {
 
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
        this.description = "Changes player's speed.";
    }
}
