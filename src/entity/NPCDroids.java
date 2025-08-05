package entity;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class NPCDroids extends Entity {

    public NPCDroids(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.getPlayerImage();
        this.setDialogue();
        this.entityType = Entity_Type.NPC;
        this.name = "Droids";
        this.movable = false;
    }

    public void getPlayerImage() {
        try {
            this.down1 = ImageIO.read(getClass().getResourceAsStream(Constants.DROIDS_IMAGE_DOWN_0));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    public void setDialogue() {
        String[] lines = {
            "We're not the droids you're lo...",
            "Ah, fine. We are. You found us.",
            "[Beep..] [Boop...] [Whistle...]"
        };
        this.dialogue = lines;
    }
}
