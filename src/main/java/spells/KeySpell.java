package spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import entity.Entity.Direction;
import main.Constants;
import main.Utils;

public class KeySpell extends SuperSpell {

    public static final int KEY_COUNT = 3;
    public Direction brokeDirection = Direction.UP;
    public EnumSet<Direction> brokenSet = EnumSet.noneOf(Direction.class);
    public static EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
    
    public KeySpell() {
        super(SpellType.KEY_SPELL);
        this.brokeDirection = randomDirection();
        this.descriptionText.add("Breaks move key in certain direction");
        this.descriptionText.add("until all other directional keys");
        this.descriptionText.add("have been pushed.");
    }

    private static Direction randomDirection() {
        List<Direction> directionList = new ArrayList<>(directions);
        int index = Utils.generateRandomInt(0, directionList.size() - 1);
        return directionList.get(index);
    }

    public int drawDescription(Graphics2D graphics2D, int x, int y, boolean clarity) {
        y = super.drawDescription(graphics2D, x, y, clarity);
        if (!clarity) return y;
         y += Constants.NEW_LINE_SIZE;
        return y;
    }
}
