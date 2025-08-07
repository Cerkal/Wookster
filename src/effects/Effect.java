package effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;
import main.Utils;

public class Effect {

    GamePanel gamePanel;
    BufferedImage image;

    public int worldX;
    public int worldY;

    public Effect(GamePanel gamePanel, int worldX, int worldY) {
        this.gamePanel = gamePanel;
        this.worldX = worldX;
        this.worldY = worldY;
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
            graphics2D.drawImage(this.image, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
        }
    }

    protected void setImage() {
        try {
            List<String> randomImage = new ArrayList<>();
            randomImage.add(Constants.EFFECT_BLOOD_0);
            randomImage.add(Constants.EFFECT_BLOOD_1);
            randomImage.add(Constants.EFFECT_BLOOD_2);
            int index = Utils.generateRandomInt(0, randomImage.size() - 1);
            setImage(randomImage.get(index));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setImage(String image) {
        try {
            this.image  = ImageIO.read(getClass().getResourceAsStream(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
