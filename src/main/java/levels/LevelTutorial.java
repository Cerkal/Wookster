package levels;

import java.awt.Graphics2D;
import java.util.HashMap;

import entity.Animal;
import entity.Entity;
import main.Constants;
import main.GamePanel;

public class LevelTutorial extends LevelBase {

    public LevelTutorial(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_TUTORIAL;
        this.background = Constants.WORLD_00_BACKGROUND;
    }

    public void init() {
        super.init();
        this.gamePanel.player.setLocation(15 , 10);

        this.gamePanel.npcs.add(new Animal(gamePanel, 15, 15));
        this.gamePanel.npcs.add(new Animal(gamePanel, 17, 17));
        this.gamePanel.npcs.add(new Animal(gamePanel, 15, 8));
    }


    public void setObjects() {}

    @Override
    public void update() {
        // System.out.println("---");
        // for (Entity entity : this.gamePanel.npcs) {
        //     if (entity instanceof Animal) {
        //         System.out.println(entity.getLocation());
        //     }
        // }
        // System.out.println("---");
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
