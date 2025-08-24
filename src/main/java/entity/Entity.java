package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import effects.AlertEffect;
import effects.BloodEffect;
import effects.Effect;
import entity.SpriteManager.Sprite;
import main.Constants;
import main.GamePanel;
import main.Utils;
import objects.weapons.MeleeWeapon;
import objects.weapons.Weapon;
import tile.TileManager.TileLocation;

public abstract class Entity {

    GamePanel gamePanel;

    public static final int SOLID_AREA_X = 10;
    public static final int SOLID_AREA_Y = 4;
    public static final int SOLID_AREA_WIDTH = 28;
    public static final int SOLID_AREA_HEIGHT = 40;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum EntityType { PLAYER, NPC, ENEMY, ANIMAL }

    // Location
    public int worldX, worldY;
    public int speed;
    public Direction direction;

    // Sprite
    protected int spriteCounter = 0;
    protected int spriteNumber = 0;
    protected boolean isMoving = false;
    protected Sprite sprite;
    SpriteManager spriteManager = new SpriteManager();

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public boolean movable = true;
    public boolean invincable = false;
    public int invincableCounter;
    public boolean isDead;
    Entity collisionEntity = null;
    Entity collisionPlayer = null;
    int collisionCounter;

    // Alert
    public boolean isFriendly;
    public boolean willChase;
    private boolean isAlerted;
    protected boolean isChasing;
    protected Queue<Point> moveQueue;
    public boolean isNeeded;
    public boolean isFrenzy;

    // Entity Values
    public EntityType entityType;
    public String name = "";
    public int maxHealth = 100;
    public int health = 100;
    public String[] dialogue;
    public int dialogueIndex = 0;
    public Effect effect;
    public Weapon weapon;
    public boolean attacking = false;

    // Sounds
    protected String damageSound;

    protected Point frenzyTarget = null;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.solidArea = new Rectangle();
        this.solidArea.x = SOLID_AREA_X;
        this.solidArea.y = SOLID_AREA_Y;
        this.solidArea.width = SOLID_AREA_WIDTH;
        this.solidArea.height = SOLID_AREA_HEIGHT;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.loadSprites();
    }

    public Entity(GamePanel gamePanel, int worldX, int worldY) {
        this(gamePanel);
        this.worldX = worldX * Constants.TILE_SIZE;
        this.worldY = worldY * Constants.TILE_SIZE;
        this.isMoving = false;
    }

    public void setLocation(int x, int y) {
        this.worldX = x * Constants.TILE_SIZE;
        this.worldY = y * Constants.TILE_SIZE;
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
                this.sprite.width,
                this.sprite.height,
                null
            );
            drawDebugCollision(graphics2D, screenX, screenY);
        }
        drawEffect(graphics2D);
    }

    public void update() {
        if (this.isDead) { return; }
        setAction();
        collision();
        if (isFrenzy) {
            startFrenzy();
        } else {
            startChase();
        }
    }

    public void checkPlayerCollision() {
        Entity entity = this.gamePanel.collision.getCollidEntity(this, this.gamePanel.player);
        if (entity != null) {
            handlePlayerCollision(this.gamePanel.player);
        }
    }

    public void handlePlayerCollision(Player player) {
        if (this.isFriendly) { return; }
        if (this.weapon instanceof MeleeWeapon) {
            MeleeWeapon meleeWeapon = (MeleeWeapon) this.weapon;
            meleeWeapon.shoot(this);
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

    public void takeDamage(int amount) {
        if (this.invincable) { return; }
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;
            this.movable = false;
            this.weapon = null;
        }
        this.gamePanel.playSoundEffect(this.damageSound);
        this.gamePanel.effects.add(new BloodEffect(this.gamePanel, this.worldX, this.worldY));
        this.effect = new AlertEffect(this.gamePanel, this);
        this.isAlerted = true;
        System.out.println(this.entityType + ": " + getCurrentHealth());
        if (this instanceof Animal) {
            this.isFrenzy = true;
            this.frenzyTarget = null;
            if (this.moveQueue != null) {
                this.moveQueue.clear();
                startFrenzy();
            }
        }
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
            takeDamage((int) Math.abs(amount));
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

    protected void getSpiteImage() {
        this.sprite = this.spriteManager.getSprite(this);
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

    private void collision() {
        this.collisionOn = false;
        this.gamePanel.collision.checkTile(this);
        this.gamePanel.collision.objectCollision(this, false);
        checkVisibility();

        collisionEntity = this.gamePanel.collision.entityCollision(this);
        collisionPlayer = this.gamePanel.collision.getCollidEntity(this, this.gamePanel.player);

        boolean collidedWithEntity = collisionEntity != null;
        boolean collidedWithPlayer = collisionPlayer != null;
        
        if (collidedWithPlayer) {
            handlePlayerCollision(this.gamePanel.player);
        }

        if (isFrenzy && (collidedWithEntity || collidedWithPlayer)) {
            collisionCounter++;
            if (collisionCounter > 20) {
                switch (this.direction) {
                    case UP:
                        this.frenzyTarget = new Point(getRawX(), getRawY() + 1);
                        break;
                    case DOWN:
                        this.frenzyTarget = new Point(getRawX(), getRawY() - 1);
                        break;
                    case LEFT:
                        this.frenzyTarget = new Point(getRawX() + 1, getRawY());
                        break;
                    case RIGHT:
                        this.frenzyTarget = new Point(getRawX() - 1, getRawY());
                        break;
                }
                startFrenzy(this.frenzyTarget);
                collisionCounter = 0;
            }
        }
    }

    private void checkVisibility() {
        boolean canSeePlayer = this.gamePanel.collision.checkLineTileCollision(gamePanel.player, this);
        if (canSeePlayer) {
            this.isAlerted = true;
        }
    }

    private void setAction() {
        if (!this.movable) { return; }
        if (this.isAlerted && this.willChase && !this.isChasing && !this.isFrenzy) {
            queueChase();
        }
    }

    protected void queueChase() {
        List<Point> path = bfsShortestPath(
            getLocation(),
            this.gamePanel.player.getLocation(),
            this.gamePanel.tileManager.walkableTiles
        );
        getPath(path);
    }

    private void getPath(List<Point> path) {
        if (path != null) {
            this.moveQueue = new LinkedList<>();
            for (Point point : path) {
                this.moveQueue.add(point);
            }
            this.isChasing = true;
        }
    }

    private void startChase() {
        if (!this.isChasing) { return; }

        Point point = this.moveQueue.peek();
        if (point == null) { return; }

        this.isMoving = true;

        moveEntityStep(point);
        checkLineOfFire();

        int targetX = point.x * Constants.TILE_SIZE;
        int targetY = point.y * Constants.TILE_SIZE;

        if (Math.abs(this.worldX - targetX) <= speed && Math.abs(this.worldY - targetY) <= speed) {
            snapToGrid(point);
            this.moveQueue.poll();
        }

        if (this.moveQueue.isEmpty()) {
            stopChase();
        }
    }

    private void stopChase() {
        this.isMoving = false;
        this.isChasing = false;
        if (this.moveQueue != null) { this.moveQueue.clear(); }
    }

    private void checkLineOfFire() {
        if (this.isFriendly) { return; }
        if (this.weapon == null) { return; }
        if (this.weapon instanceof MeleeWeapon) { return;}
        
        int buffer = 1;
        int entityX = getLocation().x;
        int entityY = getLocation().y;
        int playerX = this.gamePanel.player.getLocation().x;
        int playerY = this.gamePanel.player.getLocation().y;

        switch (this.direction) {
            case UP:
            case DOWN:
                if (entityX >= playerX - buffer && entityX <= playerX + buffer) {
                    this.weapon.shoot(this);
                }
                break;
            case RIGHT:
            case LEFT:
                if (entityY >= playerY - buffer && entityY <= playerY + buffer) {
                    this.weapon.shoot(this);
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
        }
        sprite();
    }

    private void sprite() {
        this.spriteCounter++;
        if (this.spriteCounter > Constants.FPS / 6 && this.isMoving) {
            if (this.spriteNumber == 1) {
                this.spriteNumber = 0;
            } else if (this.spriteNumber == 0) {
                this.spriteNumber = 1;
            }
            this.spriteCounter = 0;
        }
    }

    protected void moveEntiy() {
        if (!this.collisionOn) {
            switch (this.direction) {
                case UP:
                    this.worldY -= speed;
                    break;
                case DOWN:
                    this.worldY += speed;
                    break;
                case LEFT:
                    this.worldX -= speed;
                    break;
                case RIGHT:
                    this.worldX += speed;
                    break;
            }
        }
        sprite();
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
        TileLocation tileLocation = this.gamePanel.tileManager.getRandomTileLocation();
        return new Point(tileLocation.worldX, tileLocation.worldY);
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

    private void startFrenzy() {
        if (this.frenzyTarget == null) {
            this.frenzyTarget = getFrenzyLocation();
            List<Point> path = bfsShortestPath(
                getLocation(),
                this.frenzyTarget,
                this.gamePanel.tileManager.walkableTiles
            );
            if (path != null) {
                this.moveQueue = new LinkedList<>(path);
            } else {
                this.frenzyTarget = null;
                startFrenzy();
            }
        }

        if (getLocation().equals(this.frenzyTarget)) {
            this.frenzyTarget = getFrenzyLocation();
            List<Point> path = bfsShortestPath(
                getLocation(),
                this.frenzyTarget,
                this.gamePanel.tileManager.walkableTiles
            );
            if (path != null) {
                this.moveQueue = new LinkedList<>(path);
            } else {
                this.frenzyTarget = null;
                startFrenzy();
            }
        }

        if (this.moveQueue != null && !this.moveQueue.isEmpty()) {
            Point nextPoint = this.moveQueue.peek();
            moveEntityStep(nextPoint);

            int targetX = nextPoint.x * Constants.TILE_SIZE;
            int targetY = nextPoint.y * Constants.TILE_SIZE;

            if (Math.abs(this.worldX - targetX) <= speed && Math.abs(this.worldY - targetY) <= speed) {
                snapToGrid(nextPoint);
                this.moveQueue.poll();
            }
        }

        this.isMoving = true;
        sprite();
    }

    private void startFrenzy(Point point) {
        if (point != null && !getLocation().equals(point)) {
            this.frenzyTarget = point;
        } else {
            this.frenzyTarget = getFrenzyLocation();
        }

        List<Point> path = bfsShortestPath(
            getLocation(),
            this.frenzyTarget,
            this.gamePanel.tileManager.walkableTiles
        );
        if (path != null && !path.isEmpty()) {
            path.removeFirst();
            this.moveQueue = new LinkedList<>(path);
        } else {
            startFrenzy(getFrenzyLocation());
        }

        if (this.moveQueue != null && !this.moveQueue.isEmpty()) {
            Point nextPoint = this.moveQueue.peek();
            moveEntityStep(nextPoint);

            int targetX = nextPoint.x * Constants.TILE_SIZE;
            int targetY = nextPoint.y * Constants.TILE_SIZE;

            if (Math.abs(this.worldX - targetX) <= speed && Math.abs(this.worldY - targetY) <= speed) {
                snapToGrid(nextPoint);
                this.moveQueue.poll();
            }
        }

        this.isMoving = true;
        sprite();
    }
}
