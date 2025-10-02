package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Entity.MoveStatus;
import entity.NPCMom;
import entity.NPCTrooper;
import entity.NPCVendor;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import main.Quest;
import main.Quest.ResolutionLevel;
import main.QuestDescriptions;
import main.Utils;
import objects.ArrowsObject;
import objects.CarryPotionObject;
import objects.ContainerObject;
import objects.DoorObject;
import objects.KeyObject;
import objects.LasersObject;
import objects.LevelDoorObject;
import objects.SignObject;
import spells.InvincibilitySpell;

public class Level02 extends LevelBase {

    public SignObject signObject;

    private NPCVendor vendor;

    public Level02(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_02;
        this.playerStartLocation = new Point(13, 7);
    }

    @Override
    public void init() {
        super.init();

        addNPC(new NPCTrooper(this.gamePanel, 11, 40));
        addNPC(new NPCTrooper(this.gamePanel, 29, 8));
        NPCTrooper trooper = new NPCTrooper(this.gamePanel, 36, 32);
        trooper.setMoveStatus(MoveStatus.WANDER);
        addNPC(trooper);

        NPCMom mom = new NPCMom(gamePanel, 38, 9) {
            @Override
            public void postDialogAction() {
                setFollow();
                Level02 level = (Level02) gamePanel.levelManager.getCurrentLevel();
                level.signObject.removeObject();
                if (gamePanel.questManager.isActiveQuest(QuestDescriptions.MOM)) {
                    gamePanel.questManager.currentQuests.get(QuestDescriptions.MOM).completeQuest(gamePanel);
                }
                if (gamePanel.questManager.getQuest(QuestDescriptions.MOM_HOME) == null) {
                    gamePanel.questManager.addQuest(
                        new Quest(QuestDescriptions.MOM_HOME, new HashMap<>(){{
                            put(ResolutionLevel.DEFAULT, 15);
                        }}
                    ));
                }
                
            }
        };
        mom.setDialogue(Dialogue.LEVEL_01_MOM);
        mom.pushback = false;
        addNPC(mom);

        this.gamePanel.eventHandler.setRandomDamageTile(5);

        // Vendor
        this.vendor = new NPCVendor(gamePanel, 12, 14);
        this.vendor.setDialogue(Dialogue.LEVEL_02_VENDOR_INTRO);
        this.vendor.addCredits(Utils.generateRandomInt(30, 50));
        this.vendor.setMoveStatus(MoveStatus.WANDER);
        this.vendor.name = "Ammo Andy";
        this.vendor.priceModifier = 3;
        this.vendor.setHat(Constants.WOOKSER_BLUE_HAT);
        this.vendor.setVendor(
            new ArrayList<>(
                List.of(
                    new InventoryItem(new ArrowsObject(this.gamePanel), Utils.generateRandomInt(10, 30), false),
                    new InventoryItem(new LasersObject(this.gamePanel), Utils.generateRandomInt(10, 30), false)
                )
            )
        );
        addNPC(this.vendor);
    }

    @Override
    public void setObjects() {
        this.generateRandomObjects();
        this.generateRandomPotions();
        addGameObject(new DoorObject(this.gamePanel, 27, 38));
        addGameObject(new KeyObject(this.gamePanel, 30, 30));
        addGameObject(new CarryPotionObject(this.gamePanel, new InvincibilitySpell(), 10, 10));

        ContainerObject chest = new ContainerObject(this.gamePanel, 11, 41);
        chest.name = "Random Chest";
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
    public void setStaticObjects() {
        this.signObject = new SignObject(this.gamePanel, 13, 6, "You don't want to go back that way.");
        addGameObject(this.signObject);
        addGameObject(new LevelDoorObject(this.gamePanel, 13, 5));
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
