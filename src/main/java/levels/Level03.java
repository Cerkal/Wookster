package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import entity.Animal;
import entity.Entity;
import entity.NPCGeneric;
import entity.NPCMom;
import entity.NPCTrooper;
import entity.NPCVendor;
import entity.Entity.MoveStatus;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import objects.ArrowsObject;
import objects.CarryPotionObject;
import objects.ContainerObject;
import objects.FoodObject;
import objects.LasersObject;
import objects.weapons.BlasterWeapon;
import objects.weapons.CrossbowWeapon;
import objects.weapons.SwordWeapon;
import spells.HealthSpell;

public class Level03 extends LevelBase {

    Entity vendor;
    Entity trooper;

    public Level03(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_03;
        this.playerStartLocation = new Point(15, 44);
    }

    public void init() {
        super.init();

        List<Point> areaTest = List.of(
            new Point(30, 30),
            new Point(38, 38)
        );
        Animal animal1 = new Animal(this.gamePanel, 30, 30);
        animal1.areaPoints = new ArrayList<>(areaTest);
        addNPC(animal1);

        this.vendor = new NPCVendor(gamePanel, 25, 12);
        this.vendor.setDialogue(Dialogue.LEVEL_03_VENDOR_INTRO);
        this.vendor.addCredits(50);
        this.vendor.name = "Bucket Head";
        this.vendor.setVendor(
            new ArrayList<>(
                List.of(
                    new InventoryItem(new CrossbowWeapon(this.gamePanel, this.gamePanel.player, false), 3, true),
                    new InventoryItem(new SwordWeapon(this.gamePanel, this.gamePanel.player, false), 3, true),
                    new InventoryItem(new BlasterWeapon(this.gamePanel, this.gamePanel.player, false), 3, true),
                    new InventoryItem(new ArrowsObject(this.gamePanel), 20, false),
                    new InventoryItem(new LasersObject(this.gamePanel), 20, false)
                )
            )
        );
        addNPC(this.vendor);

        this.trooper = new NPCTrooper(this.gamePanel, 15, 8);
        this.trooper.setMoveStatus(MoveStatus.WANDER);
        addNPC(this.trooper);

        NPCMom mom = new NPCMom(this.gamePanel, 14, 46) {
            @Override
            public void postDialogAction() {
                this.willChase = true;
                setFollow();
                FoodObject berries = new FoodObject(this.gamePanel, new HealthSpell(10), "BERRIES");
                if (this.gamePanel.player.getInventoryItem("BERRIES") == 0) {
                    this.gamePanel.player.addInventoryItem(new InventoryItem(berries, 1, true));
                    String[] lines = {"Take me home."};
                    this.setDialogue(lines);
                }
            }
        };
        String[] lines = {"Take me home."};
        if (this.gamePanel.player.getInventoryItem("BERRIES") == 0) {
            String[] berries = {"You look hungry dear. Take some berries.", "Now take me home."};
            lines = berries;
        }
        mom.setDialogue(lines);
        mom.setMoveStatus(MoveStatus.FOLLOW);
        addNPC(mom);

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

        Entity villager02 = new NPCGeneric(gamePanel, 25, 23);
        String[] villagerDialogue02 = {"Hey, I'm just a villager. Don't mind me."};
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
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
