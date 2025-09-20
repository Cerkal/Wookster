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
        this.trooper.setWander();
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
        mom.setFollow();
        addNPC(mom);

        Entity villager = new NPCGeneric(gamePanel, 25, 23);
        String[] villagerDialogue = {"Hey, I'm just a villager. Don't mind me."};
        villager.setDialogue(villagerDialogue);
        villager.setWander();
        villager.setArea(
            List.of(
                new Point(25, 25),
                new Point(35, 35)
                )
            );
        villager.setHat(Constants.WOOKSER_BALLA_HAT);
        addNPC(villager);

        Entity villager2 = new NPCGeneric(gamePanel, 15, 23);
        String[] villagerDialogue2 = {"Have you heard about the town run by ghouls?", "Just kidding."};
        villager2.setDialogue(villagerDialogue2);
        villager2.setWander();
        villager2.setArea(
            List.of(
                new Point(15, 15),
                new Point(25, 25)
                )
            );
        villager2.setHat(Constants.WOOKSER_TOP_HAT);
        addNPC(villager2);
    }

    @Override
    public void setObjects() {}

    @Override
    public void setStaticObjects() {
        ContainerObject chest = new ContainerObject(this.gamePanel, 13, 22);
        chest.setInventoryItems(new ArrayList<>(
                List.of(
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true),
                    new InventoryItem(new CarryPotionObject(this.gamePanel), Utils.generateRandomInt(0, 2), true)

                )
            )
        );
        addGameObject(chest);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
