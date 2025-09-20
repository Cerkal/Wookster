package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import objects.weapons.Weapon.WeaponType;

public class NPCGeneric extends Entity {

    public NPCGeneric(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.entityType = EntityType.NPC;
        this.name = "Droids";
        this.movable = false;
        this.isFriendly = true;
    }

    @Override
    protected void loadSprites() {
        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.OLD_MAN_UP_0));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.OLD_MAN_DOWN_0));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.OLD_MAN_LEFT_0));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.OLD_MAN_RIGHT_0));

        String m = SpriteAnimation.MOVE.name();
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_0));
        spriteManager.setSprite(m, new Sprite(Direction.UP, Constants.PLAYER_IMAGE_UP_1));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_0));
        spriteManager.setSprite(m, new Sprite(Direction.DOWN, Constants.PLAYER_IMAGE_DOWN_1));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_0));
        spriteManager.setSprite(m, new Sprite(Direction.LEFT, Constants.PLAYER_IMAGE_LEFT_1));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_0));
        spriteManager.setSprite(m, new Sprite(Direction.RIGHT, Constants.PLAYER_IMAGE_RIGHT_1));

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
