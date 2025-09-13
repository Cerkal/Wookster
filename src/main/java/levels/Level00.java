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
import main.KeyHandler;
import main.Quest;
import main.QuestDescriptions;
import main.QuestManager;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.PotionObject;
import objects.SignObject;
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

        this.gamePanel.ui.displayDialog("Move using [w] [s] [a] [d] keys. Press " + KeyHandler.SPACEBAR + " to talk.");
        this.gamePanel.npcs.addAll(pigs);

        this.oldmanPigs = new NPCGeneric(gamePanel, 22, 15) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(QuestDescriptions.PIGS));
            }
        };
        this.oldmanPigs.invincable = true;
        this.oldmanPigs.setDialogue(Dialogue.TUTORIAL_PIGS_START);
        this.gamePanel.npcs.add(oldmanPigs);

        this.oldmanDad = new NPCGeneric(gamePanel, 16, 27) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(QuestDescriptions.MOM));
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
        addGameObject(this.inventoryDoor);
        this.potionDoor = new DoorObject(this.gamePanel, 27, 29);
        addGameObject(this.potionDoor);

        addGameObject(new PotionObject(this.gamePanel, 25, 29) {
            @Override
            public void setSpell() {
                super.setSpell();
                gamePanel.ui.displayDialog(Constants.TUTORIAL_NOTE_KEYSPELL);
            }
        });
    }

    public void setStaticObjects() {
        addGameObject(new SignObject(this.gamePanel, 30, 11, "Pig pen."));
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
        if (inPen.size() == pigCount && this.questManager.isActiveQuest(QuestDescriptions.PIGS)) {
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
            this.gamePanel.npcs.remove(this.oldmanPigs);
            this.oldmanPigs = new NPCGeneric(gamePanel, 22, 15) {
                @Override
                public void postDialogAction() {
                    Quest quest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.PIGS);
                    if (quest != null) {
                        quest.completeQuest(this.gamePanel);
                        this.gamePanel.questManager.addQuest(new Quest(QuestDescriptions.INVENTORY));
                    } else {
                        String[] lines = Dialogue.TUTORIAL_PIGS_END;
                        this.setDialogue(lines);
                    }
                }
            };
            String[] lines = Dialogue.TUTORIAL_PIGS_POSITIVE;
            if (deadCount > 0) {
                lines = Dialogue.TUTORIAL_PIGS_NEUTRAL;
            }
            if (pigs.size() == deadCount) {
                lines = Dialogue.TUTORIAL_PIGS_NEGITIVE;
            }
            this.oldmanPigs.invincable = true;
            this.oldmanPigs.setDialogue(lines);
            this.gamePanel.npcs.add(this.oldmanPigs);
        }

        if (this.gamePanel.questManager.isCompletedQuest(QuestDescriptions.PIGS)) {
            this.gamePanel.objects.remove(this.inventoryDoor);

            if (this.oldmanInventory == null) {
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(QuestDescriptions.INVENTORY)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.INVENTORY);
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

            if (this.gamePanel.questManager.getProgress(QuestDescriptions.INVENTORY) == 25) {
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_REMINDER);
            }

            if (
                this.gamePanel.player.spells.containsKey(SuperSpell.SpellType.SPEED_SPELL) &&
                this.gamePanel.questManager.getProgress(QuestDescriptions.INVENTORY) < 50
            ){
                Quest inventoryQuest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.INVENTORY);
                inventoryQuest.setProgress(50);
                this.gamePanel.npcs.remove(this.oldmanInventory);
                this.oldmanInventory = new NPCGeneric(gamePanel, 36, 25) {
                    @Override
                    public void postDialogAction() {
                        if (this.gamePanel.questManager.isActiveQuest(QuestDescriptions.INVENTORY)) {
                            Quest inventoryQuest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.INVENTORY);
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

        if (this.gamePanel.questManager.isCompletedQuest(QuestDescriptions.INVENTORY)) {
            this.gamePanel.objects.remove(this.potionDoor);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
