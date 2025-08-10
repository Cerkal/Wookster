package main;

import java.util.Arrays;
import java.util.List;

import entity.NPCDroids;
import entity.NPCMom;
import entity.NPCTrooper;
import objects.PotionObject;
import objects.SignObject;
import objects.ArrowsObject;
import objects.CarryPotionObject;
import objects.ChestObject;
import objects.DoorObject;
import objects.KeyObject;
import objects.LasersObject;
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
        this.gamePanel.playMusic(Constants.SOUND_BG_01);
    }

    public void setObjects() {
        this.gamePanel.objects.clear();
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 7));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 40));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 38, 8));

        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 10, 12));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 8, 27));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 12, 23));

        this.gamePanel.objects.add(new ChestObject(this.gamePanel, 10, 8));

        this.gamePanel.objects.add(new SignObject(this.gamePanel, 13, 22, "No wookies allowed..."));

        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new HealthSpell(20)));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell()));
        this.gamePanel.objects.add(new CarryPotionObject(this.gamePanel, new SpeedSpell()));

        this.gamePanel.objects.add(new LasersObject(this.gamePanel));
        this.gamePanel.objects.add(new ArrowsObject(this.gamePanel));

        generateRandomObjects();

        // Game ender
        this.gamePanel.objects.add(new PotionObject(this.gamePanel, new HealthSpell(-100),  23, 23));
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
            }
        }
    }
}
