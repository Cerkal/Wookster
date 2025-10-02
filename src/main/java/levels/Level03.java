package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Animal;
import entity.Entity;
import entity.NPCGeneric;
import entity.NPCMom;
import entity.NPCVendor;
import entity.Entity.MoveStatus;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import main.Quest;
import main.QuestDescriptions;
import main.Utils;
import main.Quest.ResolutionLevel;
import objects.ArrowsObject;
import objects.CarryPotionObject;
import objects.ContainerObject;
import objects.FoodObject;
import objects.LasersObject;
import objects.SignObject;
import objects.weapons.BlasterWeapon;
import objects.weapons.CrossbowWeapon;
import objects.weapons.SwordWeapon;
import spells.HealthSpell;
import spells.InvincibilitySpell;

public class Level03 extends LevelBase {

    Entity vendor;
    Entity trooper;
    Entity mom;

    public ContainerObject momBox;

    public Level03(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_03;
        this.playerStartLocation = new Point(15, 43);
    }

    @Override
    public void init() {
        super.init();

        List<Point> pigArea = List.of(
            new Point(15, 35),
            new Point(25, 45)
        );
        Animal animal1 = new Animal(this.gamePanel, 15, 27);
        animal1.setArea(pigArea);
        Animal animal2 = new Animal(this.gamePanel, 15, 29);
        animal2.setArea(pigArea);
        Animal animal3 = new Animal(this.gamePanel, 15, 32);
        animal3.setArea(pigArea);
        addNPC(List.of(animal1, animal2, animal3));

        // Vendor
        this.vendor = new NPCVendor(gamePanel, 25, 12);
        this.vendor.setDialogue(Dialogue.LEVEL_03_VENDOR_INTRO);
        this.vendor.addCredits(Utils.generateRandomInt(50, 70));
        this.vendor.setArea(
            List.of(
                new Point(23, 10),
                new Point(29, 16)
            )
        );
        this.vendor.setMoveStatus(MoveStatus.WANDER);
        this.vendor.name = "Bucket Head";
        this.vendor.setHat(Constants.WOOKSER_BUCKET_HAT);
        this.vendor.setVendor(
            new ArrayList<>(
                List.of(
                    new InventoryItem(new CrossbowWeapon(this.gamePanel, this.gamePanel.player, false), 3, true),
                    new InventoryItem(new BlasterWeapon(this.gamePanel, this.gamePanel.player, false), 3, true),
                    new InventoryItem(new ArrowsObject(this.gamePanel), 20, false),
                    new InventoryItem(new LasersObject(this.gamePanel), 20, false)
                )
            )
        );
        addNPC(this.vendor);

        this.mom = new NPCMom(this.gamePanel, 15, 46) {
            @Override
            public void postDialogAction() {
                this.willChase = true;
                FoodObject berries = new FoodObject(this.gamePanel, new HealthSpell(10), "BERRIES");
                if (
                    this.gamePanel.player.getInventoryItem("BERRIES") == 0 &&
                    this.gamePanel.player.getCurrentHealth() < 100 &&
                    this.gamePanel.questManager.isActiveQuest(QuestDescriptions.MOM_HOME) &&
                    this.gamePanel.questManager.getProgress(QuestDescriptions.MOM_HOME) == 0
                ){
                    this.gamePanel.player.addInventoryItem(new InventoryItem(berries, 1, true));
                    String[] lines = {"Take me home."};
                    this.setDialogue(lines);
                }
                if (this.gamePanel.questManager.getProgress(QuestDescriptions.MOM_HOME) == 50) {
                    this.gamePanel.questManager.getQuest(QuestDescriptions.MOM_HOME).completeQuest(this.gamePanel);
                    Level03 level = (Level03) this.gamePanel.levelManager.getCurrentLevel();
                    level.momBox.isLocked = false;
                }
            }
        };
        String[] lines = {"Take me home."};
        if (
            this.gamePanel.player.getInventoryItem("BERRIES") == 0 &&
            this.gamePanel.player.getCurrentHealth() < 100
        ){
            String[] berries = {"You look hungry dear. Take some berries.", "Now take me home."};
            lines = berries;
        }
        this.mom.setDialogue(lines);
        this.mom.setMoveStatus(MoveStatus.FOLLOW);
        addNPC(this.mom);

        Entity villager01 = new NPCGeneric(gamePanel, 15, 23);
        String[] villagerDialogue01 = {"Have you heard about the town run by ghouls?", "Just kidding."};
        villager01.setDialogue(villagerDialogue01);
        villager01.setWander();
        villager01.setArea(
            List.of(
                new Point(12, 22),
                new Point(19, 28)
                )
            );
        villager01.setMoveStatus(MoveStatus.WANDER);
        villager01.setHat(Constants.WOOKSER_TOP_HAT);
        addNPC(villager01);

        Entity villager02 = new NPCGeneric(gamePanel, 31, 26);
        String[] villagerDialogue02 = {"Don't ask me about this hat."};
        villager02.setDialogue(villagerDialogue02);
        villager02.setWander();
        villager02.setArea(
            List.of(
                new Point(12, 22),
                new Point(40, 30)
                )
            );
        villager02.setMoveStatus(MoveStatus.WANDER);
        villager02.setHat(Constants.WOOKSER_BALLA_HAT);
        addNPC(villager02);

        Entity villager03 = new NPCGeneric(gamePanel, 36, 23);
        String[] villagerDialogue03 = {"Hey, I'm just a villager. Don't mind me."};
        villager03.setDialogue(villagerDialogue03);
        villager03.setWander();
        villager03.setArea(
            List.of(
                new Point(12, 22),
                new Point(40, 30)
                )
            );
        villager03.setMoveStatus(MoveStatus.WANDER);
        villager03.setHat(Constants.WOOKSER_TINY_HAT);
        addNPC(villager03);

        Entity villager04 = new NPCGeneric(gamePanel, 14, 12);
        String[] villagerDialogue04 = {"Hey, I'm just a villager. Don't mind me."};
        villager04.setDialogue(villagerDialogue04);
        villager04.setWander();
        villager04.setMoveStatus(MoveStatus.WANDER);
        villager04.setHat(Constants.WOOKSER_DAD_HAT);
        addNPC(villager04);
    }

    @Override
    public void setObjects() {}

    @Override
    public void setStaticObjects() {
        ContainerObject chest01 = new ContainerObject(this.gamePanel, 13, 22);
        chest01.name = "Top Hat Dan's Chest";
        chest01.setInventoryItems(new ArrayList<>(
                List.of(
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true)

                )
            )
        );
        addGameObject(chest01);

        ContainerObject chest02 = new ContainerObject(this.gamePanel, 27, 22);
        chest02.name = "Chest";
        chest02.setInventoryItems(new ArrayList<>(
                List.of(
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true)

                )
            )
        );
        addGameObject(chest02);

        this.momBox = new ContainerObject(this.gamePanel, 33, 10);
        this.momBox.name = "Mom's Special Box";
        this.momBox.isLocked = true;
        this.momBox.setInventoryItems(new ArrayList<>(
                List.of(
                    new InventoryItem(new SwordWeapon(this.gamePanel, this.gamePanel.player, false), 1, true),
                    new InventoryItem(new ArrowsObject(this.gamePanel), 10, false),
                    new InventoryItem(new CarryPotionObject(this.gamePanel, new InvincibilitySpell()), Utils.generateRandomInt(1, 1), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(1, 2), true)
                )
            )
        );
        addGameObject(this.momBox);

        addGameObject(new SignObject(this.gamePanel, 23, 16, "Bucket Head's Junk Shop."));
        addGameObject(new SignObject(this.gamePanel, 33, 16, "Mom's house."));
    }

    @Override
    public void update() {
        if (gamePanel.questManager.getQuest(QuestDescriptions.MOM_HOME) == null) {
            gamePanel.questManager.addQuest(
                new Quest(QuestDescriptions.MOM_HOME, 
                new HashMap<>(){{
                    put(ResolutionLevel.DEFAULT, 15);
                }}
            ));
        }

        if (
            this.gamePanel.questManager.isActiveQuest(QuestDescriptions.MOM_HOME) &&
            this.gamePanel.questManager.getProgress(QuestDescriptions.MOM_HOME) < 50
        ){
            if (
                this.mom != null &&
                this.mom.getRawX() > 33 && this.mom.getRawX() < 40 &&
                this.mom.getRawY() > 11 && this.mom.getRawY() < 16
            ){
                this.mom.setArea(List.of(new Point(33, 11), new Point(40, 16)));
                this.mom.setMoveStatus(MoveStatus.WANDER);
                String[] line = {
                    "We made it!",
                    "There's a chest with some goodies in it.",
                    "And take some credits, don't spend it all in one place."
                };
                this.mom.setDialogue(line);
                this.gamePanel.questManager.getQuest(QuestDescriptions.MOM_HOME).setProgress(50);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
