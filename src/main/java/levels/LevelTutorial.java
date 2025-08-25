package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Animal;
import entity.Entity;
import entity.NPCGeneric;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.Quest;
import tile.Tile;

public class LevelTutorial extends LevelBase {

    List<Entity> pigs = Arrays.asList(
        new Animal(gamePanel, 20, 8),
        new Animal(gamePanel, 20, 12),
        new Animal(gamePanel, 20, 16)
    );

    Entity oldman;

    public LevelTutorial(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_TUTORIAL;
        this.background = Constants.WORLD_00_BACKGROUND;
    }

    public void init() {
        super.init();
        this.gamePanel.player.setLocation(15 , 12);
        this.gamePanel.npcs.addAll(pigs);

        this.oldman = new NPCGeneric(gamePanel, 22, 15) {
            @Override
            public void postDialogAction() {
                new Quest(this.gamePanel, "Pigs");
            }
        };
        String[] lines = {
            "Can you help me with these pigs?",
            "Just deal with them...",
            "Sometimes they just need a good punch.",
            "Press and hold the space bar to punch."
        };
        oldman.setDialogue(lines);
        this.gamePanel.npcs.add(oldman);
    }

    public void setObjects() {}

    @Override
    public void update() {
        Set<Entity> inPen = new HashSet<>();
        int pigCount = 0;
        for (Entity entity : this.gamePanel.npcs) {
            if (!entity.isDead && entity instanceof Animal) {
                pigCount += 1;
            }
        }
        for (Entity entity : this.gamePanel.npcs) {
            if (entity instanceof Animal) {
                if (
                    entity.getRawX() > 31 &&
                    entity.getRawX() < 37 &&
                    entity.getRawY() > 6 &&
                    entity.getRawY() < 16
                ) {
                    inPen.add(entity);
                } else {
                    inPen.remove(entity);
                }
            }
        }
        if (inPen.size() == pigCount && this.gamePanel.quests.containsKey("Pigs")) {
            this.gamePanel.quests.get("Pigs").completeQuest();
            int deadCount = 0;
            for (Entity entity : this.gamePanel.npcs) {
                if (entity instanceof Animal) {
                    entity.movable = false;
                    entity.isMoving = false;
                    if ( entity.isDead) {                
                        deadCount++;
                    }
                }
            }
            String[] lines = Dialogue.TUTORIAL_PIGS_POSITIVE;
            if (deadCount > 0) {
                lines = Dialogue.TUTORIAL_PIGS_NEUTRAL;
            }
            if (pigs.size() == deadCount) {
                lines = Dialogue.TUTORIAL_PIGS_NEGITIVE;
            }
            this.oldman.setDialogue(lines);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
