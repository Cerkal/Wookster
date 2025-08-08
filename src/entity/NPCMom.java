package entity;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class NPCMom extends Entity {

    public NPCMom(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.getPlayerImage();
        this.setDialogue();
        this.entityType = Entity_Type.NPC;
        this.name = "Mom";
    }

    public void getPlayerImage() {
        try {
            this.up1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_0));
            this.up2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_1));
            this.down1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_0));
            this.down2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_1));
            this.left1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_0));
            this.left2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_1));
            this.right1 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_0));
            this.right2 = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_1));
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DEAD));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    public void setDialogue() {
        String[] lines = {
            "Wookster...",
            "It's your mom...",
            "You're dead now."
        };
        this.dialogue = lines;
    }
}
