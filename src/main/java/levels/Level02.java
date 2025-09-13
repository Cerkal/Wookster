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
import objects.SignObject;
import spells.InvincibilitySpell;

public class Level02 extends LevelBase {

    public Level02(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_02;
        this.playerStartLocation = new Point(13, 7);
    }

    public void init() {
        super.init();

        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 11, 40));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 26, 8));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 36, 32));

        NPCMom mom = new NPCMom(gamePanel, 38, 9);
        mom.setDialogue(Dialogue.LEVEL_01_MOM);
        this.gamePanel.npcs.add(mom);

        this.gamePanel.eventHandler.setRandomDamageTile(5);
        this.generateRandomObjects();
        this.generateRandomPotions();
    }

    @Override
    public void setObjects() {
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 27, 38));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 30, 30));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new InvincibilitySpell(), 10, 10));
    }

    @Override
    public void setStaticObjects() {
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 13, 6, "You don't want to go back that way."));
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
