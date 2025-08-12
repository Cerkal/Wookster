package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entity.NPCDroids;
import entity.NPCMom;
import entity.NPCTrooper;
import objects.PotionObject;
import objects.SignObject;
import objects.SwordObject;
import objects.ArrowsObject;
import objects.BlasterObject;
import objects.CarryPotionObject;
import objects.ChestObject;
import objects.CrossbowObject;
import objects.DoorObject;
import objects.JermeyObject;
import objects.KeyObject;
import objects.LasersObject;
import spells.ClaritySpell;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell.SpellType;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setLevel() {
        setObjects();
        setNPCs();
    }

    public void setObjects() {
        this.gamePanel.objects.clear();
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 7));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 40));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 38, 8));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 30, 29));

        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 10, 12));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 8, 27));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 12, 23));
        
        List<List<Integer>> jeremyList = new ArrayList<>();
        jeremyList.add(Arrays.asList(27, 37));
        jeremyList.add(Arrays.asList(28, 37));
        jeremyList.add(Arrays.asList(29, 37));
        jeremyList.add(Arrays.asList(32, 37));
        jeremyList.add(Arrays.asList(33, 37));
        List<String> jeremySounds = new ArrayList<>(
            Arrays.asList(
                "/res/sounds/jermey_00.wav",
                "/res/sounds/jermey_01.wav",
                "/res/sounds/jermey_02.wav",
                "/res/sounds/jermey_03.wav",
                "/res/sounds/jermey_04.wav"
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

        this.gamePanel.objects.add(new SignObject(this.gamePanel, 31, 37, "Jeremy's house."));
        this.gamePanel.objects.add(new JermeyObject(this.gamePanel, 30, 36, "/res/sounds/jermey_start.wav", true));

        this.gamePanel.objects.add(new ChestObject(this.gamePanel, 10, 8));
        this.gamePanel.objects.add(new SignObject(this.gamePanel, 13, 22, "No wookies allowed..."));

        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new HealthSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new HealthSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell(), 24, 23));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell(), 24, 24));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new ClaritySpell(), 24, 25));

        this.gamePanel.objects.add(new LasersObject(this.gamePanel));
        this.gamePanel.objects.add(new ArrowsObject(this.gamePanel));

        this.gamePanel.objects.add(new BlasterObject(this.gamePanel));
        this.gamePanel.objects.add(new CrossbowObject(this.gamePanel));
        this.gamePanel.objects.add(new SwordObject(this.gamePanel));

        generateRandomObjects();

        // Game ender
        this.gamePanel.objects.add(new PotionObject(this.gamePanel, new HealthSpell(-100),  23, 35));
    }

    public void setNPCs() {
        this.gamePanel.npcs.clear();
        this.gamePanel.npcs.add(new NPCDroids(gamePanel, 9, 8));
        this.gamePanel.npcs.add(new NPCMom(gamePanel, 38, 8));
        this.gamePanel.npcs.add(new NPCMom(gamePanel, 38, 9));
        this.gamePanel.npcs.add(new NPCMom(gamePanel, 38, 10));
        this.gamePanel.npcs.add(new NPCMom(gamePanel, 38, 11));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 21, 21));
    }

    private void generateRandomObjects() {
        List<SpellType> spellList = Arrays.asList(SpellType.values());
        for (int i = 0; i < 3; i++) {
            int index = Utils.generateRandomInt(0, spellList.size() - 1);
            SpellType spellType = spellList.get(index);
            switch (spellType) {
                case SpellType.HEALTH_SPELL:
                    this.gamePanel.objects.add(new PotionObject(this.gamePanel, new HealthSpell()));
                    break;
                case SpellType.KEY_SPELL:
                    this.gamePanel.objects.add(new PotionObject(this.gamePanel, new KeySpell()));
                    break;
                case SpellType.SPEED_SPELL:
                    this.gamePanel.objects.add(new PotionObject(this.gamePanel, new SpeedSpell()));
                    break;
                default:
                    break;
            }
        }
    }
}
