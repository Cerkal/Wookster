package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map;

import entity.Player;
import main.Constants;
import main.GamePanel;
import main.InventoryItem;
import tile.Tile;

public class GameMap extends SuperObject {

    static final int MAP_TILE_SIZE = 8;

    public GameMap(GamePanel gamePanel) {
        super(gamePanel, 0, 0);
        init();
    }

    private void init() {
        this.objectType = Object_Type.MAP;
        this.name = this.objectType.name();
        this.inventoryItem = new InventoryItem(this, 1, true);
    }

    @Override
    public void useObject() {
        // Do nothing, always keep the map
    }

    @Override
    public void drawDetails(Graphics2D graphics2D, int x, int y) {
        Map<Point, Tile> tileMap = this.gamePanel.tileManager.tileMap;
        int mapX = x - Constants.TILE_SIZE * 2;
        int mapY = (Constants.SCREEN_HEIGHT / 2) - ((MAP_TILE_SIZE * Constants.MAX_WORLD_ROW) / 2);
        for (Point point : tileMap.keySet()) {
            x = (int) point.getX();
            y = (int) point.getY();
            x *= MAP_TILE_SIZE;
            y *= MAP_TILE_SIZE;
            Tile currentTile = tileMap.get(point);
            graphics2D.setColor(Color.GRAY);
            if (currentTile.collision == true) {
                graphics2D.setColor(Color.DARK_GRAY);
            }
            if (currentTile.discovered != true) {
                graphics2D.setColor(Color.BLACK);
            }
            graphics2D.fillRect(x + mapX, y + mapY, MAP_TILE_SIZE, MAP_TILE_SIZE);
            Player player = this.gamePanel.player;
            int playerX = (int) player.getLocation().getX();
            int playerY = (int) player.getLocation().getY();
            if (point.getX() == playerX && point.getY() == playerY) {
                playerX *= MAP_TILE_SIZE;
                playerY *= MAP_TILE_SIZE;
                graphics2D.setColor(Color.RED);
                graphics2D.fillRect(playerX + mapX, playerY + mapY, MAP_TILE_SIZE, MAP_TILE_SIZE);
            }
        }
        graphics2D.setColor(Color.WHITE);
    }
}
