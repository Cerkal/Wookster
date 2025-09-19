package levels;

import java.awt.Graphics2D;
import java.awt.Point;

import entity.NPCMom;
import entity.NPCTrooper;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.KeyObject;
import objects.LevelDoorObject;
import objects.SignObject;
import spells.InvincibilitySpell;

public class Level02 extends LevelBase {

    public SignObject signObject;

    public Level02(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_02;
        this.playerStartLocation = new Point(13, 7);
    }

    public void init() {
        super.init();

        addNPC(new NPCTrooper(gamePanel, 11, 40));
        addNPC(new NPCTrooper(gamePanel, 26, 8));
        addNPC(new NPCTrooper(gamePanel, 36, 32));

        NPCMom mom = new NPCMom(gamePanel, 38, 9) {
            @Override
            public void postDialogAction() {
                this.willChase = true;
                Level02 level = (Level02) gamePanel.levelManager.getCurrentLevel();
                level.signObject.removeObject();
            }
        };
        mom.setDialogue(Dialogue.LEVEL_01_MOM);
        addNPC(mom);

        this.gamePanel.eventHandler.setRandomDamageTile(5);
    }

    @Override
    public void setObjects() {
        this.generateRandomObjects();
        this.generateRandomPotions();
        addGameObject(new DoorObject(this.gamePanel, 27, 38));
        addGameObject(new KeyObject(this.gamePanel, 30, 30));
        addGameObject(new CarryPotionObject(this.gamePanel, new InvincibilitySpell(), 10, 10));
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
