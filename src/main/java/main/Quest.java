package main;

public class Quest {

    GamePanel gamePanel;

    public String name;
    public int progress;
    
    public Quest(GamePanel gamePanel, String name) {
        this.gamePanel = gamePanel;
        this.name = name;
        this.gamePanel.quests.put(name, this);
    }

    public void completeQuest() {
        this.gamePanel.quests.remove(this.name);
        this.gamePanel.sound.playSoundEffect(Constants.SOUND_QUEST_COMPLETE);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }
}
