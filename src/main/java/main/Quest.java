package main;

public class Quest {

    // GamePanel gamePanel;

    public String name;
    public int progress;
    
    public Quest(String name) { //GamePanel gamePanel, String name) {
        this.name = name;
        // this.gamePanel.questManager.addQuest(this);
    }

    public void completeQuest(GamePanel gamePanel) {
        this.progress = 100;
        gamePanel.questManager.completedQuests.put(this.name, this);
        gamePanel.questManager.removeQuest(this.name);
        gamePanel.sound.playSoundEffect(Constants.SOUND_QUEST_COMPLETE);
    }    public void setProgress(int progress) {
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
