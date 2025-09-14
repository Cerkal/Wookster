package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import entity.NPCMom;
import entity.NPCVendor;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import objects.CarryPotionObject;
import spells.HealthSpell;
import spells.SpeedSpell;

public class Level03 extends LevelBase {

    Entity vendor;
    Entity mom;

    public Level03(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = "/maps/world_03.txt";
        this.playerStartLocation = new Point(26, 13);
    }

    public void init() {
        super.init();

        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(10), 26, 15));
        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(10), 26, 16));
        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(10), 26, 17));
        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(-10), 26, 18));
        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(-10), 26, 19));
        addGameObject(new CarryPotionObject(gamePanel, new HealthSpell(-10), 26, 20));

        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(10), 26, 15));
        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(10), 26, 16));
        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(10), 26, 17));
        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(2), 26, 18));
        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(2), 26, 19));
        addGameObject(new CarryPotionObject(gamePanel, new SpeedSpell(2), 26, 20));
        
        this.vendor = new NPCVendor(gamePanel, 15, 12);
        this.vendor.setDialogue(Dialogue.LEVEL_03_VENDOR_INTRO);
        this.vendor.setVendor(
            new ArrayList<>(
                List.of(
                    new InventoryItem("Arrows", 2, true, true, true, 1),
                    new InventoryItem("Blaster", 3, true, true, true, 1),
                    new InventoryItem("Potion", 4, true, true, true, 1)
                )
            )
        );
        this.gamePanel.npcs.add(this.vendor);

        // this.mom = new NPCMom(gamePanel, 25, 12);
        // this.mom.setDialogue(Dialogue.LEVEL_01_MOM);
        // this.gamePanel.npcs.add(this.mom);
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
