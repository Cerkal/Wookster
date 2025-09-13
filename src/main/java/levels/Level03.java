package levels;

import java.awt.Graphics2D;
import java.awt.Point;

import entity.NPCVendor;
import main.GamePanel;

public class Level03 extends LevelBase {

    public Level03(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = "/maps/world_03.txt";
        this.playerStartLocation = new Point(26, 13);
    }

    public void init() {
        super.init();

        this.gamePanel.npcs.add(new NPCVendor(gamePanel, 15, 12));
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
