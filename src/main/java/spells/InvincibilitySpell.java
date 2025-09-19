package spells;

public class InvincibilitySpell extends SuperSpell {

    public InvincibilitySpell() {
        super(SpellType.INVINCIBILITY_SPELL);
        this.spellTime = 10;
        init();
    }

    public InvincibilitySpell(int time) {
        super(SpellType.INVINCIBILITY_SPELL);
        this.spellTime = time;
        init();
    }

    private void init() {
        this.price = 10;
        this.sellable = true;
        this.positiveSpell = true;
        this.descriptionText.add("Player does not take damage.");
        setPriceDescription();
    }
}
