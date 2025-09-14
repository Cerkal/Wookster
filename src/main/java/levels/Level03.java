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
import objects.ArrowsObject;
import objects.CarryPotionObject;
import objects.LasersObject;
import objects.weapons.Weapon;
import objects.weapons.Weapon.WeaponType;
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
        this.vendor.addCredits(50);
        this.vendor.setVendor(
            new ArrayList<>(
                List.of(
                    new InventoryItem(Weapon.WeaponType.create(gamePanel, WeaponType.BLASTER, this.vendor), 3, true),
                    new InventoryItem(Weapon.WeaponType.create(gamePanel, WeaponType.CROSSBOW, this.vendor), 3, true),
                    new InventoryItem(Weapon.WeaponType.create(gamePanel, WeaponType.SWORD, this.vendor), 3, true),
                    new InventoryItem(new ArrowsObject(gamePanel), 20, true),
                    new InventoryItem(new LasersObject(gamePanel), 20, true)
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
