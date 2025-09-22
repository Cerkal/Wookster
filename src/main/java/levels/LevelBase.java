package levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import entity.Entity;
import main.Constants;
import main.DataWrapper;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import main.GamePanel.GameState;
import main.InventoryItem.InventoryItemWrapper;
import objects.CarryPotionObject;
import objects.PotionObject;
import objects.SuperObject;
import objects.SuperObject.ObjectType;
import objects.SuperObject.SuperObjectWrapper;
import objects.weapons.Weapon.WeaponType;
import spells.ClaritySpell;
import spells.HealthSpell;
import spells.SpeedSpell;
import spells.SuperSpell.SpellType;

public abstract class LevelBase {

    public static class LevelWrapper {
        public String levelName;
        public int levelIndex;
        public List<SuperObjectWrapper> objects;
    }
    
    protected GamePanel gamePanel;
    protected String background = Constants.DEFAULT_BACKGROUND;
    public String mapPath;
    public int levelIndex;
    public Point playerStartLocation = new Point(23, 23);

    boolean loadFromSave = false;

    public LevelBase(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.mapPath = Constants.WORLD_00;
        this.gamePanel.objects.clear();
    }

    public void loading(boolean loadFromSave) {
        this.loadFromSave = loadFromSave;
        this.levelIndex = this.gamePanel.levelManager.currentLevelIndex;
        this.gamePanel.gameState = GameState.LOADING;
        long loadingStartTime = System.currentTimeMillis();
        new Thread(() -> {
            init();
            boolean entitiesReady = true;
            while (entitiesReady) {
                for (Entity entity : new ArrayList<>(this.gamePanel.npcs)) {
                    if (!entity.isReady) {
                        System.out.println("Entity " + entity.name + " not ready yet.");
                        entitiesReady = false;
                    } else {
                        System.out.println("Entity " + entity.name + " ready.");
                    }
                }
                if (entitiesReady) { break; }
            }
            
            long elapsed = System.currentTimeMillis() - loadingStartTime;
            if (elapsed < Constants.MIN_LOADING) {
                try {
                    Thread.sleep(Constants.MIN_LOADING - elapsed);
                } catch (InterruptedException ignored) {}
            }
            this.gamePanel.gameState = GameState.PLAY;
            this.gamePanel.config.saveConfig();
            
        }).start();
    }

    public void init() {
        this.gamePanel.player.setLocation(this.playerStartLocation);
        this.gamePanel.npcs.clear();
        this.gamePanel.objects.clear();
        this.gamePanel.effects.clear();
        loadMap();
        loadFromSaveFile();
    }

    public void loadFromSaveFile() {
        DataWrapper dataWrapper = this.gamePanel.config.dataWrapper;
        if (
            dataWrapper != null &&
            dataWrapper.getSavedLevelData(this.levelIndex) != null &&
            loadFromSave
        ){
            loadInventoryItems(dataWrapper);
            loadObjectItems(dataWrapper);
            loadPlayerSaveState(dataWrapper);
            loadQuestData(dataWrapper);
        } else {
            setObjects();
        }
        setStaticObjects();
    }

    public abstract void setObjects();
    public abstract void setStaticObjects();
    public abstract void update();
    public abstract void draw(Graphics2D graphics2d);
    public abstract void reset();

    public void loadMap() {
        this.gamePanel.tileManager.setMap(mapPath);
        try {
            this.gamePanel.background = ImageIO.read(getClass().getResourceAsStream(this.background));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void generateRandomObjects() {
        for (int i = 0; i < 3; i++) {
            addGameObject(new PotionObject(this.gamePanel));
        }
    }

    protected void generateRandomObjects(boolean carry, int number) {
        for (int i = 0; i < number; i++) {
            if (carry) {
                addGameObject(new CarryPotionObject(this.gamePanel));
            } else {
                addGameObject(new PotionObject(this.gamePanel));
            }
        }
    }

    protected void generateRandomPotions() {
        List<SpellType> spellList = Arrays.asList(SpellType.values());
        for (int i = 0; i < 3; i++) {
            int index = Utils.generateRandomInt(0, spellList.size() - 1);
            SpellType spellType = spellList.get(index);
            switch (spellType) {
                case SpellType.HEALTH_SPELL:
                    addGameObject(new CarryPotionObject(this.gamePanel, new HealthSpell()));
                    break;
                case SpellType.KEY_SPELL:
                    addGameObject(new CarryPotionObject(this.gamePanel, new SpeedSpell()));
                    break;
                case SpellType.SPEED_SPELL:
                    addGameObject(new CarryPotionObject(this.gamePanel, new ClaritySpell()));
                    break;
                default:
                    break;
            }
        }
    }

    public void addGameObject(SuperObject object) {
        this.gamePanel.objects.add(object);
    }

    public void addNPC(Entity entity) {
        entity.setIsReady();
        this.gamePanel.npcs.add(entity);
    }

    public void addNPC(List<Entity> entities) {
        for (Entity entity : new ArrayList<>(entities)) entity.setIsReady();
        this.gamePanel.npcs.addAll(entities);
    }

    public String getLevelName() {
        Class<?> c = this.getClass();
        return c.getName();
    }

    public void loadObjectItems(DataWrapper dataWrapper) {
        this.gamePanel.objects.clear();
        LevelWrapper levelWrapper = dataWrapper.getSavedLevelData(this.levelIndex);
        if (levelWrapper.objects != null) {
            List<SuperObjectWrapper> objects = levelWrapper.objects;
            for (SuperObjectWrapper object : objects) {
                addGameObject(ObjectType.create(gamePanel, object));
            }
        }
    }

    public void loadInventoryItems(DataWrapper dataWrapper) {
        this.gamePanel.player.inventory = new HashMap<>();
        List<InventoryItemWrapper> inventoryItems = dataWrapper.getSavedInventoryItems();
        if (inventoryItems != null) {
            List<InventoryItem> others = new ArrayList<>();
            List<WeaponType> weapons = new ArrayList<>();

            for (InventoryItemWrapper item : inventoryItems) {
                InventoryItem inventoryItem;
                if (item.object != null) {
                    for (int i = 0; i < item.count; i++) {
                        SuperObject object = ObjectType.create(gamePanel, item.object);
                        others.add(object.inventoryItem);
                    }
                } else if (item.weapon != null) {
                    weapons.add(item.weapon.weaponType);
                } else {
                    inventoryItem = new InventoryItem(
                        item.itemName,
                        item.count,
                        item.usable,
                        item.visibility,
                        item.sellable,
                        item.price
                    );
                    others.add(inventoryItem);
                }
            }

            for (InventoryItem item : others) {
                this.gamePanel.player.addInventoryItem(item);
            }

            for (WeaponType item : weapons) {
                this.gamePanel.player.addWeapon(item);
            }
        }
    }

    public void loadPlayerSaveState(DataWrapper dataWrapper) {
        this.gamePanel.player.loadPlayerSaveState(dataWrapper.getSavedPlayerData());
    }

    public void loadQuestData(DataWrapper dataWrapper) {
        this.gamePanel.questManager.currentQuests = dataWrapper.getCurrentQuests();
        this.gamePanel.questManager.completedQuests = dataWrapper.getCompletedQuests();
    }
}
