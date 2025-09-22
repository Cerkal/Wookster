package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import effects.AlertEffect;
import effects.BloodEffect;
import effects.Effect;
import entity.SpriteManager.Sprite;
import main.Constants;
import main.Dialogue;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import main.GamePanel.GameState;
import objects.weapons.FistWeapon;
import objects.weapons.MeleeWeapon;
import objects.weapons.Weapon;
import objects.weapons.Weapon.WeaponType;
import tile.TileManager.TileLocation;

public abstract class Entity {

    protected GamePanel gamePanel;

    public static final int SOLID_AREA_X = 10;
    public static final int SOLID_AREA_Y = 4;
    public static final int SOLID_AREA_WIDTH = 28;
    public static final int SOLID_AREA_HEIGHT = 40;
    public static final int DEFAULT_AREA_RADIUS = 10;
    public static final int DEFAULT_TIMEOUT = 15000; // ms

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum EntityType { PLAYER, NPC, ENEMY, ANIMAL }
    public enum MoveStatus { IDEL, CHASING, WANDER, FRENZY, FOLLOW }

    // Location
    public int worldX, worldY, startingX, startingY;
    public int speed, defaultSpeed;
    public Direction direction, startingDirection;
    public MoveStatus moveStatus = MoveStatus.IDEL;
    MoveStatus defaultMoveStatus = MoveStatus.IDEL;

    // Sprite
    protected BufferedImage hat;
    public boolean isMoving = false;
    protected Sprite sprite;
    protected SpriteManager spriteManager = new SpriteManager();

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean movable = true;
    public boolean invincable = false;
    public int invincableCounter;
    public boolean isDead;
    Entity collisionEntity = null;
    Entity collisionPlayer = null;
    int collisionCounter;
    public List<Point> areaPoints = new ArrayList<>();
    public boolean pushback = true;

    // Alert
    public boolean isFriendly;
    public boolean willChase;
    public boolean isAlerted;
    protected Queue<Point> moveQueue;
    protected boolean isNeeded;
    protected boolean isFrenzy;
    protected boolean warned;
    protected String warningMessage;
    protected int aggression;
    protected boolean canSeePlayer;
    protected boolean timerStarted;
    protected Entity attackingTarget;
    protected int attackingTimeout;
    protected Point frenzyTarget = null;
    Defaults defaults;

    // Wander behavior
    public boolean wander = false;
    public long wanderTimer;

    // Entity Values
    public EntityType entityType;
    public String name = "";
    public int maxHealth = 100;
    public int health = 100;
    public String[] dialogue;
    public int dialogueIndex = 0;
    public Effect effect;
    public boolean isReady;
    
    // Weapons
    public HashMap<WeaponType, Weapon> weapons = new HashMap<>();
    public Weapon weapon;
    public Weapon primaryWeapon;
    public boolean attacking = false;
    public double accuracy = 50; // out of 100

    // Sounds
    protected String damageSound;

    // Inventory Items
    public boolean isVendor = false;
    public HashMap<String, List<InventoryItem>> inventory = new HashMap<>();

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.solidArea = new Rectangle();
        this.solidArea.x = SOLID_AREA_X;
        this.solidArea.y = SOLID_AREA_Y;
        this.solidArea.width = SOLID_AREA_WIDTH;
        this.solidArea.height = SOLID_AREA_HEIGHT;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.attackingTimeout = DEFAULT_TIMEOUT;
        this.moveStatus = MoveStatus.IDEL;
        this.defaultMoveStatus = this.moveStatus;
        this.weapons.put(WeaponType.FIST, new FistWeapon(this.gamePanel, this));
        this.loadSprites();
    }

    public Entity(GamePanel gamePanel, int worldX, int worldY) {
        this(gamePanel);
        this.startingX = worldX;
        this.startingY = worldY;
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
        this.isMoving = false;
    }

    public void update() {
        if (this.isDead) { return; }
        if (!this.isReady) { return; }
        collision();
        checkVisibility();
        switch (this.moveStatus) {
            case MoveStatus.FRENZY:
                checkFrenzy();
                break;
            case MoveStatus.CHASING:
                checkLineOfFire();
                checkChase();
                break;
            case MoveStatus.FOLLOW:
                checkFollow();
                break;
            case MoveStatus.WANDER:
                checkWander();
                break;
            case MoveStatus.IDEL:
                checkIdel();
                break;
            default:
                break;
        }
        handleMovement();
    }

    private void checkIdel() {
        if (isMoveQueueEmpty()) {
            backToStart();
        }
    }

    private void checkWander() {
        if (isMoveQueueEmpty()) {
            int defaultValue = setDefaultSpeed();
            this.speed = (int) (defaultValue *= .5);
            startFrenzy(getFrenzyLocation());
        }
    }

    private void checkFollow() {
        if (this.moveQueue == null || this.moveQueue.isEmpty()) queueChase();
    }

    public void setWander() {
        this.isFrenzy = true;
        this.movable = true;
        this.moveStatus = MoveStatus.WANDER;
    }
        
    public void setChase() {
        this.moveStatus = MoveStatus.CHASING;
        queueChase();
    }

    private void checkChase() {
        if (this.attackingTarget != null && this.attackingTarget.isDead) {
            this.attackingTarget = null;
            this.defaults.revertToDefault(this);
        }
    }

    public void setFollow() {
        this.isAlerted = true;
        this.willChase = true;
        this.pushback = true;
        setDefaultSpeed();
        this.moveStatus = MoveStatus.FOLLOW;
    }

    public void setMoveStatus(MoveStatus moveStatus) {
        this.moveStatus = moveStatus;
        this.defaultMoveStatus = moveStatus;
        switch (moveStatus) {
            case FOLLOW:
                setFollow();
                break;
            case WANDER:
                setWander();
                break;
            case IDEL:
                break;
            default:
                break;
        }
    }

    public void setArea(List<Point> points) {
        this.areaPoints = points;
    }

    private void checkVisibility() {
        if (this.movable == false) { return; }
        this.canSeePlayer = this.gamePanel.collision.checkLineTileCollision(this.gamePanel.player, this);
        
        if ((this.canSeePlayer || this.isAlerted) && this.moveStatus != MoveStatus.FRENZY && !this.isFriendly) {
            this.isAlerted = true;
            if (this.moveStatus == MoveStatus.WANDER) {
                setDefaultSpeed();
                clearMoveQueue();
            }
            this.attackingTarget = this.attackingTarget == null ? this.gamePanel.player : this.attackingTarget;
            actionTimeout();
            setChase();
        }
    }

     private void checkFrenzy() {
        if (this.isFrenzy && isMoveQueueEmpty()) {
            setDefaultSpeed();
            startFrenzy(getFrenzyLocation());
        }
    }

    private int setDefaultSpeed() {
        if (this.defaultSpeed == 0) this.defaultSpeed = this.speed;
        this.speed = this.defaultSpeed;
        return this.speed;
    }

    protected void queueChase() {
        if ((this.moveQueue == null || this.moveQueue.isEmpty()) && this.willChase) {
            setDefaultSpeed();
            List<Point> path = bfsShortestPath(
                getLocation(),
                getEnemy(this.attackingTarget).getLocation(),
                this.gamePanel.tileManager.walkableTiles
            );
            getPath(path);
        }
    }

    private void handleMovement() {
        if (this.moveQueue != null && !this.moveQueue.isEmpty()) {
            goTo();
        }
    }

    private void goTo() {
        if (this.movable == false) { return; }
        if (this.moveQueue == null || this.moveQueue.isEmpty()) { return; }
        Point point = this.moveQueue.peek();
        if (point == null) { return; }

        this.isMoving = true;

        moveEntityStep(point);

        int targetX = point.x * Constants.TILE_SIZE;
        int targetY = point.y * Constants.TILE_SIZE;

        if (Math.abs(this.worldX - targetX) <= speed && Math.abs(this.worldY - targetY) <= speed) {
            snapToGrid(point);
            this.moveQueue.poll();
        }

        isBackAtStart();
    }

    private boolean isBackAtStart() {
        if (this.moveQueue.isEmpty() && this.defaultMoveStatus == MoveStatus.IDEL) {
            int x = this.getLocation().x;
            int y = this.getLocation().y;
            if (
                (x >= this.startingX - 1 || x <= this.solidAreaDefaultX + 1) &&
                (y >= this.startingY - 1 || y <= this.solidAreaDefaultY + 1)
            ){
                this.direction = Direction.DOWN;
                this.isMoving = false;
                this.attacking = false;
            }
            return true;
        }
        return false;
    }

    private void actionTimeout() {
        if (this.timerStarted) { return; }
        
        this.timerStarted = true;
        Defaults defaults = new Defaults(this);
        new Thread(() -> {
            try {
                Thread.sleep(DEFAULT_TIMEOUT);
            } catch (InterruptedException ignored) {}
            // Reset timer
            this.timerStarted = false;

            if (defaults.moveStatus != this.moveStatus) {
                // Reset to old status
                clearMoveQueue();
                defaults.revertToDefault(this);
                defaults.printDefaults(this, "Attacker 1");
                defaults.printDefaults(this, "Attacker 2");
                System.out.println("actionTimeout: " + this.name + " is going back to " + this.moveStatus.name() + ".");
            }
        }).start();
    }

    public void handlePlayerCollision(Player player) {
        if (this.isFriendly || this.moveStatus == MoveStatus.FOLLOW) { return; }
        if (this.isAlerted) {
            if (this.weapons != null && this.weapons.containsKey(WeaponType.FIST)) {
                this.weapon = this.weapons.get(WeaponType.FIST);
                this.weapon.shoot(this);
            }
        }
    }

    public void speak() {
        if (this.dialogue == null) { return; }
        this.gamePanel.gameState = GamePanel.GameState.DIALOGUE;
        if (this.dialogueIndex >= this.dialogue.length) {
            this.gamePanel.ui.stopDialogue();
            this.dialogueIndex = 0;
            postDialogAction();
            return;
        }
        this.gamePanel.ui.displayDialog(this.dialogue[this.dialogueIndex]);
        this.dialogueIndex++;
    
        this.direction = getOppositeDirection(this.gamePanel.player.direction);
    }

    public void postDialogAction() {}

    public Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
        }
        return null;
    }

    public void takeDamage(int amount, Entity attacker) {
        if (this.invincable) { return; }
        this.gamePanel.playSoundEffect(this.damageSound);
        this.gamePanel.effects.add(new BloodEffect(this.gamePanel, this.worldX, this.worldY));
        this.effect = new AlertEffect(this.gamePanel, this);
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;
            this.movable = false;
            this.weapon = null;
        }
        if (this.isDead && this.isNeeded) {
            this.gamePanel.death();
            return;
        }
        this.isAlerted = true;
        System.out.println(this.entityType + ": " + getCurrentHealth());

        if (
            (attacker instanceof Player && this.warned) ||
            (attacker instanceof Player == false)
        ){
            if (this.aggression >= 50) {
                this.attackingTarget = attacker;
                actionTimeout();
                this.isFriendly = false;
                this.movable = true;
                this.willChase = true;
                clearMoveQueue();
                setChase();
            } else {
                this.attackingTarget = null;
                setDefaultSpeed();
                startFrenzy(getFrenzyLocation());
            }
            System.out.println(this.entityType + " attacking target: " + attacker.name);
        }
        
        if (this.isFriendly && attacker instanceof Player) {
            if (this.warningMessage == null) { this.warningMessage = Dialogue.DEFAULT_WARNING[0]; }
            this.gamePanel.ui.displayDialog(this.warningMessage);
            this.warned = true;
        }
        if (this instanceof Animal) {
            this.isFrenzy = true;
            this.frenzyTarget = null;
            if (this.moveQueue != null) {
                clearMoveQueue();
                this.frenzyTarget = getPushBackLocation(this.gamePanel.player);
                startFrenzy(this.frenzyTarget);
            }
        }
    }

    private Entity getEnemy(Entity entity) {
        return entity != null && !entity.isDead ? entity : this.gamePanel.player;
    }

    public Entity getAttackingTarget() {
        return this.attackingTarget;
    }

    public void increaseHealth(double amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void adjustHealth(double amount) {
        if (amount > 0) {
            increaseHealth(Math.abs(amount));
        } else {
            takeDamage((int) Math.abs(amount), null);
        }
    }

    public int getCurrentHealth() {
        if (this.maxHealth == 0) return 0;
        return (int) ((this.health * 100.0) / this.maxHealth);
    }

    public int getRawX() {
        return this.worldX / Constants.TILE_SIZE;
    }

    public int getRawY() {
        return this.worldY / Constants.TILE_SIZE;
    }

    public void setDialogue(String[] lines) {
        this.dialogue = lines;
    }

    protected void getSpiteImage() {
        this.sprite = this.spriteManager.getSprite(this);
    }

    public void setVendor(List<InventoryItem> items) {
        this.isVendor = true;
        this.willChase = true;
        for (InventoryItem item : items) {
            this.inventory.put(item.name, new ArrayList<>(List.of(item)));
        }
    }

    public boolean isVendor() {
        return this.isVendor;
    }

    public void addInventoryItemFromVendor(InventoryItem item) {
        if (item == null || item.count <= 0) return;
        if (!item.sellable) return;
        List<InventoryItem> itemList = inventory.computeIfAbsent(item.name, k -> new ArrayList<>());
        for (InventoryItem existing : itemList) {
            if (existing.canStackWith(item)) {
                existing.count += item.count;
                return;
            }
        }
        InventoryItem copy = new InventoryItem(item);
        copy.count = item.count;
        if (item.weapon != null && !this.weapons.containsKey(item.weapon.weaponType)) {
            this.weapons.put(item.weapon.weaponType, item.weapon);
        }
        itemList.add(copy);
    }

    public boolean removeInventoryItemFromVendor(String name, int count) {
        List<InventoryItem> itemList = inventory.get(name);
        if (itemList == null) return false;
        int remaining = count;
        for (Iterator<InventoryItem> it = itemList.iterator(); it.hasNext() && remaining > 0;) {
            InventoryItem current = it.next();
            if (!current.sellable) continue;
            if (current.count > remaining) {
                current.count -= remaining;
                remaining = 0;
                if (current.weapon != null && !this.weapons.containsKey(current.weapon.weaponType)) {
                    this.weapons.remove(current.weapon.weaponType);
                }
            } else {
                remaining -= current.count;
                it.remove();
            }
        }
        if (itemList.isEmpty()) {
            inventory.remove(name);
        }
        return remaining <= 0;
    }

    public List<InventoryItem> getInventoryItemsForSale() {
        List<InventoryItem> weaponList = new ArrayList<>();
        List<InventoryItem> otherList = new ArrayList<>();

        for (String key : this.inventory.keySet()) {
            List<InventoryItem> items = this.inventory.get(key);

            int totalCount = items.stream()
                .filter(item -> item.sellable)
                .mapToInt(item -> item.count)
                .sum();

            if (totalCount > 0) {
                InventoryItem first = items.get(0);
                InventoryItem item = new InventoryItem(first);
                item.count = totalCount;

                if (item.weapon != null) {
                    weaponList.add(item);
                } else {
                    otherList.add(item);
                }
            }
        }
        weaponList.sort(Comparator.comparing(item -> item.name));
        otherList.sort(Comparator.comparing(item -> item.name));
        List<InventoryItem> selectableList = new ArrayList<>();
        selectableList.addAll(weaponList);
        selectableList.addAll(otherList);
        return selectableList;
    }

    protected abstract void loadSprites();

    protected void drawDebugCollision(Graphics2D graphics2D, int screenX, int screenY) {
        if (!this.gamePanel.debugCollision) { return; }
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(
            screenX + solidArea.x,
            screenY + solidArea.y,
            solidArea.width,
            solidArea.height
        );
    }

    public int getCredits() {
        return this.inventory.get(Constants.CREDITS).get(0).count;
    }

    public void addInventoryItem(InventoryItem item) {
        this.inventory.computeIfAbsent(item.name, k -> new ArrayList<>()).add(item);
        displayInventoryMessage(item);
    }

    private void displayInventoryMessage(InventoryItem item) {
        if (this instanceof Player == false || this.gamePanel.gameState == GameState.VENDOR) { return; }
        if (item.count > 1) {
            this.gamePanel.ui.displayMessage(item.count + " " + item.name.toLowerCase() + Constants.MESSGE_INVENTORY_ADDED);
        } else {
            this.gamePanel.ui.displayMessage(item.name + Constants.MESSGE_INVENTORY_ADDED);
        }
    }

    public void addCredits(int amount) {
        InventoryItem item = new InventoryItem(
            Constants.CREDITS,
            amount,
            false,
            true,
            false,
            1
        );
        if (this.inventory.containsKey(Constants.CREDITS)) {
            this.inventory.get(Constants.CREDITS).get(0).count += amount;
            displayInventoryMessage(item);
        } else {
            addInventoryItem(item);
        }
    }

    public void removeCredits(int amount) {
        if (this.inventory.containsKey(Constants.CREDITS)) {
            this.inventory.get(Constants.CREDITS).get(0).count -= amount;
        }
    }

    private void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        this.gamePanel.collision.objectCollision(this, false);

        collisionEntity = this.gamePanel.collision.entityCollision(this);
        collisionPlayer = this.gamePanel.collision.getCollidEntity(this, this.gamePanel.player);

        boolean collidedWithEntity = collisionEntity != null;
        boolean collidedWithPlayer = collisionPlayer != null;
        
        if (collidedWithPlayer || collidedWithEntity) {
            handlePlayerCollision(this.gamePanel.player);
        } else {
            if (this.primaryWeapon != null) this.weapon = this.primaryWeapon;
        }

        if ((collidedWithEntity || collidedWithPlayer)) {
            if (
                !this.pushback ||
                this.moveStatus == MoveStatus.FOLLOW ||
                this.moveStatus == MoveStatus.CHASING ||
                this.moveStatus == MoveStatus.IDEL
            ){
                this.isMoving = false;
                return;
            }

            collisionCounter++;
            if (collisionCounter > this.speed * 2) {
                if (collidedWithEntity) {
                    this.frenzyTarget = getPushBackLocation(collisionEntity);
                }
                if (collidedWithPlayer) {
                    this.frenzyTarget = getPushBackLocation(collisionPlayer);
                }
                startFrenzy(this.frenzyTarget);
                collisionCounter = 0;
            }
        }
    }

    public void setHat(String hatPath) {
        if (hatPath == null) { return; }
        try {
            this.hat = ImageIO.read(getClass().getResourceAsStream(hatPath));
            this.hat = Utils.scaleImage(this.hat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPath(List<Point> path) {
        if (path != null) {
            this.moveQueue = new LinkedList<>();
            Point last = null;
            for (Point point : path) {
                this.moveQueue.add(point);
                last = point;
            }
            if (this.moveQueue.size() >= 1 && last != null && this.moveStatus == MoveStatus.FOLLOW) {
                this.moveQueue.remove(last);
            }
        }
    }

    private void checkLineOfFire() {
        if (this.isFriendly) { return; }
        if (this.primaryWeapon == null) { return; }
        if (this.primaryWeapon instanceof MeleeWeapon) { return; }
        if (!this.isAlerted) { return; }
        
        int buffer = 1;
        int entityX = getLocation().x;
        int entityY = getLocation().y;
        int attackingX = getEnemy(this.attackingTarget).getLocation().x;
        int attackingY = getEnemy(this.attackingTarget).getLocation().y;

        int locBuffer = Constants.TILE_SIZE;
        this.movable = true;

        switch (this.direction) {
            case UP:
                if (entityY <= attackingY) { return; }
                if (entityX >= attackingX - buffer && entityX <= attackingX + buffer) {
                    this.primaryWeapon.shoot(this);
                }
                if (this.worldX >= this.attackingTarget.worldX - locBuffer && this.worldX <= this.attackingTarget.worldX + locBuffer) {
                    int diffY = Math.abs(entityY - attackingY);
                    if (diffY < 5) {
                        this.movable = false;
                        this.isMoving = false;
                        this.attacking = true;
                    }
                }
                break;
            case DOWN:
                if (entityY >= attackingY) { return; }
                if (entityX >= attackingX - buffer && entityX <= attackingX + buffer) {
                    this.primaryWeapon.shoot(this);
                }
                if (this.worldX >= this.attackingTarget.worldX - locBuffer && this.worldX <= this.attackingTarget.worldX + locBuffer) {
                    int diffY = Math.abs(entityY - attackingY);
                    if (diffY < 5) {
                        this.movable = false;
                        this.isMoving = false;
                        this.attacking = true;
                    }
                }
                break;
            case RIGHT:
                if (entityX >= attackingX) { return; }
                if (entityY >= attackingY - buffer && entityY <= attackingY + buffer) {
                    this.primaryWeapon.shoot(this);
                }
                if (this.worldY >= this.attackingTarget.worldY - locBuffer && this.worldY <= this.attackingTarget.worldY + locBuffer) {
                    int diffX = Math.abs(entityX - attackingX);
                    if (diffX < 5) {
                        this.movable = false;
                        this.isMoving = false;
                        this.attacking = true;
                    }
                }
                break;
            case LEFT:
                if (entityX <= attackingX) { return; }
                if (entityY >= attackingY - buffer && entityY <= attackingY + buffer) {
                    this.primaryWeapon.shoot(this);
                }
                if (this.worldY >= this.attackingTarget.worldY - locBuffer && this.worldY <= this.attackingTarget.worldY + locBuffer) {
                    int diffX = Math.abs(entityX - attackingX);
                    if (diffX < 5) {
                        this.movable = false;
                        this.isMoving = false;
                        this.attacking = true;
                    }
                }
                break;
        }
    }

    private void snapToGrid(Point tilePoint) {
        this.worldX = tilePoint.x * Constants.TILE_SIZE;
        this.worldY = tilePoint.y * Constants.TILE_SIZE;
    }

    private void moveEntityStep(Point point) {
        int targetX = point.x * Constants.TILE_SIZE;
        int targetY = point.y * Constants.TILE_SIZE;

        int dx = targetX - this.worldX;
        int dy = targetY - this.worldY;

        if (dx != 0) {
            this.direction = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            moveEntityToTarget(targetX, this.worldY);
        } else if (dy != 0) {
            this.direction = dy > 0 ? Direction.DOWN : Direction.UP;
            moveEntityToTarget(this.worldX, targetY);
        }
    }

    private void moveEntityToTarget(int targetX, int targetY) {
        if (this.isDead == true) { return; }
        if (!this.collisionOn) {
            this.isMoving = true;
            if (this.worldX != targetX) {
                if (this.worldX < targetX) {
                    this.worldX += Math.min(speed, targetX - this.worldX);
                } else if (this.worldX > targetX) {
                    this.worldX -= Math.min(speed, this.worldX - targetX);
                }
            } else if (this.worldY != targetY) {
                if (this.worldY < targetY) {
                    this.worldY += Math.min(speed, targetY - this.worldY);
                } else if (this.worldY > targetY) {
                    this.worldY -= Math.min(speed, this.worldY - targetY);
                }
            }
        } else {
            this.isMoving = false;
        }
    }

    protected List<Point> bfsShortestPath(Point start, Point goal, boolean[][] walkable) {
        int width = this.gamePanel.tileManager.walkableTiles.length;
        int height = this.gamePanel.tileManager.walkableTiles[0].length;
        // Breadth-First Search
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        queue.add(start);
        cameFrom.put(start, null);

        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(goal)) {
                List<Point> path = new ArrayList<>();
                for (Point p = goal; p != null; p = cameFrom.get(p)) {
                    path.add(p);
                }
                Collections.reverse(path);
                return path;
            }
            for (int[] d : directions) {
                int nx = current.x + d[0];
                int ny = current.y + d[1];
                Point neighbor = new Point(nx, ny);
                if (nx >= 0 && ny >= 0 && nx < width && ny < height
                    && walkable[nx][ny]
                    && !cameFrom.containsKey(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        return null;
    }

    private Point getFrenzyLocation() {
        if (this.areaPoints != null && !this.areaPoints.isEmpty()) {
            return this.gamePanel.tileManager.getRandomTileLocationsWithinArea(this.areaPoints);
        }
        List<Point> points = getDefaultPoints();
        if (points != null && !points.isEmpty()) {
            return points.get(Utils.generateRandomInt(0, points.size() - 1));
        }
        TileLocation tileLocation = this.gamePanel.tileManager.getRandomTileLocation();
        return new Point(tileLocation.worldX, tileLocation.worldY);
    }

    protected List<Point> getDefaultPoints() {
        this.areaPoints = List.of(
            new Point(this.getLocation().x - DEFAULT_AREA_RADIUS, this.getLocation().x - DEFAULT_AREA_RADIUS),
            new Point(this.getLocation().x + DEFAULT_AREA_RADIUS, this.getLocation().x + DEFAULT_AREA_RADIUS)
        );
        return areaPoints;
    }

    protected void drawEffect(Graphics2D graphics2D) {
        if (this.effect != null) {
            if ((this.gamePanel.gameTime - this.effect.startTime) / Constants.MILLISECOND > 500) {
                this.effect = null;
            } else {
                this.effect.worldX = this.worldX;
                this.effect.worldY = this.worldY;
                this.effect.draw(graphics2D);
            }
        }
    }

    private Point getPushBackLocation(Entity entity) {
        int dx = entity.worldX - worldX;
        int dy = entity.worldY - worldY;
        List<Point> points = new ArrayList<Point>();
        if (dx != 0 && dy != 0) {
            int ix = dx / Math.abs(dx) * -1;
            int iy = dy / Math.abs(dy) * -1;
            for (int y = getRawY() - 1; y > 0 && y < Constants.MAX_SCREEN_ROW; y += iy) {
                for (int x = getRawX() - 1; x > 0 && x < Constants.MAX_SCREEN_COL; x += ix) {
                    if (this.gamePanel.tileManager.walkableTiles[x][y]) {
                        points.add(new Point(x, y));
                    }
                }
            }
        }
        if (points.size() > 0) {
            return getPushBackLocFromWalkable(points);
        }
        return getFrenzyLocation();
    }

    private Point getPushBackLocFromWalkable(List<Point> points) {
        this.frenzyTarget = points.get(Utils.generateRandomInt(0, points.size() - 1));
        if (!getValidFrenzyPath()) {
            points.remove(this.frenzyTarget);
            if (points.isEmpty()) {
                return getFrenzyLocation();
            }
            getPushBackLocFromWalkable(points);
        }
        return this.frenzyTarget;
    }

    private void startFrenzy(Point point) {
        if (point != null && !getLocation().equals(point)) {
            this.frenzyTarget = point;
        } else {
            this.frenzyTarget = getFrenzyLocation();
        }
        if (!getValidFrenzyPath()) startFrenzy(getFrenzyLocation());
        moveFrenziedEntity();
    }

    private boolean getValidFrenzyPath() {
        List<Point> path = bfsShortestPath(
            getLocation(),
            this.frenzyTarget,
            this.gamePanel.tileManager.walkableTiles
        );
        if (path != null && !path.isEmpty()) {
            path.removeFirst();
            this.moveQueue = new LinkedList<>(path);
            return true;
        } else {
            return false;
        }
    }

    private void moveFrenziedEntity() {
        if (!movable) {
            this.frenzyTarget = null;
            this.isFrenzy = false;
            return;
        }
        if (this.moveQueue != null && !this.moveQueue.isEmpty()) {
            this.isMoving = true;
            Point nextPoint = this.moveQueue.peek();
            moveEntityStep(nextPoint);

            int targetX = nextPoint.x * Constants.TILE_SIZE;
            int targetY = nextPoint.y * Constants.TILE_SIZE;

            if (Math.abs(this.worldX - targetX) <= speed && Math.abs(this.worldY - targetY) <= speed) {
                snapToGrid(nextPoint);
                this.moveQueue.poll();
            }
        }
    }

    private void backToStart() {
        List<Point> path = bfsShortestPath(
            getLocation(),
            new Point(this.startingX, this.startingY),
            this.gamePanel.tileManager.walkableTiles
        );
        if (path != null && !path.isEmpty()) {
            this.moveQueue = new LinkedList<>(path);
        }
    }

    private boolean isMoveQueueEmpty() {
        return (this.moveQueue == null || (this.moveQueue != null && this.moveQueue.isEmpty()));
    }

    public void setLocation(int x, int y) {
        this.worldX = x * Constants.TILE_SIZE;
        this.worldY = y * Constants.TILE_SIZE;
    }

    public void setLocation(Point point) {
        this.worldX = point.x * Constants.TILE_SIZE;
        this.worldY = point.y * Constants.TILE_SIZE;
    }

    public Point getLocation() {
        return new Point(this.worldX / Constants.TILE_SIZE, this.worldY / Constants.TILE_SIZE);
    }

    public void draw(Graphics2D graphics2D) {
        getSpiteImage();
        int screenX = this.worldX - this.gamePanel.player.worldX + this.gamePanel.player.screenX;
        int screenY = this.worldY - this.gamePanel.player.worldY + this.gamePanel.player.screenY;
        if (
            worldX + (Constants.TILE_SIZE) > (this.gamePanel.player.worldX - this.gamePanel.player.screenX) &&
            worldX - (Constants.TILE_SIZE) < (this.gamePanel.player.worldX + this.gamePanel.player.screenX) &&
            worldY + (Constants.TILE_SIZE) > (this.gamePanel.player.worldY - this.gamePanel.player.screenY) &&
            worldY - (Constants.TILE_SIZE) < (this.gamePanel.player.worldY + this.gamePanel.player.screenY)
        ){
            graphics2D.drawImage(
                this.sprite.image,
                screenX - this.sprite.xAdjust,
                screenY - this.sprite.yAdjust,
                null
            );
            drawDebugCollision(graphics2D, screenX, screenY);
        }
        drawEffect(graphics2D);
        drawHat(graphics2D);
    }

    protected void drawHat(Graphics2D graphics2D) {
        if (this.hat == null) { return; }
        if (this.isDead) { return; }
        int screenX = this.worldX - this.gamePanel.player.worldX + this.gamePanel.player.screenX;
        int screenY = this.worldY - this.gamePanel.player.worldY + this.gamePanel.player.screenY;
        if (
            worldX + (Constants.TILE_SIZE) > (this.gamePanel.player.worldX - this.gamePanel.player.screenX) &&
            worldX - (Constants.TILE_SIZE) < (this.gamePanel.player.worldX + this.gamePanel.player.screenX) &&
            worldY + (Constants.TILE_SIZE) > (this.gamePanel.player.worldY - this.gamePanel.player.screenY) &&
            worldY - (Constants.TILE_SIZE) < (this.gamePanel.player.worldY + this.gamePanel.player.screenY)
        ){
            graphics2D.drawImage(
                this.hat,
                screenX,
                screenY - 18,
                null
            );
        }
    }

    public void setIsReady() {
        this.defaults = new Defaults(this);
        this.isReady = true;
    }

    private void clearMoveQueue() {
        if (this.moveQueue != null) {
            this.moveQueue.clear();
        }
    }

    class Defaults {
        public boolean isAlerted;
        public boolean attacking;
        public boolean willChase;
        public boolean movable;
        public boolean isFriendly;
        public boolean warned;
        public boolean isMoving;
        public MoveStatus moveStatus;

        public Defaults(Entity entity) {
            this.isAlerted = entity.isAlerted;
            this.attacking = entity.attacking;
            this.willChase = entity.willChase;
            this.movable = entity.movable;
            this.isFriendly = entity.isFriendly;
            this.warned = entity.warned;
            this.isMoving = entity.isMoving;
            this.moveStatus = entity.moveStatus;
        }

        public void revertToDefault(Entity entity) {
            entity.isAlerted = this.isAlerted;
            entity.attacking = this.attacking;
            entity.willChase = this.willChase;
            entity.movable = this.movable;
            entity.isFriendly = this.isFriendly;
            entity.warned = this.warned;
            entity.isMoving = this.isMoving;
            entity.moveStatus = this.moveStatus;
        }

        public void printDefaults(Entity entity) {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            entity.gamePanel.config.printPrettyString(json);
        }

        public void printDefaults(Entity entity, String name) {
            if (entity.name == name) printDefaults(entity);
        }
    }
}
