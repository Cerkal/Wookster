package main;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;

import objects.PotionObject;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell.SpellType;

public abstract class Level {
    
    protected GamePanel gamePanel;
    public String mapPath;

    public Level(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.mapPath = Constants.WORLD_00;
        this.gamePanel.objects.clear();
        this.gamePanel.eventHandler = new EventHandler(this.gamePanel);
    }

    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D graphics2d);
    public abstract void reset();

    protected void generateRandomObjects() {
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
