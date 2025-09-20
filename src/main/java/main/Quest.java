package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;

public class Quest {

    String name;
    int progress;
    ResolutionLevel resolution = ResolutionLevel.DEFAULT;
    HashMap<ResolutionLevel, Integer> paymentMap;

    public static enum ResolutionLevel {
        POSTIVE,
        NEUTRAL,
        NEGATIVE,
        DEFAULT
    }
    
    public Quest(String name) {
        this.name = name;
        this.paymentMap = new HashMap<>(){{
            put(ResolutionLevel.DEFAULT, 0);
        }};
    }

    public Quest(String name, HashMap<ResolutionLevel, Integer> paymentMap) {
        this(name);
        this.paymentMap = paymentMap;
    }

    public void completeQuest(GamePanel gamePanel) {
        if (this.progress == 100) return;
        this.progress = 100;
        gamePanel.questManager.completedQuests.put(this.name, this);
        gamePanel.questManager.removeQuest(this.name);
        gamePanel.sound.playSoundEffect(Constants.SOUND_QUEST_COMPLETE);
        if (this.paymentMap.containsKey(resolution) &&
            this.paymentMap.get(resolution) > 0
        ){
            gamePanel.player.addCredits(this.paymentMap.get(resolution));
            gamePanel.sound.playSoundEffect(Constants.SOUND_COIN);
        }
    }

    public void setResolution(ResolutionLevel resolution) {
        this.resolution = resolution;
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
        if (getProgress() == 100) {
            graphics2D.setColor(Color.YELLOW);
            graphics2D.drawString("Completed", x, y);
            graphics2D.setColor(Color.WHITE);
            y += Constants.NEW_LINE_SIZE;
        }
    }

    @Override
    public String toString() {
        return "[" + this.progress + "]";
    }
}
