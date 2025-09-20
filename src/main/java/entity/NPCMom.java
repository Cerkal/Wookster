package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.WeaponType;

public class NPCMom extends Entity {

    public NPCMom(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 3;
        this.damageSound = Constants.SOUND_HURT;
        this.entityType = EntityType.NPC;
        this.name = "Mom";
        this.willChase = false;
        this.isFriendly = true;
        this.isNeeded = true;
        this.primaryWeapon = null;
        setHat(Constants.WOOKSER_MOM_HAT);
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

        String f = WeaponType.FIST.name();
        spriteManager.setSprite(f, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(f, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_1));
        spriteManager.setSprite(f, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(f, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_1));
        spriteManager.setSprite(f, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(f, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_1));
        spriteManager.setSprite(f, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));
        spriteManager.setSprite(f, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_1));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PLAYER_IMAGE_DEAD));
    }
}
