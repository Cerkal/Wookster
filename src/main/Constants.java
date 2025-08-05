package main;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    // Settings
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int FPS = 60;
    public static final int MILLISECOND = 1000000;
    public static final long NANO_SECOND = 1000000000;
    
    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    // TEMP WORLD SETTINGS
    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;
    public static final int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_COL;
    public static final int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_ROW;

    // In milliseconds
    public static final int MESSAGE_DISPLAY_TIME = 3000;
    public static final int MESSAGE_DISPLAY_SLOW_TYPE_TIME = 50;

    // Fonts
    public static final float FONT_SIZE = 20f;
    public static final float FONT_SIZE_MEDIUM = 25f;
    public static final float FONT_SIZE_LARGE = 50f;

    // Title Menu Screen
    public static final String GAME_TITLE = "BLAM!";
    public static final String GAME_PAUSED = "Game Paused";
    public static final String GAME_TITLE_SCREEN_NEW_GAME = "New Game";
    public static final String GAME_TITLE_SCREEN_LOAD_GAME = "Load Game";
    public static final String GAME_TITLE_SCREEN_QUIT_GAME = "Quit Game";
    public static final String GAME_TITLE_SELECTOR = ">";
    public static final List<String> GAME_TITLE_MENU = new ArrayList<>(
        List.of(
            GAME_TITLE_SCREEN_NEW_GAME,
            GAME_TITLE_SCREEN_LOAD_GAME,
            GAME_TITLE_SCREEN_QUIT_GAME
        )
    );

    public static final String OBJECT_DOOR = "Door";
    public static final String OBJECT_DOOR_IMAGE = "./src/res/objects/door.png";
    public static final String OBJECT_KEY = "Key";
    public static final String OBJECT_KEY_IMAGE = "./src/res/objects/key.png";
    public static final String OBJECT_CHEST = "Chest";
    public static final String OBJECT_CHEST_IMAGE = "./src/res/objects/chest.png";
    public static final String OBJECT_ARROW = "Arrow";
    public static final String OBJECT_ARROW_IMAGE = "./src/res/objects/heart_blank.png";
    public static final String OBJECT_GREEN_POTION = "Green Potion";
    public static final String OBJECT_GREEN_POTION_IMAGE = "./src/res/objects/potion_green.png";
    public static final String OBJECT_RED_POTION = "Red Potion";
    public static final String OBJECT_RED_POTION_IMAGE = "./src/res/objects/potion_red.png";

    public static final String INVENTORY_ADDED_MESSAGE = " added to inventory";
    public static final String INVENTORY_REMOVED_MESSAGE = " removed from inventory";

    // Hero
    public static final String PLAYER_IMAGE_UP_0 = "./src/res/player/wookster_up_0.png";
    public static final String PLAYER_IMAGE_UP_1 = "./src/res/player/wookster_up_1.png";
    public static final String PLAYER_IMAGE_DOWN_0 = "./src/res/player/wookster_down_0.png";
    public static final String PLAYER_IMAGE_DOWN_1 = "./src/res/player/wookster_down_1.png";
    public static final String PLAYER_IMAGE_LEFT_0 = "./src/res/player/wookster_left_0.png";
    public static final String PLAYER_IMAGE_LEFT_1 = "./src/res/player/wookster_left_1.png";
    public static final String PLAYER_IMAGE_RIGHT_0 = "./src/res/player/wookster_right_0.png";
    public static final String PLAYER_IMAGE_RIGHT_1 = "./src/res/player/wookster_right_1.png";

    // NPC
    public static final String TROOPER_IMAGE_UP_0 = "./src/res/npc/trooper_up_0.png";
    public static final String TROOPER_IMAGE_UP_1 = "./src/res/npc/trooper_up_1.png";
    public static final String TROOPER_IMAGE_DOWN_0 = "./src/res/npc/trooper_down_0.png";
    public static final String TROOPER_IMAGE_DOWN_1 = "./src/res/npc/trooper_down_1.png";
    public static final String TROOPER_IMAGE_LEFT_0 = "./src/res/npc/trooper_left_0.png";
    public static final String TROOPER_IMAGE_LEFT_1 = "./src/res/npc/trooper_left_1.png";
    public static final String TROOPER_IMAGE_RIGHT_0 = "./src/res/npc/trooper_right_0.png";
    public static final String TROOPER_IMAGE_RIGHT_1 = "./src/res/npc/trooper_right_1.png";

    // Droids
    public static final String DROIDS_IMAGE_DOWN_0 = "./src/res/npc/droids.png";

    // Effect
    public static final String SPELL_EFFECT_SPARKLE_0 = "./src/res/effects/sparkle_01.png";
    public static final String SPELL_EFFECT_SPARKLE_1 = "./src/res/effects/sparkle_02.png";
    public static final String SPELL_EFFECT_SPARKLE_2 = "./src/res/effects/sparkle_03.png";

    public static final String WORLD_00 = "./src/res/maps/world00.txt";

    // Fonts
    public static final String FONT_ARI = "src/res/fonts/ari-w9500.ttf";
    public static final String FONT_ANALOG = "src/res/fonts/analog.ttf";
    public static final String FONT_DELTARUNE = "src/res/fonts/deltarune.ttf";
    public static final String FONT_DOS = "src/res/fonts/dos.ttf";
    public static final String FONT_LARGE = "src/res/fonts/pixelout.ttf";

    // World tiles
    public static final String TILE_GRASS_00 = "./src/res/tile/grass00.png";
    public static final String TILE_GRASS_01 = "./src/res/tile/grass01.png";
    public static final String TILE_EARTH = "./src/res/tile/earth.png";
    public static final String TILE_WALL = "./src/res/tile/wall.png";
    public static final String TILE_TREE = "./src/res/tile/tree.png";
    
    // Water tiles
    public static final String TILE_WATER_00 = "./src/res/tile/water00.png";
    public static final String TILE_WATER_01 = "./src/res/tile/water01.png";
    public static final String TILE_WATER_02 = "./src/res/tile/water02.png";
    public static final String TILE_WATER_03 = "./src/res/tile/water03.png";
    public static final String TILE_WATER_04 = "./src/res/tile/water04.png";
    public static final String TILE_WATER_05 = "./src/res/tile/water05.png";
    public static final String TILE_WATER_06 = "./src/res/tile/water06.png";
    public static final String TILE_WATER_07 = "./src/res/tile/water07.png";
    public static final String TILE_WATER_08 = "./src/res/tile/water08.png";
    public static final String TILE_WATER_09 = "./src/res/tile/water09.png";
    public static final String TILE_WATER_10 = "./src/res/tile/water10.png";
    public static final String TILE_WATER_11 = "./src/res/tile/water11.png";
    public static final String TILE_WATER_12 = "./src/res/tile/water12.png";
    public static final String TILE_WATER_13 = "./src/res/tile/water13.png";
    
    // Anaimted water tiles
    public static final String TILE_WATER_A01 = "./src/res/tile/water01_a01.png";
    public static final String TILE_WATER_A02 = "./src/res/tile/water01_a02.png";
    public static final String TILE_WATER_A03 = "./src/res/tile/water01_a03.png";

    // Road tiles
    public static final String TILE_ROAD_00 = "./src/res/tile/road00.png";
    public static final String TILE_ROAD_01 = "./src/res/tile/road01.png";
    public static final String TILE_ROAD_02 = "./src/res/tile/road02.png";
    public static final String TILE_ROAD_03 = "./src/res/tile/road03.png";
    public static final String TILE_ROAD_04 = "./src/res/tile/road04.png";
    public static final String TILE_ROAD_05 = "./src/res/tile/road05.png";
    public static final String TILE_ROAD_06 = "./src/res/tile/road06.png";
    public static final String TILE_ROAD_07 = "./src/res/tile/road07.png";
    public static final String TILE_ROAD_08 = "./src/res/tile/road08.png";
    public static final String TILE_ROAD_09 = "./src/res/tile/road09.png";
    public static final String TILE_ROAD_10 = "./src/res/tile/road10.png";
    public static final String TILE_ROAD_11 = "./src/res/tile/road11.png";
    public static final String TILE_ROAD_12 = "./src/res/tile/road12.png";

    public static final String SOUND_BG_01 = "./src/res/sounds/BlueBoyAdventure.wav";
    public static final String SOUND_COIN = "./src/res/sounds/coin.wav";
    public static final String SOUND_POWER_UP = "./src/res/sounds/powerup.wav";
    public static final String SOUND_UNLOCK = "./src/res/sounds/unlock.wav";
    public static final String SOUND_LOCK = "./src/res/sounds/lockeddoor.wav";
    public static final String SOUND_FLARE = "./src/res/sounds/fanflare.wav";
    public static final String SOUND_TEXT = "./src/res/sounds/text.wav";
    public static final String SOUND_TEXT_LONG = "./src/res/sounds/text_long.wav";
    public static final String SOUND_HURT = "./src/res/sounds/hurt.wav";
    public static final String SOUND_STATIC = "./src/res/sounds/static.wav";
    public static final String SOUND_TITLE_SCREEN = "./src/res/sounds/starwars_theme.wav";
    public static final String SOUND_CURSOR = "./src/res/sounds/cursor.wav";
    public static final String SOUND_LEVELUP = "./src/res/sounds/levelup.wav";
    
    public static final List<String> SOUND_LIST = new ArrayList<>(
        List.of(
            SOUND_BG_01,
            SOUND_COIN,
            SOUND_POWER_UP,
            SOUND_UNLOCK,
            SOUND_LOCK,
            SOUND_FLARE,
            SOUND_TEXT,
            SOUND_TEXT_LONG,
            SOUND_HURT,
            SOUND_STATIC,
            SOUND_TITLE_SCREEN,
            SOUND_CURSOR,
            SOUND_LEVELUP
        )
    );

    public static final String RANDOM_HURT_DIALOGUE_01 = "A spider bites you, but it appologizes.";
    public static final String RANDOM_HURT_DIALOGUE_02 = "Damn dirty bees! You pull out the stinger.";
    public static final String RANDOM_HURT_DIALOGUE_03 = "You step in ewok poo. Gross.";
    public static final String RANDOM_HURT_DIALOGUE_04 = "You step on a nail. Ouch.";

    public static final List<String> RNADOM_HURT_DIALOGUE = new ArrayList<>(
        List.of(
            RANDOM_HURT_DIALOGUE_01,
            RANDOM_HURT_DIALOGUE_02,
            RANDOM_HURT_DIALOGUE_03,
            RANDOM_HURT_DIALOGUE_04
        )
    );

    public static final String FONT_ARIAL = "Arial";
}
