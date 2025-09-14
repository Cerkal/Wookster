package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.SpriteManager.Sprite;
import entity.SpriteManager.SpriteAnimation;
import main.Constants;
import main.GamePanel;
import main.Utils;
import main.GamePanel.GameState;

public class NPCVendor extends Entity {

    private BufferedImage hat;

    public NPCVendor(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 3;
        this.damageSound = Constants.SOUND_HURT;
        this.entityType = EntityType.NPC;
        this.name = "Vendor";
        this.isFriendly = true;
        this.isNeeded = false;
        setHat();
    }

    private void setHat() {
        try {
            this.hat = ImageIO.read(getClass().getResourceAsStream(Constants.WOOKSER_MOM_HAT));
            this.hat = Utils.scaleImage(this.hat);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        
        spriteManager.setSprite(SpriteAnimation.DEAD.name(), new Sprite(null, Constants.PLAYER_IMAGE_DEAD));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        drawHat(graphics2D);
    }

    protected void drawHat(Graphics2D graphics2D) {
        if (this.isDead && this.isNeeded) {
            this.gamePanel.gameState = GameState.DEATH;
            return;
        }
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
}
