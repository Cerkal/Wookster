package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import entity.Entity.Direction;
import main.Constants;
import main.GamePanel.GameState;

public class SpriteManager {

    public static enum SpriteAnimation {
        MOVE,
        IDEL,
        DEAD;
    }

    public static class Sprite {
        public int frames;
        public BufferedImage image;
        public int height;
        public int width;
        public int xAdjust;
        public int yAdjust;
        public Direction direction;
        
        private static final int DEFAULT_FRAMES = 10;

        public Sprite(Direction direction, String imagePath, int frames) {
            try {
                this.direction = direction;
                this.image = ImageIO.read(getClass().getResourceAsStream(imagePath));
                this.frames = frames;
                this.height = this.image.getHeight() * Constants.SCALE;
                this.width = this.image.getWidth() * Constants.SCALE;
                if (this.height > Constants.TILE_SIZE && direction == Direction.UP) {
                    this.yAdjust = this.height - Constants.TILE_SIZE;
                }
                if (this.width > Constants.TILE_SIZE && direction == Direction.LEFT) {
                    this.xAdjust = this.width - Constants.TILE_SIZE;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public Sprite(Direction direction, String imagePath) {
            this(direction, imagePath, DEFAULT_FRAMES);
        }
    }

    public HashMap<String, HashMap<Direction, List<Sprite>>> spriteMap = new HashMap<>();

    public void setSprite(String type, Sprite sprite) {
        spriteMap
            .computeIfAbsent(type, k -> new HashMap<>())
            .computeIfAbsent(sprite.direction, k -> new ArrayList<>())
            .add(sprite);
    }

    public Sprite getSprite(Entity entity) {
        long gameTime = entity.gamePanel.gameTime;

        if (entity.isDead) {
            return this.spriteMap.get(SpriteAnimation.DEAD.name()).get(null).get(0);
        }
        
        String type;
        if (entity.isMoving) {
            type = SpriteAnimation.MOVE.name();
        } else {
            type = SpriteAnimation.IDEL.name();
        }

        if (entity.gamePanel.gameState != GameState.PLAY) {
            return this.spriteMap.get(type).get(entity.direction).get(0);
        }

        if (entity.attacking) {
            type = entity.weapon.weaponType.name();
            if (!entity.isMoving) {
                return this.spriteMap.get(type).get(entity.direction).get(0);
            }
        }

        List<Sprite> sprites = this.spriteMap.get(type).get(entity.direction);

        long nsPerFrame = 1_000_000_000L / 60;
        long totalFrames = gameTime / nsPerFrame;
        int totalSpriteFrames = 0;

        for (Sprite sprite : sprites) {
            totalSpriteFrames += sprite.frames;
        }

        long currentFrameIndex = totalFrames % totalSpriteFrames;

        int accumulated = 0;
        for (Sprite sprite : sprites) {
            accumulated += sprite.frames;
            if (currentFrameIndex < accumulated) {
                return sprite;
            }
        }

        return sprites.get(0);
    }

}
