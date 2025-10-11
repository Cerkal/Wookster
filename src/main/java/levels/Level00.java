package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Animal;
import entity.Entity;
import entity.NPCOld;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import main.KeyHandler;
import main.Quest;
import main.QuestDescriptions;
import main.QuestManager;
import main.Quest.ResolutionLevel;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.PotionObject;
import objects.SignObject;
import objects.SuperObject;
import objects.weapons.Weapon.WeaponType;
import spells.HealthSpell;
import spells.SpeedSpell;
import spells.SuperSpell;

public class Level00 extends LevelBase {

    QuestManager questManager;
    SuperObject inventoryDoor;
    SuperObject potionDoor;

    Entity oldmanPigs;
    Entity oldmanPigsFinished;
    Entity oldmanInventory;
    Entity oldmanDad;

    List<Entity> pigs;

    boolean savior;

    public Level00(GamePanel gamePanel) {
        super(gamePanel);
        this.questManager = this.gamePanel.questManager;
        this.mapPath = Constants.WORLD_00;
        this.background = Constants.WORLD_00_BACKGROUND;
        this.playerStartLocation = new Point(15, 12);
    }

    @Override
    public void init() {
        super.init();

        this.gamePanel.ui.displayDialog("Move using [w] [a] [s] [d] keys. Press " + KeyHandler.SPACEBAR + " to talk.");
        this.pigs = Arrays.asList(
            new Animal(gamePanel, 20, 8),
            new Animal(gamePanel, 20, 12),
            new Animal(gamePanel, 20, 9)
        );
        addNPC(this.pigs);

        this.oldmanPigs = new NPCOld(gamePanel, 22, 15) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(
                    new Quest(
                        QuestDescriptions.PIGS,
                        new HashMap<>(){{
                            put(ResolutionLevel.POSTIVE, 10);
                            put(ResolutionLevel.NEUTRAL, 7);
                            put(ResolutionLevel.NEGATIVE, 5);
                        }}
                    )
                );
            }
        };
        String[] startLines = this.gamePanel.mouseAim ? Dialogue.TUTORIAL_PIGS_START_MOUSE_AIM : Dialogue.TUTORIAL_PIGS_START;
        this.oldmanPigs.setDialogue(startLines);
        addNPC(oldmanPigs);

        this.oldmanDad = new NPCOld(gamePanel, 16, 27) {
            @Override
            public void postDialogAction() {
                this.gamePanel.questManager.addQuest(new Quest(QuestDescriptions.MOM));
                this.gamePanel.player.addWeapon(WeaponType.CROSSBOW);

                // Savior of the pigs
                if (
                    this.gamePanel.questManager.getQuest(QuestDescriptions.PIGS) != null &&
                    this.gamePanel.questManager.getQuest(QuestDescriptions.PIGS).getResolutionLevel() == ResolutionLevel.POSTIVE
                ){
                    this.gamePanel.player.addInventoryItem(
                        new InventoryItem(
                            new CarryPotionObject(
                                gamePanel,
                                new HealthSpell(10)
                            ),
                        1, true
                        )
                    );
                }

                Quest tutorial = new Quest(QuestDescriptions.TUTORIAL_COMPLETE);
                tutorial.setDisplay(false);
                this.gamePanel.questManager.addQuest(tutorial);
                this.gamePanel.questManager.getQuest(QuestDescriptions.TUTORIAL_COMPLETE).completeQuest(gamePanel);
                this.gamePanel.levelManager.loadNextLevel();
            }
        };
        String[] completeLines = this.gamePanel.mouseAim ? Dialogue.TUTORIAL_COMPLETE_MOUSE_AIM : Dialogue.TUTORIAL_COMPLETE;
        this.oldmanDad.setDialogue(completeLines);
        this.oldmanDad.setHat(Constants.WOOKSER_DAD_HAT);
        addNPC(this.oldmanDad);       
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
        if (
            inPen.size() == pigCount &&
            (
                this.questManager.isActiveQuest(QuestDescriptions.PIGS) ||
                this.questManager.isCompletedQuest(QuestDescriptions.PIGS)
            )
        ){
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
            if (this.oldmanPigsFinished == null) {
                this.oldmanPigsFinished = new NPCOld(gamePanel, 22, 15) {
                    @Override
                    public void postDialogAction() {
                        Quest quest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.PIGS);
                        if (quest != null) {
                            quest.completeQuest(this.gamePanel);
                            this.gamePanel.questManager.addQuest(new Quest(QuestDescriptions.INVENTORY));
                        }
                    }
                };
                Quest quest = this.gamePanel.questManager.getCurrentQuest(QuestDescriptions.PIGS);
                String[] lines = Dialogue.TUTORIAL_PIGS_POSITIVE;
                quest.setResolution(ResolutionLevel.POSTIVE);
                if (deadCount == 0) {
                    String[] savior = {"Oh and take this health potion, you little pig savior you."};
                    String[] mergedLines = Arrays.copyOf(
                        Dialogue.TUTORIAL_COMPLETE,
                        Dialogue.TUTORIAL_COMPLETE.length + savior.length
                    );
                    System.arraycopy(savior, 0, mergedLines, Dialogue.TUTORIAL_COMPLETE.length, savior.length);
                    this.oldmanDad.setDialogue(mergedLines);
                }
                if (deadCount > 0) {
                    lines = Dialogue.TUTORIAL_PIGS_NEUTRAL;
                    quest.setResolution(ResolutionLevel.NEUTRAL);
                }
                if (pigs.size() == deadCount) {
                    lines = Dialogue.TUTORIAL_PIGS_NEGATIVE;
                    quest.setResolution(ResolutionLevel.NEGATIVE);
                }
                this.oldmanPigsFinished.setDialogue(lines);
                addNPC(this.oldmanPigsFinished);
            }
        }

        if (this.gamePanel.questManager.isCompletedQuest(QuestDescriptions.PIGS)) {
            if (this.oldmanPigsFinished != null) this.oldmanPigsFinished.setDialogue(Dialogue.TUTORIAL_PIGS_END);
            this.gamePanel.objects.remove(this.inventoryDoor);

            if (this.oldmanInventory == null) {
                this.oldmanInventory = new NPCOld(gamePanel, 36, 25) {
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
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_START);
                addNPC(this.oldmanInventory);
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
                this.oldmanInventory = new NPCOld(gamePanel, 36, 25) {
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
                this.oldmanInventory.setDialogue(Dialogue.TUTORIAL_INVENTORY_COMPLETE);
                addNPC(this.oldmanInventory);
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
