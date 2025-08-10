package spells;

import main.Constants;
import main.Utils;

public class HealthSpell extends SuperSpell {

    public SpellType spellType;
    public double healthAmount;

    public HealthSpell() {
        super(SpellType.HEALTH_SPELL);
        randomHealth();
        init();
    }

    public HealthSpell(double healthAmount) {
        super(SpellType.HEALTH_SPELL);
        this.healthAmount = healthAmount;
        init();
    }

    public HealthSpell(double healthAmount, int time) {
        this(healthAmount);
        this.spellTime = time;
    }

    private void init() {
        if (this.healthAmount > 0) {
            this.positiveSpell = true;
            this.message = Constants.MESSAGE_POTION_GOOD;
        } else {
            this.message = Constants.MESSAGE_POTION_BAD;
        }
        this.description = "Modify player's health.";
    }

    private void randomHealth() {
        this.healthAmount = Utils.generateRandomInt(-30, 30);
    }
}
