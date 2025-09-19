package main;

import java.util.ArrayList;
import java.util.List;

import entity.Entity.Direction;
import spells.HealthSpell;
import spells.KeySpell;
import spells.SpeedSpell;
import tile.TileManager.TileLocation;

public class EventHandler {

    GamePanel gamePanel;
    EventRectangle eventRectangle[][];
    public int worldX, worldY, defaultX, defaultY;
    public List<TileLocation> randomDamage = new ArrayList<>();
    boolean canTouchAgain = true;

    int previousX;
    int previousY;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.defaultX = 0;
        this.defaultY = 0;

        eventRectangle = new EventRectangle[Constants.MAX_WORLD_COL][Constants.MAX_WORLD_ROW];

        int row = 0;
        int col = 0;
        while (col < Constants.MAX_WORLD_COL && row < Constants.MAX_WORLD_ROW) {
            this.eventRectangle[col][row] = new EventRectangle();
            this.eventRectangle[col][row].x = 23;
            this.eventRectangle[col][row].y = 23;
            this.eventRectangle[col][row].width = 2;
            this.eventRectangle[col][row].height = 2;
            this.eventRectangle[col][row].eventRectangleDefaultX = eventRectangle[col][row].x;
            this.eventRectangle[col][row].eventRectangleDefaultY = eventRectangle[col][row].y;
            col++;
            if (col == Constants.MAX_WORLD_COL) {
                col = 0;
                row++;
            }
        }
    }

    public void setRandomDamageTile(int count) {
        for (int i = 0; i < count; i++) {
            TileLocation tileLocation = this.gamePanel.tileManager.getRandomTileLocation();
            randomDamage.add(tileLocation);
        }
    }

    public void checkEvent() {
        int xDistance = Math.abs(this.gamePanel.player.worldX - previousX);
        int yDistance = Math.abs(this.gamePanel.player.worldY - previousY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > Constants.TILE_SIZE) {
            this.canTouchAgain = true;
        }
        for (TileLocation randomDamageTile : new ArrayList<>(randomDamage)) {
            if (
                checkCollision(randomDamageTile.worldX, randomDamageTile.worldY, null) &&
                this.canTouchAgain
            ){
                int index = Utils.generateRandomInt(0, Constants.RANDOM_HURT_DIALOGUE.size() - 1);
                String message = Constants.RANDOM_HURT_DIALOGUE.get(index);
                message(randomDamageTile.worldX, randomDamageTile.worldY, message);
            }
        }
    }

    private void message(int col, int row, String message) {
        this.gamePanel.player.entityInDialogue = null;
        this.gamePanel.ui.displayDialog(message);
        if (this.eventRectangle[col][row].retriggerable == false) {
            this.eventRectangle[col][row].eventFinished = true;
        }
        this.canTouchAgain = false;
        int random = Utils.generateRandomInt(0, 1);
        if (random == 0) {
            damagePlayer();
        } else {
            spellPlayer();
        }
    }

    private void damagePlayer() {
        this.gamePanel.player.takeDamage(Utils.generateRandomInt(5, 15), null);
    }

    private void spellPlayer() {
        int random = Utils.generateRandomInt(0, 2);
        switch (random) {
            case 0:
                HealthSpell healthSpell = new HealthSpell();
                healthSpell.randomHealthDamage();
                healthSpell.setSpell(this.gamePanel);
                break;
            case 1:
                SpeedSpell speedSpell = new SpeedSpell();
                speedSpell.randomSpeedSlow();
                speedSpell.setSpell(this.gamePanel);
                break;
            default:
                KeySpell keySpell = new KeySpell();
                keySpell.setSpell(this.gamePanel);
                break;
        }
    }

    public boolean checkCollision(int col, int row, Direction direction) {
        boolean collision = false;

        this.gamePanel.player.solidArea.x = this.gamePanel.player.worldX + this.gamePanel.player.solidArea.x;
        this.gamePanel.player.solidArea.y = this.gamePanel.player.worldY + this.gamePanel.player.solidArea.y;
        this.eventRectangle[col][row].x = col * Constants.TILE_SIZE + this.eventRectangle[col][row].x;
        this.eventRectangle[col][row].y = row * Constants.TILE_SIZE + this.eventRectangle[col][row].y;

        if (
            this.gamePanel.player.solidArea.intersects(this.eventRectangle[col][row]) &&
            !this.eventRectangle[col][row].eventFinished &&
            this.canTouchAgain == true
        ){
            if (direction == null || this.gamePanel.player.direction == direction) {
                collision = true;
                previousX = this.gamePanel.player.worldX;
                previousY = this.gamePanel.player.worldY;
            }
        }

        this.gamePanel.player.solidArea.x = this.gamePanel.player.solidAreaDefaultX;
        this.gamePanel.player.solidArea.y = this.gamePanel.player.solidAreaDefaultY;
        this.eventRectangle[col][row].x = this.eventRectangle[col][row].eventRectangleDefaultX;
        this.eventRectangle[col][row].y = this.eventRectangle[col][row].eventRectangleDefaultY;
        return collision;
    }
}
