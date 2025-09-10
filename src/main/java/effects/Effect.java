package effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.Utils;

public class Effect {

    GamePanel gamePanel;
    public BufferedImage image;
    public int worldX;
    public int worldY;
    public int effectTime = 0;
    public long startTime = 0;

    public Effect(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX;
        this.worldY = worldY;
        this.startTime = this.gamePanel.gameTime;
        setImage();
    }

    public void draw(Graphics2D graphics2D) {
        int screenX = this.worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = this.worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (
            this.worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
            this.worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
            this.worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
            this.worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.screenY)
        ){
            graphics2D.drawImage(this.image, screenX, screenY - Constants.TILE_SIZE/2, null);
        }
    }

    protected void setImage() {
        setImage(Constants.EFFECT_UKNOWN);
    }

    protected void setImage(String image) {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(image));
            this.image = Utils.scaleImage(this.image); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
