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

    public void removeQuest(String name) {
        this.currentQuests.remove(name);
    }

    public Quest getQuest(String name) {
        return this.currentQuests.get(name);
    }
}
