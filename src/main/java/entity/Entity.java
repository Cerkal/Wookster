package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import effects.AlertEffect;
import effects.BloodEffect;
import effects.Effect;
import entity.SpriteManager.Sprite;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import main.Utils;
import main.GamePanel.GameState;
import objects.weapons.FistWeapon;
import objects.weapons.MeleeWeapon;
import objects.weapons.Weapon;
import objects.weapons.Weapon.WeaponType;

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

    // Location
    public int worldX, worldY, startingX, startingY;
    public int speed, defaultSpeed;
    public int runSpeed = 4;
    public Direction direction, startingDirection;
    public MoveStatus moveStatus = MoveStatus.IDLE;
    
    // Debug
    public boolean debugEntity;

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
    int followCollisionCounter;
    public List<Point> areaPoints = new ArrayList<>();
    public boolean pushback = true;
    public Entity predictiveCollision;
    public Entity predictiveCollisionSelf;

    // Alert
    public boolean isFriendly;
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
    Defaults defaults;

    // Pathfinding
    protected List<Point> currentPath = new ArrayList<>();
    protected int pathIndex = 0;

    // Timeouts
    protected long stateExpireTime = 0;

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
    public int priceModifier = 1;


    // MOVEMENT STATES

    public enum MoveStatus {
        IDLE {
            @Override
            void update(Entity e) {
                // Idle means do nothing
            }

            @Override
            void onEnter(Entity e) {
                e.setPath(null); // clear path
            }
        },
        WANDER {
            @Override
            void update(Entity e) {
                if (refreshPath(e)) {
                    e.speed = e.defaultSpeed / 2;
                    Point wanderTarget = e.gamePanel.pathfinder.randomFrenzyPointWithinAreaSquare(e.areaPoints);
                    e.setPath(e.gamePanel.pathfinder.findPath(e.getLocation(), wanderTarget));
                }
            }
        },
        CHASING {
            @Override
            void update(Entity e) {
                if (refreshPath(e)) {
                    e.speed = e.defaultSpeed;
                    Entity target = e.attackingTarget;
                    if (target != null) {
                        e.printDebugData("Setting path from chase from move status update");
                        e.setPath(e.gamePanel.pathfinder.findPath(e.getLocation(), target.getLocation()));
                    }
                }
            }
        },
        FOLLOW {
            @Override
            void update(Entity e) {
                if (refreshPath(e)) {
                    e.speed = e.defaultSpeed;
                    Entity leader = e.gamePanel.getPlayer();
                    e.setPath(e.gamePanel.pathfinder.findPath(e.getLocation(), leader.getLocation()));
                }
            }
        },
        FRENZY {
            @Override
            void update(Entity e) {
                if (refreshPath(e)) {
                    e.speed = e.defaultSpeed;
                    Point frenzyTarget = e.gamePanel.pathfinder.randomFrenzyPointWithinAreaSquare(e.areaPoints);
                    e.setPath(e.gamePanel.pathfinder.findPath(e.getLocation(), frenzyTarget));
                }
            }
        };

        abstract void update(Entity e);
        void onEnter(Entity e) {} // optional override
    }

    private static boolean refreshPath(Entity entity) {
        return entity.pathIndex >= entity.currentPath.size() &&
        entity.collisionPlayer == null &&
        entity.collisionEntity == null;
    }

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.solidArea = new Rectangle();
        this.solidArea.x = SOLID_AREA_X;
        this.solidArea.y = SOLID_AREA_Y;
        this.solidArea.width = SOLID_AREA_WIDTH;
        this.solidArea.height = SOLID_AREA_HEIGHT;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.direction = Direction.DOWN;
        this.attackingTimeout = DEFAULT_TIMEOUT;
        this.moveStatus = MoveStatus.IDLE;
        this.speed = this.defaultSpeed;
        this.weapons.put(WeaponType.FIST, new FistWeapon(this.gamePanel, this));
        this.loadSprites();
        this.setPredictiveEntity();
    }

    public Entity(GamePanel gamePanel, int worldX, int worldY) {
        this(gamePanel);
        this.startingX = worldX;
        this.startingY = worldY;
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
        this.isMoving = false;
    }

    public void setIsReady() {
        this.defaults = new Defaults(this);
        this.isReady = true;
    }

    public void update() {
        if (isDead || !isReady) return;
        collision();
        checkVisibility();
        checkLineOfFire();
        moveStatus.update(this);
        handleMovement();
        checkTimeout();
    }

    private void collision() {
        if (this.moveStatus == MoveStatus.IDLE) return;
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        this.gamePanel.collision.objectCollision(this, false);

        collisionEntity = this.gamePanel.collision.entityCollision(this);
        collisionPlayer = this.gamePanel.collision.getCollidEntity(this, this.gamePanel.player);

        boolean collidedWithEntity = collisionEntity != null;
        boolean collidedWithPlayer = collisionPlayer != null;

        if (collidedWithPlayer || collidedWithEntity) {
            // handlePlayerCollision();
        } else {
            if (this.primaryWeapon != null) this.weapon = this.primaryWeapon;
        }

        if ((collidedWithEntity || collidedWithPlayer)) {
            // TODO: FIX
            if (this.moveStatus == MoveStatus.FOLLOW && collidedWithPlayer) {
                followCollisionCounter++;
                if (followCollisionCounter > 150) {
                    this.changeState(MoveStatus.FRENZY);
                    Point pushBack = this.gamePanel.pathfinder.getPushBackLocationFollow(this, this.gamePanel.player);
                    printDebugData("Pushing back to: " + pushBack);
                    actionTimeout(1000);
                    setPathPoint(pushBack);
                    followCollisionCounter = 0;
                }
            } else {
                if (collisionCounter == 0) {
                    Entity collisionEntity = collidedWithPlayer ? this.gamePanel.player : this.collisionEntity;
                    printDebugData("Setting path from collision");
                    setPathPoint(getPathLocation(collisionEntity));
                    collisionCounter = this.speed;
                }
            }
        }

        if (collisionCounter > 0) {
            collisionCounter--;
        }
    }

    private void checkVisibility() {
        if (this.moveStatus == MoveStatus.FOLLOW) { return; }
        if (this.isFriendly || this.moveStatus == MoveStatus.FRENZY) { return; }

        List<Entity> entities = new ArrayList<>(this.gamePanel.npcs);
        entities.add(this.gamePanel.player);

        for (Entity entity : entities) {
            if (this.getClass() == entity.getClass() || this == entity) continue;
            boolean target = this.gamePanel.collision.checkLineTileCollision(entity, this);
            
            if (this.attackingTarget == null && target && entity.isDead == false) {
                this.attackingTarget = entity;
                clearPath();
                actionTimeout();
                changeState(MoveStatus.CHASING);
                printDebugData(this.name + " attacking: " + this.attackingTarget + " going to chase");
            }

            if (this.attackingTarget == entity && entity.isDead) {
                this.attackingTarget = null;
            } 
        }
        return;
    }

    private void checkLineOfFire() {
        if (
            this.isFriendly ||
            this.primaryWeapon == null ||
            this.primaryWeapon instanceof MeleeWeapon ||
            this.attackingTarget == null
        ){
            return;
        }

        int buffer = 1;
        int locBuffer = Constants.TILE_SIZE;
        int entityX = getLocation().x;
        int entityY = getLocation().y;
        int attackingX = this.attackingTarget.getLocation().x;
        int attackingY = this.attackingTarget.getLocation().y;

        boolean inLine = false;
        boolean closeEnough = false;
        this.movable = true;

        switch (this.direction) {
            case UP:
                inLine = entityY > attackingY && Math.abs(entityX - attackingX) <= buffer;
                closeEnough = Math.abs(entityY - attackingY) < 5 && Math.abs(this.worldX - this.attackingTarget.worldX) <= locBuffer;
                break;
            case DOWN:
                inLine = entityY < attackingY && Math.abs(entityX - attackingX) <= buffer;
                closeEnough = Math.abs(entityY - attackingY) < 5 && Math.abs(this.worldX - this.attackingTarget.worldX) <= locBuffer;
                break;
            case RIGHT:
                inLine = entityX < attackingX && Math.abs(entityY - attackingY) <= buffer;
                closeEnough = Math.abs(entityX - attackingX) < 5 && Math.abs(this.worldY - this.attackingTarget.worldY) <= locBuffer;
                break;
            case LEFT:
                inLine = entityX > attackingX && Math.abs(entityY - attackingY) <= buffer;
                closeEnough = Math.abs(entityX - attackingX) < 5 && Math.abs(this.worldY - this.attackingTarget.worldY) <= locBuffer;
                break;
        }

        boolean canSeeTarget = this.gamePanel.collision.checkLineTileCollision(this.attackingTarget, this);

        if (inLine && canSeeTarget) {
            this.primaryWeapon.shoot(this);
        }
        if (closeEnough && canSeeTarget) {
            this.movable = false;
            this.isMoving = false;
            this.attacking = true;
        }
    }

    // MOVEMENT METHODS

    protected void handleMovement() {
        try {
            if (currentPath == null || pathIndex >= currentPath.size()) return;
            moveEntityStep(currentPath.get(pathIndex));

            // reached this step?
            Point step = currentPath.get(pathIndex);
            if (
                Math.abs(this.worldX - step.x * Constants.TILE_SIZE) <= this.speed &&
                Math.abs(this.worldY - step.y * Constants.TILE_SIZE) <= this.speed
            ) {
                Point nextPoint = this.currentPath.get(pathIndex);
                snapToGrid(nextPoint);
                pathIndex++;
            }
        } catch (Exception e) {
            // Updated while running, thrown exception. We ignore.
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
            if (predictiveCollision(targetX, this.worldY)) {
                setPathPoint(getPathLocation(this.attackingTarget));
                printDebugData("Setting path from move predictive");
            } else {
                moveEntityToTarget(this, targetX, this.worldY);
            }
        } else if (dy != 0) {
            this.direction = dy > 0 ? Direction.DOWN : Direction.UP;
            if (predictiveCollision(this.worldX, targetY)) {
                setPathPoint(getPathLocation(this.attackingTarget));
                printDebugData("Setting path from move predictive");
            } else {
                moveEntityToTarget(this, this.worldX, targetY);
            }
        }
    }

    protected boolean predictiveCollision(int targetX, int targetY) {
        if (this.isDead == true) { return false; }
        if (this.moveStatus == MoveStatus.FOLLOW) { return false; }
        this.predictiveCollision.updatePredictive(this);
        this.predictiveCollision.speed = 10;
        moveEntityToTarget(this.predictiveCollision, targetX, targetY);
        boolean willCollideWithTile = this.gamePanel.collision.checkTile(this.predictiveCollision);
        if (willCollideWithTile || this.collisionOn) {
            return true;
        }
        boolean willCollideWithEntity = this.gamePanel.collision.entityCollision(this.predictiveCollision) != null;
        boolean willCollideWithPlayer = this.gamePanel.collision.getCollidEntity(this.predictiveCollision, this.gamePanel.getPlayer()) != null;
        return willCollideWithEntity || willCollideWithPlayer;
    }
    
    protected void moveEntityToTarget(Entity entity, int targetX, int targetY) {
        if (entity.isDead == true || this.movable == false) { return; }
        if (!this.collisionOn) {
            entity.isMoving = true;
            if (entity.worldX != targetX) {
                if (entity.worldX < targetX) {
                    entity.worldX += Math.min(speed, targetX - entity.worldX);
                } else if (entity.worldX > targetX) {
                    entity.worldX -= Math.min(speed, entity.worldX - targetX);
                }
            } else if (entity.worldY != targetY) {
                if (entity.worldY < targetY) {
                    entity.worldY += Math.min(speed, targetY - entity.worldY);
                } else if (entity.worldY > targetY) {
                    entity.worldY -= Math.min(speed, entity.worldY - targetY);
                }
            }
        }
    }

    private void checkTimeout() {
        if (
            this.gamePanel.gameTime / Constants.MILLISECOND > this.stateExpireTime &&
            this.defaults != null &&
            this.defaults.moveStatus != this.moveStatus
        ){
            printDebugData("Entity: " + this.name + " going from " + this.moveStatus + " back to " + this.defaults.moveStatus);
            clearPath();
            this.defaults.revertToDefault(this);
        }
    }

    public void changeState(MoveStatus newState) {
        if (this.moveStatus == newState) return;
        this.moveStatus = newState;
        this.moveStatus.onEnter(this);
    }

    public void setDefaultState(MoveStatus moveStatus) {
        this.moveStatus = moveStatus;
        this.defaults = new Defaults(this);
        this.moveStatus.onEnter(this);
    }

    public void setPath(List<Point> path) {
        this.currentPath = path != null ? path : new ArrayList<>();
        this.pathIndex = 0;
    }

    public void setPathPoint(Point point) {
        setPath(this.gamePanel.pathfinder.findPath(this.getLocation(), point));
    }

    public void setArea(List<Point> points) {
        this.areaPoints = points;
    }

    public void clearPath() {
        this.currentPath.clear();
        this.pathIndex = 0;
    }

    private boolean isBackAtStart() {
        if (this.moveQueue.isEmpty() && this.defaults.moveStatus == MoveStatus.IDLE) {
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
        actionTimeout(this.attackingTimeout);
    }

    private void actionTimeout(int timeout) {
        long gameTimeMS = this.gamePanel.gameTime / Constants.MILLISECOND;
        this.stateExpireTime = gameTimeMS + timeout;
        printDebugData("Action started at : " + gameTimeMS + " will expire at: " + this.stateExpireTime);
    }

    public void handlePlayerCollision() {
        if (this.isFriendly || this.moveStatus != MoveStatus.CHASING) { return; }
        if (this.weapons != null && this.weapons.containsKey(WeaponType.FIST)) {
            this.weapon = this.weapons.get(WeaponType.FIST);
            this.weapon.shoot(this);
        }
    }

    public int getRawX() {
        return this.worldX / Constants.TILE_SIZE;
    }

    public int getRawY() {
        return this.worldY / Constants.TILE_SIZE;
    }

    protected Point getPathLocation(Entity entity) {
        Point point = null;
        switch (this.moveStatus) {
            case FOLLOW:
                printDebugData("getPathLocation: follow");
                point = this.gamePanel.getPlayer().getLocation();
                break;
            case WANDER:
                printDebugData("getPathLocation: wander");
                point = this.gamePanel.pathfinder.randomFrenzyPointWithinAreaSquare(this.areaPoints);
                break;
            case CHASING:
                if (this.attackingTarget != null && entity != this.attackingTarget) {
                    printDebugData("getPathLocation: chasing / attacking is not null");
                    point = this.attackingTarget.getLocation();
                } else {
                    printDebugData("getPathLocation: chasing / attacking is null or not attacker so defaulting");
                    point = this.gamePanel.pathfinder.randomFrenzyPoint();
                }
                break;
            default:
                point = this.gamePanel.pathfinder.randomFrenzyPoint();
                printDebugData("getPathLocation: default");
                break;
        }
        return point;
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

    // DIALOGUE METHODS

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

    public void setDialogue(String[] lines) {
        this.dialogue = lines;
    }

    // DAMAGE METHODS

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
        if (this instanceof Player) { return; }
        if (this.getClass() == attacker.getClass()) { return; }

        printDebugData(this.entityType + ": " + getCurrentHealth());

        if (
            (attacker instanceof Player && this.warned) ||
            (attacker instanceof Player == false)
        ){
            if (this.aggression >= 50) {
                this.attackingTarget = attacker;
                this.isFriendly = false;
                this.movable = true;
                actionTimeout();
                changeState(MoveStatus.CHASING);
            } else {
                this.attackingTarget = null;
                actionTimeout();
                changeState(MoveStatus.FRENZY);
            }
        }
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

    // DRAW METHODS

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

    protected void drawDebugCollision(Graphics2D graphics2D, int screenX, int screenY) {
        if (!this.gamePanel.debugCollision) { return; }
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(
            screenX + solidArea.x,
            screenY + solidArea.y,
            solidArea.width,
            solidArea.height
        );
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("(" + this.worldX + ", " + this.worldY + ")", screenX, screenY - 30);
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

    private void printDebugData(String message) {
        if (this.debugEntity) {
            System.out.println(message);
        }
    }

    // VENDOR METHODS

    public void setVendor(List<InventoryItem> items) {
        this.isVendor = true;
        for (InventoryItem item : items) {
            this.inventory.put(item.name, new ArrayList<>(List.of(item)));
        }
    }

    public boolean isVendor() {
        return this.isVendor;
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

    // SPRITE METHODS

    protected void getSpiteImage() {
        this.sprite = this.spriteManager.getSprite(this);
    }

    protected abstract void loadSprites();

    public void setHat(String hatPath) {
        if (hatPath == null) { return; }
        try {
            this.hat = ImageIO.read(getClass().getResourceAsStream(hatPath));
            this.hat = Utils.scaleImage(this.hat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // PREDICTIVE METHODS

    private void setPredictiveEntity() {
        this.predictiveCollision = new Entity(this) {
            @Override
            protected void loadSprites() {}
        };
    }

    public Entity(Entity predictiveEntity) {
        this.predictiveCollisionSelf = predictiveEntity;
        updatePredictive(predictiveEntity);
    }

    protected void updatePredictive(Entity predictiveEntity) {
        this.worldX = predictiveEntity.worldX;
        this.worldY = predictiveEntity.worldY;
        this.speed = predictiveEntity.speed;
        this.solidArea = predictiveEntity.solidArea;
        this.solidAreaDefaultX = predictiveEntity.solidArea.x;
        this.solidAreaDefaultY = predictiveEntity.solidArea.y;
        this.direction = predictiveEntity.direction;
    }

    class Defaults {
        public boolean attacking;
        public boolean movable;
        public boolean isFriendly;
        public boolean warned;
        public boolean isMoving;
        public MoveStatus moveStatus;
        private Entity attackingTarget;

        public Defaults(Entity entity) {
            this.attacking = entity.attacking;
            this.movable = entity.movable;
            this.isFriendly = entity.isFriendly;
            this.warned = entity.warned;
            this.isMoving = entity.isMoving;
            this.moveStatus = entity.moveStatus;
            this.attackingTarget = entity.attackingTarget;
        }

        public void revertToDefault(Entity entity) {
            entity.attacking = this.attacking;
            entity.movable = this.movable;
            entity.isFriendly = this.isFriendly;
            entity.warned = this.warned;
            entity.isMoving = this.isMoving;
            entity.attackingTarget = this.attackingTarget;
            entity.changeState(this.moveStatus);
        }
    }

}
