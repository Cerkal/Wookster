package entity;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;

public class NPCOld extends Entity {

    public NPCOld(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.entityType = EntityType.NPC;
        this.name = "Old Man";
        this.movable = false;
        this.isFriendly = true;
        this.invincable = true;
    }

    @Override
    protected void loadSprites() {
        String i = SpriteAnimation.IDEL.name();
        spriteManager.setSprite(i, new Sprite(Direction.UP, Constants.OLD_MAN_UP_0));
        spriteManager.setSprite(i, new Sprite(Direction.DOWN, Constants.OLD_MAN_DOWN_0));
        spriteManager.setSprite(i, new Sprite(Direction.LEFT, Constants.OLD_MAN_LEFT_0));
        spriteManager.setSprite(i, new Sprite(Direction.RIGHT, Constants.OLD_MAN_RIGHT_0));

        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PLAYER_IMAGE_DEAD));
    }
}
