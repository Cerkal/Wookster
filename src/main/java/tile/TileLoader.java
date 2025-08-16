package tile;

import java.util.ArrayList;
import java.util.Arrays;

import main.Constants;

public class TileLoader {

    public static Tile[] getTiles() {
        Tile[] tiles = new Tile[70];

        for (int i = 0; i < 10; i++) {
            tiles[i] = new Tile("Unknown", Constants.EFFECT_UKNOWN);
        }

        tiles[0] = new Tile("Clear", Constants.TILE_CLEAR);

        tiles[10] = new Tile(
            "Grass",
            Constants.TILE_GRASS_00,
            new ArrayList<String>(Arrays.asList(Constants.TILE_GRASS_01)),
            2
        );
        tiles[11] = new Tile("Grass", Constants.TILE_GRASS_01);
        
        // Water tiles
        tiles[12] = new Tile("Water", Constants.TILE_WATER_00);
        tiles[13] = new Tile(
            "Water",
            Constants.TILE_WATER_00,
            new ArrayList<String>(Arrays.asList(Constants.TILE_WATER_A01, Constants.TILE_WATER_A02, Constants.TILE_WATER_A03))
        );
        tiles[14] = new Tile("Water", Constants.TILE_WATER_02);
        tiles[15] = new Tile("Water", Constants.TILE_WATER_03);
        tiles[16] = new Tile("Water", Constants.TILE_WATER_04);
        tiles[17] = new Tile("Water", Constants.TILE_WATER_05);
        tiles[18] = new Tile("Water", Constants.TILE_WATER_06);
        tiles[19] = new Tile("Water", Constants.TILE_WATER_07);
        tiles[20] = new Tile("Water", Constants.TILE_WATER_08);
        tiles[21] = new Tile("Water", Constants.TILE_WATER_09);
        tiles[22] = new Tile("Water", Constants.TILE_WATER_10);
        tiles[23] = new Tile("Water", Constants.TILE_WATER_11);
        tiles[24] = new Tile("Water", Constants.TILE_WATER_12);
        tiles[25] = new Tile("Water", Constants.TILE_WATER_13);
        
        // Road tiles
        tiles[26] = new Tile("Road", Constants.TILE_ROAD_00);
        tiles[27] = new Tile("Road", Constants.TILE_ROAD_01);
        tiles[28] = new Tile("Road", Constants.TILE_ROAD_02);
        tiles[29] = new Tile("Road", Constants.TILE_ROAD_03);
        tiles[30] = new Tile("Road", Constants.TILE_ROAD_04);
        tiles[31] = new Tile("Road", Constants.TILE_ROAD_05);
        tiles[32] = new Tile("Road", Constants.TILE_ROAD_06);
        tiles[33] = new Tile("Road", Constants.TILE_ROAD_07);
        tiles[34] = new Tile("Road", Constants.TILE_ROAD_08);
        tiles[35] = new Tile("Road", Constants.TILE_ROAD_09);
        tiles[36] = new Tile("Road", Constants.TILE_ROAD_10);
        tiles[37] = new Tile("Road", Constants.TILE_ROAD_11);
        tiles[38] = new Tile("Road", Constants.TILE_ROAD_12);

        // World tiles
        tiles[39] = new Tile("Earth", Constants.TILE_EARTH);
        tiles[40] = new Tile("Wall", Constants.TILE_WALL);
        tiles[41] = new Tile("Tree", Constants.TILE_TREE);
        tiles[42] = new Tile("Tree", Constants.TILE_TREE_FALL);

        // Bridge
        tiles[43] = new Tile("Bridge", Constants.TILE_BRIDGE_00);
        tiles[44] = new Tile("Bridge", Constants.TILE_BRIDGE_01);
        // Joiner
        tiles[45] = new Tile("Bridge", Constants.TILE_BRIDGE_JOIN_00);
        tiles[46] = new Tile("Bridge", Constants.TILE_BRIDGE_JOIN_01);
        tiles[47] = new Tile("Bridge", Constants.TILE_BRIDGE_JOIN_02);
        tiles[48] = new Tile("Bridge", Constants.TILE_BRIDGE_JOIN_03);
        // Side
        tiles[49] = new Tile("Bridge", Constants.TILE_BRIDGE_SIDE_00, true, false);
        tiles[50] = new Tile("Bridge", Constants.TILE_BRIDGE_SIDE_01);
        tiles[51] = new Tile("Bridge", Constants.TILE_BRIDGE_SIDE_02);
        // Middle

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                tiles[i].indexNumber = i;
            }
        }
        return tiles;
    }

    public static Tile getTile(int index) {
        Tile[] tiles = getTiles();
        return tiles[index];
    }
}
