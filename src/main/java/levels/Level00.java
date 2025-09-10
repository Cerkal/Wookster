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
import main.QuestManager;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.PotionObject;
import objects.SuperObject;
import objects.weapons.Weapon.WeaponType;
import spells.SpeedSpell;
import spells.SuperSpell;

public class Level00 extends LevelBase {

    QuestManager questManager;
    SuperObject inventoryDoor;
    SuperObject potionDoor;

    Entity oldmanPigs;
    Entity oldmanInventory;
    Entity oldmanDad;

    private static String QUEST_PIGS = "Pigs";
    private static String QUEST_INVENTORY = "Inventory";
    private static String QUEST_MOM = "Find Mom";

    List<Entity> pigs = Arrays.asList(
        new Animal(gamePanel, 20, 8),
        new Animal(gamePanel, 20, 12),
        new Animal(gamePanel, 20, 16)
    );

    public Level00(GamePanel gamePanel) {
        super(gamePanel);
        this.questManager = this.gamePanel.questManager;
        this.mapPath = Constants.WORLD_00;
        this.background = Constants.WORLD_00_BACKGROUND;
        this.playerStartLocation = new Point(15, 12);
    }

    public void init() {
        super.init();
        this.gamePanel.npcs.addAll(pigs);

        this.oldmanPigs = new NPCGeneric(gamePanel, 22, 15) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(QUEST_PIGS));
            }
        };
        this.oldmanPigs.invincable = true;
        this.oldmanPigs.setDialogue(Dialogue.TUTORIAL_PIGS_START);
        this.gamePanel.npcs.add(oldmanPigs);

        this.oldmanDad = new NPCGeneric(gamePanel, 16, 27) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(QUEST_MOM));
                this.gamePanel.player.addWeapon(WeaponType.CROSSBOW);
                this.gamePanel.levelManager.loadNextLevel();
            }
        };
        this.oldmanDad.invincable = true;
        this.oldmanDad.setDialogue(Dialogue.TUTORIAL_COMPLETE);
        this.gamePanel.npcs.add(this.oldmanDad);
    }

    public void setObjects() {
        this.inventoryDoor = new DoorObject(this.gamePanel, 36, 21);
        this.gamePanel.objects.add(this.inventoryDoor);

        this.potionDoor = new DoorObject(this.gamePanel, 27, 29);
        this.gamePanel.objects.add(this.potionDoor);

        this.gamePanel.objects.add(new PotionObject(this.gamePanel, 25, 29));
    }

    @Override
    public void update() {
        Set<Entity> inPen = new HashSet<>();
        int pigCount = 0;
        for (Entity entity : new ArrayList<>(this.gamePanel.npcs)) {
            if (!entity.isDead && entity instanceof Animal) {
                pigCount += 1;
            }
        }
        for (Entity entity : new ArrayList<>(this.gamePanel.npcs)) {
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
        if (inPen.size() == pigCount && this.questManager.isActiveQuest(QUEST_PIGS)) {
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
                    this.gamePanel.questManager
                        .getQuest(QUEST_PIGS)
                        .completeQuest(this.gamePanel);
                    this.gamePanel.questManager.addQuest(new Quest(QUEST_INVENTORY));
                }
            };
            this.oldmanPigs.invincable = true;
            this.oldmanPigs.setDialogue(lines);
            this.gamePanel.npcs.add(this.oldmanPigs);
        }

        if (this.gamePanel.questManager.isCompletedQuest(QUEST_PIGS)) {
            this.gamePanel.objects.remove(this.inventoryDoor);

            if (this.oldmanInventory == null) {
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(QUEST_INVENTORY)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getQuest(QUEST_INVENTORY);
                            if (inventoryQuest.getProgress() == 0) {
                                this.gamePanel.objects.add(
                                    new CarryPotionObject(
                                        this.gamePanel,
                                        new SpeedSpell(6),
                                        this.gamePanel.player.getRawX(),
                                        this.gamePanel.player.getRawY()
                                    )
                                );
                                inventoryQuest.setProgress(25);
                            }
                        }
                    }
                };
                this.oldmanInventory.invincable = true;
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_START);
                this.gamePanel.npcs.add(this.oldmanInventory);
            }

            if (this.gamePanel.questManager.getProgress(QUEST_INVENTORY) == 25) {
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_REMINDER);
            }

            if (
                this.gamePanel.player.spells.containsKey(SuperSpell.SpellType.SPEED_SPELL) &&
                this.gamePanel.questManager.getProgress(QUEST_INVENTORY) < 50
            ){
                Quest inventoryQuest = this.gamePanel.questManager.getQuest(QUEST_INVENTORY);
                inventoryQuest.setProgress(50);
                this.gamePanel.npcs.remove(this.oldmanInventory);
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(QUEST_INVENTORY)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getQuest(QUEST_INVENTORY);
                            if (inventoryQuest.getProgress() == 50) {
                                inventoryQuest.completeQuest(this.gamePanel);
                            }
                        }
                    }
                };
                this.oldmanInventory.invincable = true;
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_COMPLETE);
                this.gamePanel.npcs.add(this.oldmanInventory);
            }
        }

        if (this.gamePanel.questManager.isCompletedQuest(QUEST_INVENTORY)) {
            this.gamePanel.objects.remove(this.potionDoor);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
