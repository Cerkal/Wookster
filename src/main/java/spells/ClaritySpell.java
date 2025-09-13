package spells;

public class ClaritySpell extends SuperSpell {

    public ClaritySpell() {
        super(SpellType.CLARITY_SPELL);
        this.spellTime = 10;
        this.descriptionText.add("See detailed effects of");
        this.descriptionText.add("other potions.");
    }

    public ClaritySpell(int time) {
        this();
        this.spellTime = time;
    }
}
