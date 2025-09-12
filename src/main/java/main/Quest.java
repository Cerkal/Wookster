package main;

import java.awt.Graphics2D;
import java.util.List;

public class Quest {


    public String name;
    public int progress;
    
    public Quest(String name) {
        this.name = name;
    }

    public void completeQuest(GamePanel gamePanel) {
        this.progress = 100;
        gamePanel.questManager.completedQuests.put(this.name, this);
        gamePanel.questManager.removeQuest(this.name);
        gamePanel.sound.playSoundEffect(Constants.SOUND_QUEST_COMPLETE);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }

    public List<String> getDesciption() {
        if (QuestDescriptions.DESCRIPTIONS.containsKey(this.name)) {
            return QuestDescriptions.DESCRIPTIONS.get(this.name);
        }
        return null;
    }

    public void drawInfo(GamePanel gamePanel, Graphics2D graphics2D, int x, int y) {
        List<String> details = getDesciption();
        if (details == null) { return; }
        for (String line : details) {
            graphics2D.drawString(line, x, y);
            y += Constants.NEW_LINE_SIZE;
        }
    }

    @Override
    public String toString() {
        return "[" + this.progress + "]";
    }
}
