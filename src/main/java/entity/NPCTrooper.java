package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;

public class NPCTrooper extends Entity {

    public NPCTrooper(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.damageSound = Constants.SOUND_TROOPER_HURT;
        this.entityType = EntityType.ENEMY;
        this.name = "Trooper";
        this.willChase = true;
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

        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.TROOPER_IMAGE_IDEL_01));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_IDEL_01, 250));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.TROOPER_IMAGE_IDEL_02, 50));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.TROOPER_IMAGE_IDEL_01));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.TROOPER_IMAGE_IDEL_01));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.TROOPER_IMAGE_DEAD));
    }
}
