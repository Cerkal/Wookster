package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import objects.weapons.BlasterWeapon;
import objects.weapons.Weapon.WeaponType;

public class NPCTrooper extends Entity {

    final static int DEFAULT_ATTACK_TIMEOUT = 20000;

    public NPCTrooper(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.damageSound = Constants.SOUND_TROOPER_HURT;
        this.entityType = EntityType.ENEMY;
        this.name = "Trooper";
        this.willChase = true;
        this.attackingTimeout = DEFAULT_ATTACK_TIMEOUT;
        this.weapons.put(WeaponType.BLASTER, new BlasterWeapon(gamePanel, this));
        this.isFrenzy = false;
        this.primaryWeapon = new BlasterWeapon(gamePanel, this);
        this.aggression = 50;
        this.accuracy = 10;
    }

    @Override
    protected void loadSprites() {
        String m = SpriteAnimation.MOVE.name();
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_UP_0));
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_UP_1));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_DOWN_0));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_DOWN_1));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_LEFT_0));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_LEFT_1));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_RIGHT_0));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_RIGHT_1));

        String b = WeaponType.BLASTER.name();
        spriteManager.setSprite(b, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_UP_0));
        spriteManager.setSprite(b, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_UP_1));
        spriteManager.setSprite(b, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_DOWN_0));
        spriteManager.setSprite(b, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_DOWN_1));
        spriteManager.setSprite(b, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_LEFT_0));
        spriteManager.setSprite(b, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_LEFT_1));
        spriteManager.setSprite(b, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_RIGHT_0));
        spriteManager.setSprite(b, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_RIGHT_1));

        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_IDEL_UP_00));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_IDEL_DOWN_00, 250));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_IDEL_DOWN_01, 50));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_IDEL_LEFT_00));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_IDEL_RIGHT_00));

        String fist = WeaponType.FIST.name();
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_FIST_UP_00));
        spriteManager.setSprite(fist, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_FIST_UP_01));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_FIST_DOWN_00));
        spriteManager.setSprite(fist, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_FIST_DOWN_01));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_FIST_LEFT_00));
        spriteManager.setSprite(fist, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_FIST_LEFT_01));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_FIST_RIGHT_00));
        spriteManager.setSprite(fist, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_FIST_RIGHT_01));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.TROOPER_IMAGE_DEAD));
    }
}
