package entity;

import main.GamePanel;

public class NPCDroids extends Entity {

    public NPCDroids(GamePanel gamePanel, int worldX, int worldY) {
        super(gamePanel, worldX, worldY);
        this.direction = Direction.DOWN;
        this.speed = 2;
        this.setDialogue();
        this.entityType = EntityType.NPC;
        this.name = "Droids";
        this.movable = false;
    }

    public void setDialogue() {
        String[] lines = {
            "We're not the droids you're lo...",
            "Ah, fine. We are. You found us.",
            "[Beep..] [Boop...] [Whistle...]"
        };
        this.dialogue = lines;
    }

    @Override
    protected void loadSprites() {
        throw new UnsupportedOperationException("Unimplemented method 'loadSprites'");
    }
}
