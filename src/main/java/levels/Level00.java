package levels;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import entity.NPCMom;
import entity.NPCTrooper;
import main.Constants;
import main.GamePanel;
import objects.CarryPotionObject;
import objects.DoorObject;
import objects.JermeyObject;
import objects.KeyObject;
import objects.SignObject;
import objects.SwordObject;
import spells.ClaritySpell;
import spells.HealthSpell;
import spells.SpeedSpell;

public class Level00 extends LevelBase {

    public HashMap<String, Integer> jermeyCount = new HashMap<>();

    public Level00(GamePanel gamePanel) {
        super(gamePanel);
        this.mapPath = Constants.WORLD_00;
        this.background = Constants.WORLD_00_BACKGROUND;
    }

    public void init() {
        super.init();
        this.gamePanel.eventHandler.setRandomDamageTile();
        this.setStaticObjects();
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 23, 12));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 38, 8));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 30, 39));

        this.gamePanel.npcs.add(new NPCMom(gamePanel, 10, 11));
    }

    @Override
    public void setObjects() {
        this.generateRandomObjects();

        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 7));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 40));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 38, 8));
        
        this.gamePanel.objects.add(new SwordObject(this.gamePanel, 30, 29));

        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 10, 12));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 8, 27));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 12, 23));
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 13, 22, Constants.LEVEL_00_SIGN));

        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new HealthSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new ClaritySpell()));

        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new HealthSpell(), 23, 25));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell(),  23, 26));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new ClaritySpell(),  23, 27));
    }

    public void setStaticObjects() {
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 31, 37, Constants.LEVEL_00_JERMEY_SIGN));
        this.gamePanel.objects.add(new JermeyObject(this.gamePanel, 30, 36, Constants.LEVEL_00_JERMEY_SOUND_START, true));
        List<List<Integer>> jeremyList = new ArrayList<>();
            jeremyList.add(Arrays.asList(27, 37));
            jeremyList.add(Arrays.asList(28, 37));
            jeremyList.add(Arrays.asList(29, 37));
            jeremyList.add(Arrays.asList(32, 37));
            jeremyList.add(Arrays.asList(33, 37));
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
                List<Integer> coords = jeremyList.get(i);
                int x = coords.get(0);
                int y = coords.get(1);
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
