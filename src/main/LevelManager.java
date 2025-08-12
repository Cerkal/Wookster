package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private GamePanel gamePanel;
    private List<Level> levels = new ArrayList<>();
    
    public int currentLevelIndex = 0;

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void loadLevel(int index) {
        if (index >= 0 && index < levels.size()) {
            currentLevelIndex = index;
            getCurrentLevel().init();
        }
    }

    public void loadNextLevel() {
        if (currentLevelIndex + 1 < levels.size()) {
            loadLevel(currentLevelIndex + 1);
        }
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public void update() {
        getCurrentLevel().update();
    }

    public void draw(Graphics2D g2) {
        getCurrentLevel().draw(g2);
    }
}
