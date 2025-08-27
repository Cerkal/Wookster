package levels;

import java.awt.Graphics2D;
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
import main.QuestManager;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.SuperObject;
import spells.SpeedSpell;
import spells.SuperSpell;

public class LevelTutorial extends LevelBase {

    SuperObject inventoryDoor;
    QuestManager questManager;

    private static String PIGS_QUEST = "Pigs";
    private static String INVENTORY_QUEST = "Inventory";

    List<Entity> pigs = Arrays.asList(
        new Animal(gamePanel, 20, 8),
        new Animal(gamePanel, 20, 12),
        new Animal(gamePanel, 20, 16)
    );

    Entity oldmanPigs;
    Entity oldmanInventory;

    public LevelTutorial(GamePanel gamePanel) {
        super(gamePanel);
        this.questManager = this.gamePanel.questManager;
        this.mapPath = Constants.WORLD_TUTORIAL;
        this.background = Constants.WORLD_00_BACKGROUND;
    }

    public void init() {
        super.init();
        this.gamePanel.player.setLocation(15 , 12);
        this.gamePanel.npcs.addAll(pigs);

        this.inventoryDoor = new DoorObject(this.gamePanel, 36, 21);
        this.gamePanel.objects.add(this.inventoryDoor);

        this.oldmanPigs = new NPCGeneric(gamePanel, 22, 15) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(this.gamePanel, PIGS_QUEST));
            }
        };
        oldmanPigs.setDialogue(Dialogue.TUTORIAL_PIGS_START);
        this.gamePanel.npcs.add(oldmanPigs);
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
        if (inPen.size() == pigCount && this.questManager.isActiveQuest(PIGS_QUEST)) {
            this.questManager.getQuest(PIGS_QUEST).completeQuest();
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

            this.gamePanel.npcs.remove(this.oldmanPigs);
            this.oldmanPigs = new NPCGeneric(gamePanel, 22, 15) {
                @Override
                public void postDialogAction() {
                    this.gamePanel.questManager.addQuest(new Quest(this.gamePanel, INVENTORY_QUEST));
                }
            };
            this.oldmanPigs.setDialogue(lines);
            this.gamePanel.npcs.add(this.oldmanPigs);
        }

        if (this.gamePanel.questManager.isActiveQuest(INVENTORY_QUEST)) {
            this.gamePanel.objects.remove(this.inventoryDoor);

            if (this.oldmanInventory == null) {
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(INVENTORY_QUEST)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getQuest(INVENTORY_QUEST);
                            if (inventoryQuest.getProgress() == 0) {
                                this.gamePanel.objects.add(
                                    new CarryPotionObject(
                                        this.gamePanel,
                                        new SpeedSpell(6),
                                        this.gamePanel.player.getRawX(),
                                        this.gamePanel.player.getRawY()
                                    )
                                );
                                inventoryQuest.setProgress(50);
                            }
                        }
                    }
                };
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_START);
                this.gamePanel.npcs.add(this.oldmanInventory);
            }

            if (this.gamePanel.player.spells.containsKey(SuperSpell.SpellType.SPEED_SPELL)) {
                Quest inventoryQuest = this.gamePanel.questManager.getQuest(INVENTORY_QUEST);
                inventoryQuest.completeQuest();
                this.gamePanel.npcs.remove(this.oldmanInventory);
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(INVENTORY_QUEST)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getQuest(INVENTORY_QUEST);
                            if (inventoryQuest.getProgress() == 0) {
                                this.gamePanel.objects.add(
                                    new CarryPotionObject(
                                        this.gamePanel,
                                        new SpeedSpell(6),
                                        this.gamePanel.player.getRawX(),
                                        this.gamePanel.player.getRawY()
                                    )
                                );
                                inventoryQuest.setProgress(50);
                            }
                        }
                    }
                };
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
