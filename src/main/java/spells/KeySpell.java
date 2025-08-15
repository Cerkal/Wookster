package spells;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import entity.Entity.Direction;
import main.Utils;

public class KeySpell extends SuperSpell {

    public static final int KEY_COUNT = 3;
    public Direction brokeDirection = Direction.UP;
    public EnumSet<Direction> brokenSet = EnumSet.noneOf(Direction.class);
    public static EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
    
    public KeySpell() {
        super(SpellType.KEY_SPELL);
        this.brokeDirection = randomDirection();
        this.descriptionText.add("Breaks move key in certain direction.");
    }

    private static Direction randomDirection() {
        List<Direction> directionList = new ArrayList<>(directions);
        int index = Utils.generateRandomInt(0, directionList.size() - 1);
        return directionList.get(index);
    }
}
