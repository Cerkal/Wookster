package tile;

import java.io.File;
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
        loadMap(Constants.WORLD_00);
    }

    public void gameTileImage() {

            // Grass tiles
            setTile(10, Constants.TILE_GRASS_00, false);
            setTile(11, Constants.TILE_GRASS_01, false);

            // Water tiles
            setTile(12, Constants.TILE_WATER_00, true);
            try {
                tile[13] = new Tile();
                tile[13].imageSequence.add(ImageIO.read(new File(Constants.TILE_WATER_A01)));
                tile[13].imageSequence.add(ImageIO.read(new File(Constants.TILE_WATER_A02)));
                tile[13].imageSequence.add(ImageIO.read(new File(Constants.TILE_WATER_A03)));
                tile[13].collision = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            setTile(14, Constants.TILE_WATER_02, true);
            setTile(15, Constants.TILE_WATER_03, true);
            setTile(16, Constants.TILE_WATER_04, true);
            setTile(17, Constants.TILE_WATER_05, true);
            setTile(18, Constants.TILE_WATER_06, true);
            setTile(19, Constants.TILE_WATER_07, true);
            setTile(20, Constants.TILE_WATER_08, true);
            setTile(21, Constants.TILE_WATER_09, true);
            setTile(22, Constants.TILE_WATER_10, true);
            setTile(23, Constants.TILE_WATER_11, true);
            setTile(24, Constants.TILE_WATER_12, true);
            setTile(25, Constants.TILE_WATER_13, true);

            // Road tiles
            setTile(26, Constants.TILE_ROAD_00, false);
            setTile(27, Constants.TILE_ROAD_01, false);
            setTile(28, Constants.TILE_ROAD_02, false);
            setTile(29, Constants.TILE_ROAD_03, false);
            setTile(30, Constants.TILE_ROAD_04, false);
            setTile(31, Constants.TILE_ROAD_05, false);
            setTile(32, Constants.TILE_ROAD_06, false);
            setTile(33, Constants.TILE_ROAD_07, false);
            setTile(34, Constants.TILE_ROAD_08, false);
            setTile(35, Constants.TILE_ROAD_09, false);
            setTile(36, Constants.TILE_ROAD_10, false);
            setTile(37, Constants.TILE_ROAD_11, false);
            setTile(38, Constants.TILE_ROAD_12, false);

            // World tiles
            setTile(39, Constants.TILE_EARTH, false);
            setTile(40, Constants.TILE_WALL, true);
            setTile(41, Constants.TILE_TREE, true);
    }

    private void setTile(int index, String imagePath, boolean collision) {
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(new File(imagePath));
            tile[index].collision = collision;
            if (!collision) {
                walkableTileIndex.add(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String path) {
        try {
            File mapFile = new File(path);
            Scanner readMap = new Scanner(mapFile);

            int col = 0;
            int row = 0;

            while (col < Constants.MAX_WORLD_COL && row < Constants.MAX_WORLD_ROW) {
                String line = readMap.nextLine();
                while (col < Constants.MAX_WORLD_COL) {
                    String sections[] = line.split(" ");
                    int tileNumber = Integer.parseInt(sections[col]);
                    mapTileNum[col][row] = tileNumber;
                    if (walkableTileIndex.contains(tileNumber)) { 
                        TileLocation tileLocation = new TileLocation(col, row);
                        walkableTiles.add(tileLocation);
                    }
                    col++;
                }
                if (col == Constants.MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }
            readMap.close();
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
                worldY - (Constants.TILE_SIZE) < (gamePanel.player.worldY + gamePanel.player.worldY)
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

    public class TileLocation {
        public int worldX;
        public int worldY;
        public TileLocation(int worldX, int worldY) {
            this.worldX = worldX;
            this.worldY = worldY;
        }
    }
}
