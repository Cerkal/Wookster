package spells;

public class ClaritySpell extends SuperSpell {

    public ClaritySpell() {
        super(SpellType.CLARITY_SPELL);
        this.spellTime = 10;
        this.descriptionText.add("See detailed effects of");
        this.descriptionText.add("other potions.");
        this.sellable = true;
        this.price = 1;
        this.positiveSpell = true;
        setPriceDescription();
    }

    public ClaritySpell(int time) {
        this();
        this.spellTime = time;
    }
}
