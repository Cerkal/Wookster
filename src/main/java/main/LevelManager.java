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
        this.gamePanel.config.saveConfig();
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

    public void draw(Graphics2D graphics2D) {
        getCurrentLevel().draw(graphics2D);
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
