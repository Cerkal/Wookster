package main;

import java.util.HashMap;

public class QuestManager {

    GamePanel gamePanel;

    public HashMap<String, Quest> currentQuests = new HashMap<>();
    public HashMap<String, Quest> completedQuests = new HashMap<>();

    public QuestManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void addQuest(Quest quest) {
        this.currentQuests.put(quest.name, quest);
    }

    public boolean isActiveQuest(String name) {
        return this.currentQuests.containsKey(name);
    }

    public boolean isCompletedQuest(String name) {
        return this.completedQuests.containsKey(name);
    }

    public int getProgress(String name) {
        if (isActiveQuest(name)) {
            return getQuest(name).getProgress();
        } else if (isCompletedQuest(name)) {
            return 100;
        }
        return 0;
    }

    public void removeQuest(String name) {
        this.currentQuests.remove(name);
    }

    public Quest getQuest(String name) {
        return this.currentQuests.get(name);
    }

    public HashMap<String, Quest> getCurrentQuests() {
        return this.currentQuests;
    }

    public HashMap<String, Quest> getCompletedQuests() {
        return this.completedQuests;
    }
}
