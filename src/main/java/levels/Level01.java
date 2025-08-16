package levels;

import java.awt.Graphics2D;

import main.Constants;
import main.GamePanel;

public class Level01 extends LevelBase {

    public Level01(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_01;
    }

    public void init() {
        super.init();
    }

    @Override
    public void setObjects() { }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
