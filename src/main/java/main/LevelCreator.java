package main;

import java.io.*;
import java.util.*;

public class LevelCreator {

    private static final int CLEAR = 0;
    private static final int WATER = 12;
    private static final int GRASS = 10;
    private static final int TREE = 40;
    private static final int WOOD = 64;

    private static final String INPUT_PATH = "/maps/build/raw_map.txt";
    private static final String OUTPUT_PATH = "src/main/resources/maps/build/generated_map.txt";

    private int[][] mapTileNum;

    public LevelCreator(String inputPath, String outputPath, int rows, int cols) {
        mapTileNum = new int[cols][rows];
        loadMap(inputPath, rows, cols);

        int[][] newMap = new int[cols][rows];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int tile = mapTileNum[x][y];
                if (tile == GRASS || tile == TREE) {
                    newMap[x][y] = determineEdgeTileGrass(x, y);
                } else if (tile == WOOD) {
                    newMap[x][y] = determineEdgeTileWood(x, y);
                } else {
                    newMap[x][y] = tile;
                }
            }
        }

        saveMap(outputPath, newMap, rows, cols);
    }

    private int determineEdgeTileWood(int x, int y) {
        boolean up = isClear(x, y - 1);
        boolean down = isClear(x, y + 1);
        boolean left = isClear(x - 1, y);
        boolean right = isClear(x + 1, y);

        boolean upLeft = isClear(x - 1, y - 1);
        boolean upRight = isClear(x + 1, y - 1);
        boolean downLeft = isClear(x - 1, y + 1);
        boolean downRight = isClear(x + 1, y + 1);

        // --- Outer Corners ---
        if (up && left && !down && !right) return 60;   // top-left outer
        if (up && right && !down && !left) return 63;   // top-right outer
        if (down && left && !up && !right) return 61;   // bottom-left outer
        if (down && right && !up && !left) return 62;   // bottom-right outer

        // --- Straight Edges ---
        if (up && !down) return 58;       // water above
        if (down && !up) return 53;       // water below
        if (left && !right) return 56;    // water left
        if (right && !left) return 55;    // water right

        // --- Inner Corners (water diagonal, but not directly) ---
        if (upLeft && !up && !left) return 59;          // inner top-left
        if (upRight && !up && !right) return 57;        // inner top-right
        if (downLeft && !down && !left) return 54;      // inner bottom-left
        if (downRight && !down && !right) return 52;    // inner bottom-right

        // Surrounded -> still grass
        return WOOD;
    }

    private boolean isClear(int x, int y) {
        if (x < 0 || y < 0 || x >= mapTileNum.length || y >= mapTileNum[0].length) {
            return false;
        }
        return mapTileNum[x][y] == CLEAR;
    }


    private int determineEdgeTileGrass(int x, int y) {
        boolean up = isWater(x, y - 1);
        boolean down = isWater(x, y + 1);
        boolean left = isWater(x - 1, y);
        boolean right = isWater(x + 1, y);

        boolean upLeft = isWater(x - 1, y - 1);
        boolean upRight = isWater(x + 1, y - 1);
        boolean downLeft = isWater(x - 1, y + 1);
        boolean downRight = isWater(x + 1, y + 1);

        // --- Outer Corners ---
        if (up && left && !down && !right) return 22;   // top-left outer
        if (up && right && !down && !left) return 23;   // top-right outer
        if (down && left && !up && !right) return 24;   // bottom-left outer
        if (down && right && !up && !left) return 25;   // bottom-right outer

        // --- Straight Edges ---
        if (up && !down) return 20;       // water above
        if (down && !up) return 15;       // water below
        if (left && !right) return 18;    // water left
        if (right && !left) return 17;    // water right

        // --- Inner Corners (water diagonal, but not directly) ---
        if (upLeft && !up && !left) return 21;          // inner top-left
        if (upRight && !up && !right) return 19;        // inner top-right
        if (downLeft && !down && !left) return 16;      // inner bottom-left
        if (downRight && !down && !right) return 14;    // inner bottom-right

        // Surrounded -> still grass
        return GRASS;
    }

    private boolean isWater(int x, int y) {
        if (x < 0 || y < 0 || x >= mapTileNum.length || y >= mapTileNum[0].length) {
            return false;
        }
        return mapTileNum[x][y] == WATER;
    }

    private void loadMap(String path, int rows, int cols) {
        try (InputStream is = getClass().getResourceAsStream(path);
             Scanner readMap = new Scanner(is)) {

            if (is == null) {
                throw new IllegalArgumentException("Map file not found: " + path);
            }

            int row = 0;
            while (row < rows && readMap.hasNextLine()) {
                String line = readMap.nextLine();
                String[] sections = line.trim().split("\\s+");
                for (int col = 0; col < cols; col++) {
                    mapTileNum[col][row] = Integer.parseInt(sections[col]);
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveMap(String path, int[][] newMap, int rows, int cols) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    if (newMap[x][y] == 0) {
                        pw.print("00");
                    } else {
                        pw.print(newMap[x][y]);
                    }
                    if (x < cols - 1) pw.print(" ");
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LevelCreator(
            INPUT_PATH,
            OUTPUT_PATH,
            Constants.MAX_WORLD_ROW,
            Constants.MAX_WORLD_COL
        );
    }
}
