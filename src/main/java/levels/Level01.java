package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import entity.NPCTrooper;
import main.Constants;
import main.GamePanel;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.JermeyObject;
import objects.KeyObject;
import objects.LevelDoorObject;
import objects.SignObject;
import objects.SwordObject;
import spells.ClaritySpell;
import spells.HealthSpell;
import spells.SpeedSpell;

public class Level01 extends LevelBase {

    public HashMap<String, Integer> jermeyCount = new HashMap<>();

    public Level01(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_01;
        this.background = Constants.WORLD_00_BACKGROUND;
        this.playerStartLocation = new Point(23, 23);
    }

    public void init() {
        super.init();
        this.gamePanel.eventHandler.setRandomDamageTile(5);
        addNPC(new NPCTrooper(gamePanel, 23, 12));
        addNPC(new NPCTrooper(gamePanel, 38, 8));
        addNPC(new NPCTrooper(gamePanel, 30, 39));
    }

    @Override
    public void setObjects() {
        this.generateRandomObjects();

        addGameObject(new KeyObject(this.gamePanel, 23, 7));
        addGameObject(new KeyObject(this.gamePanel, 23, 40));
        addGameObject(new KeyObject(this.gamePanel, 38, 8));
        
        addGameObject(new SwordObject(this.gamePanel, 30, 29));

        addGameObject(new DoorObject(this.gamePanel, 10, 12));
        addGameObject(new DoorObject(this.gamePanel, 8, 27));
        addGameObject(new DoorObject(this.gamePanel, 12, 23));

        addGameObject(new CarryPotionObject(this.gamePanel, new HealthSpell()));
        addGameObject(new CarryPotionObject(this.gamePanel, new SpeedSpell()));
        addGameObject(new CarryPotionObject(this.gamePanel, new ClaritySpell()));
    }

    @Override
    public void setStaticObjects() {
        LevelDoorObject levelDoor = new LevelDoorObject(this.gamePanel, 10, 7);
        levelDoor.setDifferentImage(Constants.TILE_CLEAR);
        this.gamePanel.objects.add(levelDoor);
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 13, 22, Constants.LEVEL_00_SIGN));
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 31, 37, Constants.LEVEL_00_JERMEY_SIGN));
        this.gamePanel.objects.add(new JermeyObject(this.gamePanel, 30, 36, Constants.LEVEL_00_JERMEY_SOUND_START, true));
        List<Point> jeremyList = new ArrayList<>();
            jeremyList.add(new Point(27, 37));
            jeremyList.add(new Point(28, 37));
            jeremyList.add(new Point(29, 37));
            jeremyList.add(new Point(32, 37));
            jeremyList.add(new Point(33, 37));
            List<String> jeremySounds = new ArrayList<>(
                Arrays.asList(
                    Constants.LEVEL_00_JERMEY_SOUND_00,
                    Constants.LEVEL_00_JERMEY_SOUND_01,
                    Constants.LEVEL_00_JERMEY_SOUND_02,
                    Constants.LEVEL_00_JERMEY_SOUND_03,
                    Constants.LEVEL_00_JERMEY_SOUND_04
                )
            );
            Collections.shuffle(jeremyList);
            for (int i = 0; i < jeremySounds.size() && i < jeremyList.size(); i++) {
                int x = jeremyList.get(i).x;
                int y = jeremyList.get(i).y;
                String sound = jeremySounds.get(i);
                this.gamePanel.objects.add(new JermeyObject(this.gamePanel, x, y, sound));
            }
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {}

    @Override
    public void reset() {}
}
