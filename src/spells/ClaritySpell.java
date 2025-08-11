package spells;

public class ClaritySpell extends SuperSpell {

    public SpellType spellType;

    public ClaritySpell() {
        super(SpellType.CLARITY_SPELL);
        this.descriptionText.add("See detailed effects of");
        this.descriptionText.add("other potions.");
    }
}
