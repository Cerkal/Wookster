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
    public static final String OBJECT_DOOR_IMAGE = "/res/objects/door.png";
    public static final String OBJECT_KEY = "Key";
    public static final String OBJECT_KEY_IMAGE = "/res/objects/key.png";
    public static final String OBJECT_CHEST = "Chest";
    public static final String OBJECT_CHEST_IMAGE = "/res/objects/chest.png";
    public static final String OBJECT_GREEN_POTION = "Green Potion";
    public static final String OBJECT_GREEN_POTION_IMAGE = "/res/objects/potion_green.png";
    public static final String OBJECT_RED_POTION = "Red Potion";
    public static final String OBJECT_RED_POTION_IMAGE = "/res/objects/potion_red.png";

    public static final String INVENTORY_ADDED_MESSAGE = " added to inventory";
    public static final String INVENTORY_REMOVED_MESSAGE = " removed from inventory";

    // Hero
    public static final String PLAYER_IMAGE_UP_0 = "/res/player/wookster_up_0.png";
    public static final String PLAYER_IMAGE_UP_1 = "/res/player/wookster_up_1.png";
    public static final String PLAYER_IMAGE_DOWN_0 = "/res/player/wookster_down_0.png";
    public static final String PLAYER_IMAGE_DOWN_1 = "/res/player/wookster_down_1.png";
    public static final String PLAYER_IMAGE_LEFT_0 = "/res/player/wookster_left_0.png";
    public static final String PLAYER_IMAGE_LEFT_1 = "/res/player/wookster_left_1.png";
    public static final String PLAYER_IMAGE_RIGHT_0 = "/res/player/wookster_right_0.png";
    public static final String PLAYER_IMAGE_RIGHT_1 = "/res/player/wookster_right_1.png";

    // NPC
    public static final String TROOPER_IMAGE_UP_0 = "/res/npc/trooper_up_0.png";
    public static final String TROOPER_IMAGE_UP_1 = "/res/npc/trooper_up_1.png";
    public static final String TROOPER_IMAGE_DOWN_0 = "/res/npc/trooper_down_0.png";
    public static final String TROOPER_IMAGE_DOWN_1 = "/res/npc/trooper_down_1.png";
    public static final String TROOPER_IMAGE_LEFT_0 = "/res/npc/trooper_left_0.png";
    public static final String TROOPER_IMAGE_LEFT_1 = "/res/npc/trooper_left_1.png";
    public static final String TROOPER_IMAGE_RIGHT_0 = "/res/npc/trooper_right_0.png";
    public static final String TROOPER_IMAGE_RIGHT_1 = "/res/npc/trooper_right_1.png";

    // Droids
    public static final String DROIDS_IMAGE_DOWN_0 = "/res/npc/droids.png";

    // Effects
    public static final String SPELL_EFFECT_SPARKLE_0 = "/res/effects/sparkle_01.png";
    public static final String SPELL_EFFECT_SPARKLE_1 = "/res/effects/sparkle_02.png";
    public static final String SPELL_EFFECT_SPARKLE_2 = "/res/effects/sparkle_03.png";
    
    public static final String EFFECT_BLOOD_0 = "/res/effects/blood_01.png";
    public static final String EFFECT_BLOOD_1 = "/res/effects/blood_02.png";
    public static final String EFFECT_BLOOD_2 = "/res/effects/blood_03.png";


    // Weapon
    public static final String WEAPON_PROJECTILE_ARROW = "/res/objects/arrow.png";

    public static final String WORLD_00 = "/res/maps/world00.txt";

    // Fonts
    public static final String FONT_DOS = "/res/fonts/dos.ttf";

    // World tiles
    public static final String TILE_GRASS_00 = "/res/tile/grass00.png";
    public static final String TILE_GRASS_01 = "/res/tile/grass01.png";
    public static final String TILE_EARTH = "/res/tile/earth.png";
    public static final String TILE_WALL = "/res/tile/wall.png";
    public static final String TILE_TREE = "/res/tile/tree.png";
    
    // Water tiles
    public static final String TILE_WATER_00 = "/res/tile/water00.png";
    public static final String TILE_WATER_01 = "/res/tile/water01.png";
    public static final String TILE_WATER_02 = "/res/tile/water02.png";
    public static final String TILE_WATER_03 = "/res/tile/water03.png";
    public static final String TILE_WATER_04 = "/res/tile/water04.png";
    public static final String TILE_WATER_05 = "/res/tile/water05.png";
    public static final String TILE_WATER_06 = "/res/tile/water06.png";
    public static final String TILE_WATER_07 = "/res/tile/water07.png";
    public static final String TILE_WATER_08 = "/res/tile/water08.png";
    public static final String TILE_WATER_09 = "/res/tile/water09.png";
    public static final String TILE_WATER_10 = "/res/tile/water10.png";
    public static final String TILE_WATER_11 = "/res/tile/water11.png";
    public static final String TILE_WATER_12 = "/res/tile/water12.png";
    public static final String TILE_WATER_13 = "/res/tile/water13.png";
    
    // Anaimted water tiles
    public static final String TILE_WATER_A01 = "/res/tile/water01_a01.png";
    public static final String TILE_WATER_A02 = "/res/tile/water01_a02.png";
    public static final String TILE_WATER_A03 = "/res/tile/water01_a03.png";

    // Road tiles
    public static final String TILE_ROAD_00 = "/res/tile/road00.png";
    public static final String TILE_ROAD_01 = "/res/tile/road01.png";
    public static final String TILE_ROAD_02 = "/res/tile/road02.png";
    public static final String TILE_ROAD_03 = "/res/tile/road03.png";
    public static final String TILE_ROAD_04 = "/res/tile/road04.png";
    public static final String TILE_ROAD_05 = "/res/tile/road05.png";
    public static final String TILE_ROAD_06 = "/res/tile/road06.png";
    public static final String TILE_ROAD_07 = "/res/tile/road07.png";
    public static final String TILE_ROAD_08 = "/res/tile/road08.png";
    public static final String TILE_ROAD_09 = "/res/tile/road09.png";
    public static final String TILE_ROAD_10 = "/res/tile/road10.png";
    public static final String TILE_ROAD_11 = "/res/tile/road11.png";
    public static final String TILE_ROAD_12 = "/res/tile/road12.png";

    public static final String SOUND_BG_01 = "/res/sounds/backgroundmusic.wav";
    public static final String SOUND_COIN = "/res/sounds/coin.wav";
    public static final String SOUND_POWER_UP = "/res/sounds/powerup.wav";
    public static final String SOUND_UNLOCK = "/res/sounds/unlock.wav";
    public static final String SOUND_LOCK = "/res/sounds/lockeddoor.wav";
    public static final String SOUND_FLARE = "/res/sounds/fanflare.wav";
    public static final String SOUND_TEXT = "/res/sounds/text.wav";
    public static final String SOUND_HURT = "/res/sounds/hurt.wav";
    public static final String SOUND_STATIC = "/res/sounds/static.wav";
    public static final String SOUND_TITLE_SCREEN = "/res/sounds/starwarstheme.wav";
    public static final String SOUND_CURSOR = "/res/sounds/cursor.wav";
    public static final String SOUND_LEVELUP = "/res/sounds/levelup.wav";
    public static final String SOUND_ARROW = "/res/sounds/arrow.wav";
    
    public static final List<String> SOUND_LIST = new ArrayList<>(
        List.of(
            SOUND_BG_01,
            SOUND_COIN,
            SOUND_POWER_UP,
            SOUND_UNLOCK,
            SOUND_LOCK,
            SOUND_FLARE,
            SOUND_TEXT,
            SOUND_HURT,
            SOUND_STATIC,
            SOUND_TITLE_SCREEN,
            SOUND_CURSOR,
            SOUND_LEVELUP,
            SOUND_ARROW
        )
    );

    public static final String RANDOM_HURT_DIALOGUE_01 = "A spider bites you, but it appologizes.";
    public static final String RANDOM_HURT_DIALOGUE_02 = "Damn dirty bees! You pull out the stinger.";
    public static final String RANDOM_HURT_DIALOGUE_03 = "You step in ewok poo. Gross.";
    public static final String RANDOM_HURT_DIALOGUE_04 = "You step on a nail. Ouch.";

    public static final String POTION_GOOD = "The potion is spicy and nice.";
    public static final String POTION_BAD = "The potion is wet and smelly.";

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
