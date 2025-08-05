package spells;

import java.util.Objects;

public class SuperSpell {
    
    public enum SpellType {
        KEY_SPELL,
        HEALTH_SPELL,
        SPEED_SPELL
    }

    public SpellType spellType;
    public long startTime = 0;
    public int spellTime = 0;
    public boolean positiveSpell = false;
    public String message;

    public SuperSpell(SpellType spellType) {
        this.spellType = spellType;
    }

    public SpellType getSpellType() {
        return this.spellType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperSpell that = (SuperSpell) o;
        return spellType == that.spellType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(spellType);
    }

    @Override
    public String toString() {
        return "Spell{" +
            "spellType='" + this.spellType + '\'' +
        '}';
    }
}
