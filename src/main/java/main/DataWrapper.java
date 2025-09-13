package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import entity.Player.PlayerWrapper;
import levels.LevelBase.LevelWrapper;
import main.InventoryItem.InventoryItemWrapper;

public class DataWrapper {

    public int currentLevelIndex;
    PlayerWrapper player;
    String currentLevel;
    List<LevelWrapper> levels  = new ArrayList<>();
    HashMap<String, Quest> currentQuests = new HashMap<>();
    HashMap<String, Quest> completedQuests = new HashMap<>();
    HashMap<String, Integer> settingsMap = new HashMap<>();

    public HashMap<String, Quest> getCompletedQuests() {
        return this.completedQuests;
    }

    public HashMap<String, Quest> getCurrentQuests() {
        return this.currentQuests;
    }

    public PlayerWrapper getSavedPlayerData() {
        return this.player;
    }

    public List<LevelWrapper> getAllSavedLevelData() {
        return this.levels;
    }

    public LevelWrapper getSavedLevelData(int levelIndex) {
        if ((this.levels.size() - 1) >= levelIndex) {
           return this.levels.get(levelIndex);
        }
        return null;
    }

    public List<InventoryItemWrapper> getSavedInventoryItems() {
        return new ArrayList<>(this.player.inventory.values());
    }

    public String getDataForSave(GamePanel gamePanel) {
        
        this.currentQuests = gamePanel.questManager.getCurrentQuests();
        this.completedQuests = gamePanel.questManager.getCompletedQuests();
        this.settingsMap = gamePanel.ui.titleScreen.getSettings();

        // Level Data
        if (!completedQuests.isEmpty()) {
            this.player = gamePanel.player.getPlayerSaveState();
            LevelWrapper levelWrapper = gamePanel.levelManager.getLevelWrapper();
            try {
                this.levels.set(levelWrapper.levelIndex, levelWrapper);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                this.levels.add(levelWrapper);
            }
            this.currentLevel = gamePanel.levelManager.getCurrentLevel().getLevelName();
            this.currentLevelIndex = gamePanel.levelManager.currentLevelIndex;
        }

        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
