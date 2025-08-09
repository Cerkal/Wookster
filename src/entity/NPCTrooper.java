package entity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class NPCTrooper extends Entity {

    public NPCTrooper(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.getPlayerImage();
        // this.setDialogue();
        this.entityType = Entity_Type.ENEMY;
        this.name = "Trooper";
    }

    public void getPlayerImage() {
        try {
            this.imageMapDefault.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_UP_1))
            )));
            this.imageMapDefault.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_DOWN_1))
            )));
            this.imageMapDefault.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_LEFT_1))
            )));
            this.imageMapDefault.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_RIGHT_1))
            )));
            this.imageMap = this.imageMapDefault;
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.TROOPER_IMAGE_DEAD));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + e.getStackTrace());
        }
    }

    // public void setDialogue() {
    //     String[] lines = {
    //         "These are not the droids you are looking for.",
    //         "I'm just kidding.",
    //         "They're right over there.",
    //         "Wait...",
    //         "Don't look for them.",
    //         "But believe me, they're over there."
    //     };
    //     this.dialogue = lines;
    // }
}
