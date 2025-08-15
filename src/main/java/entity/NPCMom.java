package entity;

import java.util.ArrayList;
import java.util.Arrays;

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
        this.damageSound = Constants.SOUND_HURT;
        this.entityType = EntityType.NPC;
        this.name = "Mom";
    }

    public void getPlayerImage() {
        try {
            this.imageMapDefault.put(Direction.UP, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_UP_1))
            )));
            this.imageMapDefault.put(Direction.DOWN, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DOWN_1))
            )));
            this.imageMapDefault.put(Direction.LEFT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_LEFT_1))
            )));
            this.imageMapDefault.put(Direction.RIGHT, new ArrayList<>(Arrays.asList(
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_0)),
                ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_RIGHT_1))
            )));
            this.imageMap = this.imageMapDefault;
            this.dead = ImageIO.read(getClass().getResourceAsStream(Constants.PLAYER_IMAGE_DEAD));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + e.getStackTrace());
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
