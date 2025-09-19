package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import main.GamePanel.GameState;
import objects.weapons.CrossbowWeapon;
import objects.weapons.Weapon.WeaponType;

public class NPCVendor extends Entity {

    public NPCVendor(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 3;
        this.damageSound = Constants.SOUND_HURT;
        this.entityType = EntityType.NPC;
        this.name = "Vendor";
        this.isFriendly = true;
        this.isNeeded = false;
        this.aggression = 75;
        this.weapon = new CrossbowWeapon(gamePanel, this);
        setHat(Constants.WOOKSER_BUCKET_HAT);
    }

    @Override
    public void postDialogAction() {
        this.gamePanel.gameState = GameState.VENDOR;
    }

    @Override
    protected void loadSprites() {
        String m = SpriteAnimation.MOVE.name();
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_1));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_1));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_1));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_1));

        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));

        String crossbow = WeaponType.CROSSBOW.name();
        spriteManager.setSprite(crossbow, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_1));
        spriteManager.setSprite(crossbow, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_0));
        spriteManager.setSprite(crossbow, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_1));

        String fist = WeaponType.FIST.name();
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_0));
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_CROSSBOW_UP_1));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_0));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_CROSSBOW_DOWN_1));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_0));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_CROSSBOW_LEFT_1));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_0));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_CROSSBOW_RIGHT_1));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PLAYER_IMAGE_DEAD));
    }
}
