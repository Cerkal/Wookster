package main;

public class Quest {

    GamePanel gamePanel;

    public String name;
    public int progress;
    
    public Quest(GamePanel gamePanel, String name) {
        this.gamePanel = gamePanel;
        this.name = name;
        this.gamePanel.questManager.addQuest(this);
    }

    public void completeQuest() {
        this.progress = 100;
        this.gamePanel.questManager.completedQuests.put(this.name, this);
        this.gamePanel.questManager.removeQuest(this.name);
        this.gamePanel.sound.playSoundEffect(Constants.SOUND_QUEST_COMPLETE);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }

    @Override
    public String toString() {
        return "[" + this.progress + "]";
    }
}
