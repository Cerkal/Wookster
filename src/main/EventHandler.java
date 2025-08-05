package main;

import java.util.ArrayList;
import java.util.List;

import entity.Entity.Direction;
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
        setRandomDamageTile();
    }

    private void setRandomDamageTile() {
        for (int i = 0; i < 10; i++) {
            int walkableTileSize = this.gamePanel.tileManager.walkableTiles.size();
            int randomTileIndex = Utils.generateRandomInt(0, walkableTileSize);
            TileLocation randomDamageTile = this.gamePanel.tileManager.walkableTiles.get(randomTileIndex);
            randomDamage.add(randomDamageTile);
        }
    }

    public void checkEvent() {
        int xDistance = Math.abs(this.gamePanel.player.worldX - previousX);
        int yDistance = Math.abs(this.gamePanel.player.worldY - previousY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > Constants.TILE_SIZE) {
            this.canTouchAgain = true;
        }
        for (TileLocation randomDamageTile : randomDamage) {
            if (
                checkCollision(randomDamageTile.worldX, randomDamageTile.worldY, null) &&
                this.canTouchAgain
            ){
                int index = Utils.generateRandomInt(0, Constants.RNADOM_HURT_DIALOGUE.size() - 1);
                String message = Constants.RNADOM_HURT_DIALOGUE.get(index);
                damageWithMessage(randomDamageTile.worldX, randomDamageTile.worldY, message);
            }
        }
    }

    private void damageWithMessage(int col, int row, String message) {
        this.gamePanel.player.entityInDialogue = null;
        this.gamePanel.ui.displayDialog(message);
        this.gamePanel.player.takeDamage(10);
        if (this.eventRectangle[col][row].retriggerable == false) {
            this.eventRectangle[col][row].eventFinished = true;
        }
        this.canTouchAgain = false;
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
