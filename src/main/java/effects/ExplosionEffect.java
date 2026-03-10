package effects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import entity.SpriteManager;
import entity.SpriteManager.Sprite;
import main.Constants;
import main.GamePanel;
import objects.projectiles.Projectile;

public class ExplosionEffect extends Effect {

    SpriteManager spriteManager;
    int worldX;
    int worldY;

    public Rectangle solidArea = new Rectangle(0, 0, Constants.TILE_SIZE*2, Constants.TILE_SIZE*2);
    public int solidAreaDefaultX = solidArea.x;
    public int solidAreaDefaultY = solidArea.y;

    final String EFFECT_NAME = "ExplosionEffect";

    private int currentSpriteIndex = 0;
    private int frameCounter = 0;
    private static final int FRAMES_PER_SPRITE = 10;
    private static final int TOTAL_SPRITES = 5;

    public ExplosionEffect(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel, projectile.worldX, projectile.worldY);
        this.worldX = projectile.worldX;
        this.worldY = projectile.worldY;
        this.effectTime = 2;
        setImage();
    }

    protected void setImage() {
        spriteManager = new SpriteManager();
        spriteManager.setSprite(EFFECT_NAME, new Sprite(null, Constants.EXPLISION_EFFECT_0));
        spriteManager.setSprite(EFFECT_NAME, new Sprite(null, Constants.EXPLISION_EFFECT_1));
        spriteManager.setSprite(EFFECT_NAME, new Sprite(null, Constants.EXPLISION_EFFECT_2));
        spriteManager.setSprite(EFFECT_NAME, new Sprite(null, Constants.EXPLISION_EFFECT_3));
        spriteManager.setSprite(EFFECT_NAME, new Sprite(null, Constants.EXPLISION_EFFECT_4));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (currentSpriteIndex >= TOTAL_SPRITES) {
            this.gamePanel.effects.remove(this);
            return;
        }

        Sprite effectSprite = this.spriteManager.spriteMap.get(EFFECT_NAME).get(null).get(currentSpriteIndex);

        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        graphics2D.drawImage(effectSprite.image, screenX - Constants.TILE_SIZE, screenY - Constants.TILE_SIZE, null);

        frameCounter++;
        if (frameCounter >= FRAMES_PER_SPRITE) {
            frameCounter = 0;
            currentSpriteIndex++;
        }

        if (this.gamePanel.debugCollision) {
            graphics2D.setColor(java.awt.Color.RED);
            graphics2D.drawRect(
                screenX + solidArea.x - Constants.TILE_SIZE,
                screenY + solidArea.y - Constants.TILE_SIZE,
                solidArea.width,
                solidArea.height
            );
        }
    }

    @Override
    public void update() {
        int explosionLeft = this.worldX - Constants.TILE_SIZE + solidArea.x;
        int explosionTop = this.worldY - Constants.TILE_SIZE + solidArea.y;
        Rectangle explosionWorldArea = new Rectangle(explosionLeft, explosionTop, solidArea.width, solidArea.height);

        for (Entity entity : this.gamePanel.npcs) {
            int entityLeft = entity.worldX + entity.solidArea.x;
            int entityTop = entity.worldY + entity.solidArea.y;
            Rectangle entityWorldArea = new Rectangle(entityLeft, entityTop, entity.solidArea.width, entity.solidArea.height);

            if (entityWorldArea.intersects(explosionWorldArea)) {
                entity.takeDamage(50, this.gamePanel.player);
            }
        }
    }
}
