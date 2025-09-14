package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import entity.Animal;
import entity.Entity;
import entity.NPCMom;
import entity.NPCTrooper;
import entity.NPCVendor;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import objects.ArrowsObject;
import objects.FoodObject;
import objects.LasersObject;
import objects.SuperObject;
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

        addNPC(new Animal(this.gamePanel, 23, 24));
        addNPC(new Animal(this.gamePanel, 23, 27));
        addNPC(new Animal(this.gamePanel, 23, 29));
        
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

        this.trooper = new NPCTrooper(this.gamePanel, 15, 12);
        addNPC(this.trooper);

        NPCMom mom = new NPCMom(this.gamePanel, 14, 46) {
            @Override
            public void postDialogAction() {
                this.willChase = true;
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
        addNPC(mom);

    }

    @Override
    public void setObjects() {}

    @Override
    public void setStaticObjects() {}

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
