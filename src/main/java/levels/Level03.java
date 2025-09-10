package levels;

import java.awt.Graphics2D;
import java.awt.Point;

import main.GamePanel;

public class Level03 extends LevelBase {

    public Level03(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = "/maps/build/generated_map.txt";
        this.playerStartLocation = new Point(23, 23);
    }

    public void init() {
        super.init();
    }

    @Override
    public void setObjects() {}

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
