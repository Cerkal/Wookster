package tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Constants;
import main.GamePanel;
import main.Utils;

public class TileManager {

    GamePanel gamePanel;
    
    public Tile[] tile;
    public int mapTileNum[][];
    public Set<Integer> walkableTileIndex = new HashSet<Integer>();
    public List<TileLocation> walkableTiles = new ArrayList<TileLocation>();

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new Tile[50];
        mapTileNum = new int[Constants.MAX_WORLD_COL][Constants.MAX_WORLD_ROW];
        gameTileImage();
    }

    public void setMap(String mapPath) {
        walkableTiles.clear();
        loadMap(mapPath);
    }


    public void gameTileImage() {

            // Grass tiles
            setTile(10, Constants.TILE_GRASS_00, false, false);
            setTile(11, Constants.TILE_GRASS_01, false, false);

            // Water tiles
            setTile(12, Constants.TILE_WATER_00, true, false);
            try {
                tile[13] = new Tile();
                tile[13].imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.TILE_WATER_A01)));
                tile[13].imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.TILE_WATER_A02)));
                tile[13].imageSequence.add(ImageIO.read(getClass().getResourceAsStream(Constants.TILE_WATER_A03)));
                tile[13].collision = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            setTile(14, Constants.TILE_WATER_02, true, false);
            setTile(15, Constants.TILE_WATER_03, true, false);
            setTile(16, Constants.TILE_WATER_04, true, false);
            setTile(17, Constants.TILE_WATER_05, true, false);
            setTile(18, Constants.TILE_WATER_06, true, false);
            setTile(19, Constants.TILE_WATER_07, true, false);
            setTile(20, Constants.TILE_WATER_08, true, false);
            setTile(21, Constants.TILE_WATER_09, true, false);
            setTile(22, Constants.TILE_WATER_10, true, false);
            setTile(23, Constants.TILE_WATER_11, true, false);
            setTile(24, Constants.TILE_WATER_12, true, false);
            setTile(25, Constants.TILE_WATER_13, true, false);

            // Road tiles
            setTile(26, Constants.TILE_ROAD_00, false, false);
            setTile(27, Constants.TILE_ROAD_01, false, false);
            setTile(28, Constants.TILE_ROAD_02, false, false);
            setTile(29, Constants.TILE_ROAD_03, false, false);
            setTile(30, Constants.TILE_ROAD_04, false, false);
            setTile(31, Constants.TILE_ROAD_05, false, false);
            setTile(32, Constants.TILE_ROAD_06, false, false);
            setTile(33, Constants.TILE_ROAD_07, false, false);
            setTile(34, Constants.TILE_ROAD_08, false, false);
            setTile(35, Constants.TILE_ROAD_09, false, false);
            setTile(36, Constants.TILE_ROAD_10, false, false);
            setTile(37, Constants.TILE_ROAD_11, false, false);
            setTile(38, Constants.TILE_ROAD_12, false, false);

            // World tiles
            setTile(39, Constants.TILE_EARTH, false, false);
            setTile(40, Constants.TILE_WALL, true , true);
            setTile(41, Constants.TILE_TREE, true, true);
    }

    private void setTile(int index, String imagePath, boolean collision, boolean projectileCollision) {
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            tile[index].collision = collision;
            tile[index].projectileCollision = projectileCollision;
            if (!collision) {
                walkableTileIndex.add(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                    if (walkableTileIndex.contains(tileNumber)) { 
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


    public void draw(Graphics2D graphics2D) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < Constants.MAX_WORLD_COL && worldRow < Constants.MAX_WORLD_ROW) {
            int tileSec = mapTileNum[worldCol][worldRow];
            
            int worldX = worldCol * Constants.TILE_SIZE;
            int worldY = worldRow * Constants.TILE_SIZE;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if (
                worldX + (Constants.TILE_SIZE) > (gamePanel.player.worldX - gamePanel.player.screenX) &&
                worldX - (Constants.TILE_SIZE) < (gamePanel.player.worldX + gamePanel.player.screenX) &&
                worldY + (Constants.TILE_SIZE) > (gamePanel.player.worldY - gamePanel.player.screenY) &&
                worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.screenY)
            ){
                BufferedImage tileImage = tile[tileSec].getCurrentImage(gamePanel.gameTime);
                graphics2D.drawImage(tileImage, screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
            }
            
            worldCol++;

            if (worldCol == Constants.MAX_WORLD_COL) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public TileLocation getRandomTileLocation() {

        int walkableTileSize = this.walkableTiles.size() - 1;
        if (walkableTileSize == 0) {
            throw new IllegalArgumentException("Not enough free tiles");
        }
        int randomTileIndex = Utils.generateRandomInt(0, walkableTileSize);
        TileLocation randomDamageTile = this.walkableTiles.get(randomTileIndex);
        walkableTiles.remove(randomTileIndex);
        return randomDamageTile;
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
