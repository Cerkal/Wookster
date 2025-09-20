package tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<TileLocation> availableTiles = new ArrayList<>();
    public boolean[][] walkableTiles = new boolean[50][50];
    public BufferedImage background;

    public final Map<Point, Tile> tileMap = new HashMap<>();

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tile = TileLoader.getTiles();
        this.mapTileNum = new int[Constants.MAX_WORLD_COL][Constants.MAX_WORLD_ROW];
    }

    public void setMap(String mapPath) {
        availableTiles.clear();
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
                    currentTile.imageIndex = tileNumber;
                    currentTile.x = col;
                    currentTile.y = row;
                    currentTile.randomize();
                    tileMap.put(key, currentTile);

                    walkableTiles[col][row] = !tile[tileNumber].collision;
                    if (tile[tileNumber].collision == false) {
                        availableTiles.add(new TileLocation(col, row));
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

        drawBackground(graphics2D);

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
                    if (tileImage != null) {
                        graphics2D.drawImage(tileImage, screenX, screenY, null);
                        drawDebug(graphics2D, currentTile, screenX, screenY);
                    }
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

    private void drawBackground(Graphics2D graphics2D) {
        if (this.gamePanel.background == null) {
            System.out.println("Background is null ");
            return;
        }
        double parallax = 0.75;

        int bgX = (int) (
            Constants.SCREEN_WIDTH / 2
            - (Constants.BACKGROUND_WIDTH / 2) // center the bg
            - gamePanel.player.worldX * parallax
            + gamePanel.player.screenX * parallax
        ) + Constants.SCREEN_WIDTH/2;

        int bgY = (int) (
            Constants.SCREEN_HEIGHT / 2
            - (Constants.BACKGROUND_HEIGHT / 2) // center the bg
            - gamePanel.player.worldY * parallax
            + gamePanel.player.screenY * parallax
        ) + (int) (Constants.SCREEN_HEIGHT  * 1.25);

        graphics2D.drawImage(
            this.gamePanel.background,
            bgX, bgY,
            Constants.BACKGROUND_WIDTH, Constants.BACKGROUND_HEIGHT,
            null
        );
    }

    private void drawDebug(Graphics2D graphics2D, Tile currentTile, int screenX, int screenY) {
        // if (this.gamePanel.debugMap) {
        //     graphics2D.drawRect(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE);
        //     graphics2D.setColor(Color.WHITE);
        //     screenY += 10;
        //     graphics2D.drawString(
        //         String.valueOf(currentTile.imageIndex),
        //         screenX,
        //         screenY
        //     );
        // }
        if (this.gamePanel.debugMap) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE);
            screenY += 10;
            graphics2D.drawString(
                String.valueOf(currentTile.indexNumber),
                screenX,
                screenY
            );
            screenY += 10;
            graphics2D.drawString(
                String.valueOf("x: " + currentTile.x),
                screenX,
                screenY
            );
            screenY += 10;
            graphics2D.drawString(
                String.valueOf("y: " + currentTile.y),
                screenX,
                screenY
            );
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
        if (availableTiles.isEmpty()) {
            throw new IllegalArgumentException("Not enough free tiles");
        }
        return availableTiles.get(Utils.generateRandomInt(0, availableTiles.size() - 1));
    }

    public Point getRandomTileLocationsWithinArea(List<Point> areaList) {
        List<Point> locations = new ArrayList<>();
        if (areaList.size() != 2) {
            TileLocation tileLocation = getRandomTileLocation();
            new Point(tileLocation.worldX, tileLocation.worldY);
        }
        for (int y = areaList.get(0).y; y < areaList.get(1).y; y++) {
            for (int x = areaList.get(0).x; x < areaList.get(1).x; x++) {
                if (walkableTiles[x][y] == true) {
                    locations.add(new Point(x, y));
                }
            }
        }
        Point activePoint = locations.get(Utils.generateRandomInt(0, locations.size() - 1));
        if (activePoint != null) {
            return locations.get(Utils.generateRandomInt(0, locations.size() - 1));
        }
        return null;
    }

    public class TileLocation {
        public int worldX;
        public int worldY;
        public TileLocation(int worldX, int worldY) {
            this.worldX = worldX;
            this.worldY = worldY;
        }
    }

    public void removeWalkableTile(int x, int y) {
        if (x < 0 || x >= walkableTiles.length || y < 0 || y >= walkableTiles[0].length) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        walkableTiles[x][y] = false;
        availableTiles.removeIf(tileLocation -> tileLocation.worldX == x && tileLocation.worldY == y);
    }

    public void addWalkableTile(int x, int y) {
        if (x < 0 || x >= walkableTiles.length || y < 0 || y >= walkableTiles[0].length) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        if (!walkableTiles[x][y]) {
            walkableTiles[x][y] = true;
            availableTiles.add(new TileLocation(x, y));
        }
    }
}
