package spells;

public class HealthSpell extends SuperSpell {

    public SpellType spellType;
    public double healthAmount;

    public HealthSpell(double healthAmount) {
        super(SpellType.HEALTH_SPELL);
        this.healthAmount = healthAmount;
        if (healthAmount > 0) {
            this.positiveSpell = true;
            this.message = "The potion is spicy and nice.";
        } else {
            this.message = "The potion is wet and smelly.";
        }
    }

    public HealthSpell(double healthAmount, int time) {
        this(healthAmount);
        this.spellTime = time;
    }
}
