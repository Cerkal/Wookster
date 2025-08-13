package tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Scanner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import main.Constants;
import main.GamePanel;
import main.Utils;

public class TileManager {

    GamePanel gamePanel;

    public Tile[] tile;
    public int[][] mapTileNum;
    public Set<Integer> walkableTileIndex = new HashSet<>();
    public List<TileLocation> walkableTiles = new ArrayList<>();

    public final Map<Point, Tile> tileMap = new HashMap<>();

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tile = TileLoader.getTiles();
        this.mapTileNum = new int[Constants.MAX_WORLD_COL][Constants.MAX_WORLD_ROW];
    }

    public void setMap(String mapPath) {
        walkableTiles.clear();
        tileMap.clear();
        loadMap(mapPath);
    }

    public void loadMap(String path) {
        try (InputStream is = getClass().getResourceAsStream(path);
            Scanner readMap = new Scanner(is)) {

            if (is == null) {
                throw new IllegalArgumentException("Map file not found: " + path);
            }

            int col = 0;
            int row = 0;

            while (col < Constants.MAX_WORLD_COL && row < Constants.MAX_WORLD_ROW) {
                String line = readMap.nextLine();
                String[] sections = line.split(" ");

                while (col < Constants.MAX_WORLD_COL) {
                    int tileNumber = Integer.parseInt(sections[col]);
                    mapTileNum[col][row] = tileNumber;

                    Point key = new Point(col, row);
                    Tile baseTile = tile[tileNumber];
                    Tile currentTile = new Tile(baseTile);
                    currentTile.randomize();
                    tileMap.put(key, currentTile);

                    if (tile[tileNumber].collision == false) {
                        walkableTiles.add(new TileLocation(col, row));
                    }
                    col++;
                }

                if (col == Constants.MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tile getTile(int x, int y) {
        return tileMap.get(new Point(x, y));
    }

    public Tile getTile(Point point) {
        return tileMap.get(point);
    }

    public void draw(Graphics2D graphics2D) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < Constants.MAX_WORLD_COL && worldRow < Constants.MAX_WORLD_ROW) {
            int worldX = worldCol * Constants.TILE_SIZE;
            int worldY = worldRow * Constants.TILE_SIZE;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
            if (
                worldX + Constants.TILE_SIZE > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - Constants.TILE_SIZE < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + Constants.TILE_SIZE > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - Constants.TILE_SIZE < gamePanel.player.worldY + gamePanel.player.screenY
            ) {
                Tile currentTile = tileMap.get(new Point(worldCol, worldRow));
                if (currentTile != null) {
                    BufferedImage tileImage = currentTile.getCurrentImage(gamePanel.gameTime);
                    graphics2D.drawImage(tileImage, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
                    drawDebug(graphics2D, currentTile, screenX, screenY);
                }
            }
            disoverTile(worldCol, worldRow);
            worldCol++;
            if (worldCol == Constants.MAX_WORLD_COL) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    private void drawDebug(Graphics2D graphics2D, Tile currentTile, int screenX, int screenY) {
        if (this.gamePanel.debugMapBuilder) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(
                String.valueOf(currentTile.indexNumber),
                screenX + Constants.TILE_SIZE/2,
                screenY + Constants.TILE_SIZE/2
            );
            if (currentTile.tileHighlight) {
                graphics2D.setColor(Color.RED);
            }
            graphics2D.drawRect(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
    }

    private void disoverTile(int worldCol, int worldRow) {
        int worldX = worldCol * Constants.TILE_SIZE;
        int worldY = worldRow * Constants.TILE_SIZE;
        if (
            worldX + Constants.TILE_SIZE > gamePanel.player.worldX - gamePanel.player.screenX * .75 &&
            worldX - Constants.TILE_SIZE < gamePanel.player.worldX + gamePanel.player.screenX * .75 &&
            worldY + Constants.TILE_SIZE > gamePanel.player.worldY - gamePanel.player.screenY * .75 &&
            worldY - Constants.TILE_SIZE < gamePanel.player.worldY + gamePanel.player.screenY * .75
        ) {
            Tile currentTile = tileMap.get(new Point(worldCol, worldRow));
            if (currentTile != null) {
                currentTile.discovered = true;
            }
        }
    }

    public TileLocation getRandomTileLocation() {
        if (walkableTiles.isEmpty()) {
            throw new IllegalArgumentException("Not enough free tiles");
        }
        return walkableTiles.get(Utils.generateRandomInt(0, walkableTiles.size() - 1));
    }

    public class TileLocation {
        public int worldX;
        public int worldY;
        public TileLocation(int worldX, int worldY) {
            this.worldX = worldX;
            this.worldY = worldY;
        }
    }
}
