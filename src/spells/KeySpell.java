package spells;

import java.util.EnumSet;

import entity.Entity.Direction;

public class KeySpell extends SuperSpell {

    public static final int KEY_COUNT = 3;
    public Direction brokeDirection = Direction.UP;
    public EnumSet<Direction> brokenSet = EnumSet.noneOf(Direction.class);
    
    public SpellType spellType;

    public KeySpell(Direction direction) {
        super(SpellType.KEY_SPELL);
        this.brokeDirection = direction;
    }
}
