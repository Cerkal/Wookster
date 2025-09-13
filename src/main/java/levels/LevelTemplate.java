package levels;

import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;

public class LevelTemplate extends LevelBase {

    public LevelTemplate(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_02; // MAP FILE PATH
    }

    public void init() {
        super.init();
    }

    @Override
    public void setObjects() {}

    @Override
    public void setStaticObjects() {}

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
