package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;

public class Animal extends Entity {

    public Animal(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.damageSound = Constants.SOUND_TROOPER_HURT;
        this.entityType = EntityType.ANIMAL;
        this.name = "Animal";
        this.willChase = true;
        this.isFriendly = false;
        this.isFrenzy = true;
    }

    @Override
    protected void loadSprites() {
        String m = SpriteAnimation.MOVE.name();
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PIG_IMAGE_UP_00));
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PIG_IMAGE_UP_01));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PIG_IMAGE_DOWN_00));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PIG_IMAGE_DOWN_01));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PIG_IMAGE_LEFT_00));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PIG_IMAGE_LEFT_01));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PIG_IMAGE_RIGHT_00));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PIG_IMAGE_RIGHT_01));

        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.PIG_IMAGE_UP_00));
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.PIG_IMAGE_UP_01));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.PIG_IMAGE_DOWN_00));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.PIG_IMAGE_DOWN_01));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.PIG_IMAGE_LEFT_00));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.PIG_IMAGE_LEFT_01));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.PIG_IMAGE_RIGHT_00));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.PIG_IMAGE_RIGHT_01));
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PIG_IMAGE_DEAD));
    }
}
