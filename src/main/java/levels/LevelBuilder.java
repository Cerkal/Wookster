package levels;

import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;
import main.Level;

public class LevelBuilder extends Level {

    public LevelBuilder(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_BUILDER;
    }

    public void init() {
        super.init();
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
