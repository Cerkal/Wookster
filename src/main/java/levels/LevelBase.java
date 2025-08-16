package levels;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import main.Constants;
import main.DataWrapper;
import main.EventHandler;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import main.InventoryItem.InventoryItemWrapper;
import objects.PotionObject;
import objects.SuperObject;
import objects.SuperObject.ObjectType;
import objects.SuperObject.SuperObjectWrapper;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import spells.SuperSpell.SpellType;

public abstract class LevelBase {

    public static class LevelWrapper {
        public String levelName;
        public int levelIndex;
        public List<SuperObjectWrapper> objects;
    }
    
    protected GamePanel gamePanel;
    public String mapPath;
    public int levelIndex;

    public LevelBase(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.mapPath = Constants.WORLD_00;
        this.gamePanel.objects.clear();
        this.gamePanel.eventHandler = new EventHandler(this.gamePanel);
        this.levelIndex = this.gamePanel.levelManager.currentLevelIndex;
    }

    public void init() {
        loadMap();
        loadFromSaveFile();
    }

    public void loadFromSaveFile() {
        DataWrapper dataWrapper = this.gamePanel.config.dataWrapper;
        if (
            dataWrapper != null &&
            dataWrapper.getSavedLevelData(this.levelIndex) != null
        ){
            loadInventoryItems(dataWrapper);
            loadObjectItems(dataWrapper);
            System.out.println("Loading level " + levelIndex);
        } else {
            setObjects();
        }
    }

    public abstract void setObjects();
    public abstract void update();
    public abstract void draw(Graphics2D graphics2d);
    public abstract void reset();

    public void loadMap() {
        this.gamePanel.tileManager.setMap(mapPath);
        try {
            this.gamePanel.background = ImageIO.read(getClass().getResourceAsStream(Constants.DEFAULT_BACKGROUND));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void generateRandomObjects() {
        List<SpellType> spellList = Arrays.asList(SpellType.values());
        for (int i = 0; i < 3; i++) {
            int index = Utils.generateRandomInt(0, spellList.size() - 1);
            SpellType spellType = spellList.get(index);
            switch (spellType) {
                case SpellType.HEALTH_SPELL:
                    addGameObject(new PotionObject(this.gamePanel, new HealthSpell()));
                    break;
                case SpellType.KEY_SPELL:
                    addGameObject(new PotionObject(this.gamePanel, new KeySpell()));
                    break;
                case SpellType.SPEED_SPELL:
                    addGameObject(new PotionObject(this.gamePanel, new SpeedSpell()));
                    break;
                default:
                    break;
            }
        }
    }

    public void addGameObject(SuperObject object) {
        this.gamePanel.objects.add(object);
    }

    public String getLevelName() {
        Class<?> c = this.getClass();
        return c.getName();
    }

    private void loadObjectItems(DataWrapper dataWrapper) {
        this.gamePanel.objects.clear();
        LevelWrapper levelWrapper = dataWrapper.getSavedLevelData(this.levelIndex);
        if (levelWrapper.objects != null) {
            List<SuperObjectWrapper> objects = levelWrapper.objects;
            for (SuperObjectWrapper object : objects) {
                addGameObject(ObjectType.create(gamePanel, object));
            }
        }
    }

    private void loadInventoryItems(DataWrapper dataWrapper) {
        this.gamePanel.player.inventory = new HashMap<>();
        List<InventoryItemWrapper> inventoryItems = dataWrapper.getSavedInventoryItems();
        if (inventoryItems != null) {
            for (InventoryItemWrapper item : inventoryItems) {
                InventoryItem inventoryItem;
                if (item.object != null) {
                    SuperObject object = ObjectType.create(gamePanel, item.object);
                    this.gamePanel.player.addInventoryItem(object.inventoryItem);
                } else if (item.weapon != null) {
                    this.gamePanel.player.addWeapon(item.weapon.weaponType);
                } else {
                    inventoryItem = new InventoryItem(
                        item.itemName,
                        item.count,
                        item.usable,
                        item.visibility
                    );
                    this.gamePanel.player.addInventoryItem(inventoryItem);
                }
            }
        }
    }
}
