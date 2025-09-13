package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import levels.LevelBase;
import levels.LevelBase.LevelWrapper;
import objects.SuperObject;
import objects.SuperObject.SuperObjectWrapper;

public class LevelManager {
    private GamePanel gamePanel;
    private List<LevelBase> levels = new ArrayList<>();
    
    public int currentLevelIndex = 0;

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void addLevel(LevelBase level) {
        levels.add(level);
    }

    public void loadLevel(int index, boolean loadFromSave) {
        if (index >= 0 && index < levels.size()) {
            currentLevelIndex = index;
            getCurrentLevel().loading(loadFromSave);
        }
    }

    public void loadNextLevel() {
        System.out.println("------------------");
        System.out.println("Completed level.");
        System.out.println("Current Level Index: " + this.currentLevelIndex);
        System.out.println("Objects: " + this.gamePanel.objects);
        System.out.println("Loading next level.");
        System.out.println("Saving config...");
        this.gamePanel.config.saveConfig();
        System.out.println("------------------");
        if (currentLevelIndex + 1 < levels.size()) {
            loadLevel(currentLevelIndex + 1, false);
        }
    }

    public LevelBase getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public void update() {
        getCurrentLevel().update();
    }

    public void draw(Graphics2D g2) {
        getCurrentLevel().draw(g2);
    }

    public LevelWrapper getLevelWrapper() {
        List<SuperObjectWrapper> objectWrapper = new ArrayList<>();
        for (SuperObject object : this.gamePanel.objects) {
            if (!object.isSpecial) { objectWrapper.add(object.getSuperObjectWrapper()); }
        }
        LevelWrapper levelWrapper = new LevelWrapper();
        levelWrapper.levelName = this.getCurrentLevel().getLevelName();
        levelWrapper.levelIndex = this.currentLevelIndex;
        levelWrapper.objects = objectWrapper;
        return levelWrapper;
    }
}
