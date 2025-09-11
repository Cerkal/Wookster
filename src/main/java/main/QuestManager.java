package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            return getCurrentQuest(name).getProgress();
        } else if (isCompletedQuest(name)) {
            return 100;
        }
        return 0;
    }

    public void removeQuest(String name) {
        this.currentQuests.remove(name);
    }

    public Quest getCurrentQuest(String name) {
        return this.currentQuests.get(name);
    }

    public Quest getCompletedQuest(String name) {
        return this.completedQuests.get(name);
    }

    public Quest getQuest(String name) {
        Quest quest = null;
        if (isActiveQuest(name)) {
            quest = getCurrentQuest(name);
        } else if (isCompletedQuest(name)) {
            quest = getCompletedQuest(name);
        }
        return quest;
    }

    public HashMap<String, Quest> getCurrentQuests() {
        return this.currentQuests;
    }

    public HashMap<String, Quest> getCompletedQuests() {
        return this.completedQuests;
    }

    public HashMap<String, Quest> getAllQuests() {
        HashMap<String, Quest> quests = new HashMap<>();
        quests.putAll(getCompletedQuests());
        quests.putAll(getCurrentQuests());
        return quests;
    }

    public List<String> getAllQuestsString() {
        HashMap<String, Quest> completedQuestMap = this.gamePanel.questManager.getCompletedQuests();
        HashMap<String, Quest> currentQuestMap = this.gamePanel.questManager.getCurrentQuests();
        List<String> completedQuestList = new ArrayList<>(completedQuestMap.keySet());
        List<String> currentQuestList = new ArrayList<>(currentQuestMap.keySet());
        List<String> allQuests = new ArrayList<>();
        allQuests.addAll(completedQuestList);
        allQuests.addAll(currentQuestList);
        return allQuests;
    }
}
