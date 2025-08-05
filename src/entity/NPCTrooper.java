package entity;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class NPCTrooper extends Entity {

    public NPCTrooper(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.getPlayerImage();
        this.setDialogue();
        this.entityType = Entity_Type.NPC;
        this.name = "Trooper";
    }

    public void getPlayerImage() {
        try {
            this.up1 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_UP_0));
            this.up2 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_UP_1));
            this.down1 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_DOWN_0));
            this.down2 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_DOWN_1));
            this.left1 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_LEFT_0));
            this.left2 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_LEFT_1));
            this.right1 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_RIGHT_0));
            this.right2 = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_RIGHT_1));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    public void setDialogue() {
        String[] lines = {
            "These are not the droids you are looking for.",
            "I'm just kidding.",
            "They're right over there.",
            "Wait...",
            "Don't look for them.",
            "But believe me, they're over there."
        };
        this.dialogue = lines;
    }
}
