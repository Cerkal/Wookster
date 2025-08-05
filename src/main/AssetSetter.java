package main;

import entity.NPCMom;
import entity.NPCTrooper;
import entity.Entity.Direction;
import objects.PotionObject;
import objects.ChestObject;
import objects.DoorObject;
import objects.KeyObject;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;

public class AssetSetter {

    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setObject() {
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 7));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 23, 40));
        this.gamePanel.objects.add(new KeyObject(this.gamePanel, 38, 8));

        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 10, 12));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 8, 27));
        this.gamePanel.objects.add(new DoorObject(this.gamePanel, 12, 23));

        this.gamePanel.objects.add(new ChestObject(this.gamePanel, 10, 8));

        this.gamePanel.objects.add(new PotionObject(this.gamePanel, 22, 7, new KeySpell(Direction.LEFT)));
        this.gamePanel.objects.add(new PotionObject(this.gamePanel, 23, 12, new HealthSpell(10)));
        this.gamePanel.objects.add(new PotionObject(this.gamePanel, 23, 30, new HealthSpell(-10)));

        this.gamePanel.objects.add(new PotionObject(this.gamePanel, 23, 32, new SpeedSpell(6, 10)));
    }

    public void setNPCs() {
        this.gamePanel.npcs.add(new NPCMom(gamePanel, 38, 8));
        this.gamePanel.npcs.add(new NPCTrooper(gamePanel, 21, 21));
    }

}
