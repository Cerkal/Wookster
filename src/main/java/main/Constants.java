package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import objects.SuperObject.ObjectType;
import objects.weapons.Weapon.WeaponType;

public class Constants {

    // Settings
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int FPS = 60;
    public static final int MILLISECOND = 1000000;
    public static final long NANO_SECOND = 1000000000;
    
    public static final int MAX_SCREEN_COL = 22;
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public static final int MIN_LOADING = 1000; // ms
    public static final int DOT_UPDATE_INTERVAL = 100; // ms

    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;
    public static final int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_COL;
    public static final int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_ROW;

    public static final int NEW_LINE_SIZE = TILE_SIZE - 5;

    // Background Size
    public static final int BACKGROUND_WIDTH = 2800;
    public static final int BACKGROUND_HEIGHT = 2400;

    // Full Screen
    public static final int FULL_SCREEN_WIDTH = SCREEN_WIDTH;
    public static final int FULL_SCREEN_HEIGHT = SCREEN_HEIGHT;

    // In milliseconds
    public static final int MESSAGE_DISPLAY_TIME = 3000;
    public static final int MESSAGE_DISPLAY_SLOW_TYPE_TIME = 50;

    // Fonts
    public static final float FONT_SIZE_SMALL = 15f;
    public static final float FONT_SIZE = 20f;
    public static final float FONT_SIZE_MEDIUM = 25f;
    public static final float FONT_SIZE_LARGE = 50f;

    // Title Menu Screen
    public static final String GAME_TITLE = "BLAM!";
    public static final String GAME_PAUSED = "Game Paused";
    public static final String GAME_DEATH = "Dead";
    public static final String GAME_LOADING = "Loading";
    public static final String GAME_TITLE_SCREEN_NEW_GAME = "New Game";
    public static final String GAME_TITLE_SCREEN_SAVE_GAME = "Save Game";
    public static final String GAME_TITLE_SCREEN_LOAD_GAME = "Load Game";
    public static final String GAME_TITLE_SCREEN_CONTROLS = "Controls";
    public static final String GAME_TITLE_SCREEN_OPTIONS = "Options";
    public static final String GAME_TITLE_SCREEN_QUIT_GAME = "Quit Game";
    public static final String GAME_TITLE_SELECTOR = ">";
    public static final List<String> GAME_TITLE_MENU = new ArrayList<>(
        List.of(
            GAME_TITLE_SCREEN_NEW_GAME,
            GAME_TITLE_SCREEN_SAVE_GAME,
            GAME_TITLE_SCREEN_LOAD_GAME,
            // GAME_TITLE_SCREEN_CONTROLS,
            // GAME_TITLE_SCREEN_OPTIONS,
            GAME_TITLE_SCREEN_QUIT_GAME
        )
    );

    // Testing for controls
    public static final HashMap<String, String> GAME_CONTROLS_LIST = new HashMap<>() {{
        put("Move", "[W] [S] [A] [D]");
        put("Attack / Talk", "[SPACEBAR]");
        put("Inventory", "[I]");
        put("Pause", "[ESCAPE]");
    }};
    public static final HashMap<String, Integer> GAME_OPTIONS_LIST = new HashMap<>() {{
        put("Sound", 1);
    }};
    

    // Inventory Tabs

    public static final String INVENTORY = "Inventory";
    public static final String QUESTS = "Quests";

    public static final List<String> INVENTORY_TABS = Arrays.asList(
        INVENTORY,
        QUESTS
    );
    public static final int INVENTORY_INDEX = Constants.INVENTORY_TABS.indexOf(Constants.INVENTORY);
    public static final int QUEST_INDEX = Constants.INVENTORY_TABS.indexOf(Constants.QUESTS);
    public static final int INVENTORY_ICON_SIZE = Constants.TILE_SIZE * 2;

    // Files
    public static final String SAVE_FILE = "save.json";

    // Objects
    public static final String OBJECT_DOOR_IMAGE = "/objects/door.png";
    public static final String OBJECT_KEY_IMAGE = "/objects/key.png";
    public static final String OBJECT_CHEST_IMAGE = "/objects/chest.png";
    public static final String OBJECT_GREEN_POTION_IMAGE = "/objects/potion_green.png";
    public static final String OBJECT_PURPLE_POTION_IMAGE = "/objects/potion_purple.png";
    public static final String OBJECT_SIGN_IMAGE = "/objects/sign.png";
    public static final String OBJECT_AMMO_ARROWS_IMAGE = "/objects/arrows.png";
    public static final String OBJECT_AMMO_LASERS_IMAGE = "/objects/lasers.png";
    public static final String OBJECT_WEAPON_BLASTER = "/objects/weapon_blaster.png";
    public static final String OBJECT_WEAPON_CROSSBOW = "/objects/weapon_bow.png";
    public static final String OBJECT_WEAPON_SWORD = "/objects/weapon_sword.png";

    // Hero
    public static final String PLAYER_IMAGE_UP_0 = "/player/wookster_up_0.png";
    public static final String PLAYER_IMAGE_UP_1 = "/player/wookster_up_1.png";
    public static final String PLAYER_IMAGE_DOWN_0 = "/player/wookster_down_0.png";
    public static final String PLAYER_IMAGE_DOWN_1 = "/player/wookster_down_1.png";
    public static final String PLAYER_IMAGE_LEFT_0 = "/player/wookster_left_0.png";
    public static final String PLAYER_IMAGE_LEFT_1 = "/player/wookster_left_1.png";
    public static final String PLAYER_IMAGE_RIGHT_0 = "/player/wookster_right_0.png";
    public static final String PLAYER_IMAGE_RIGHT_1 = "/player/wookster_right_1.png";

    public static final String PLAYER_IMAGE_CROSSBOW_UP_0 = "/player/wookster_crossbow_up_0.png";
    public static final String PLAYER_IMAGE_CROSSBOW_UP_1 = "/player/wookster_crossbow_up_1.png";
    public static final String PLAYER_IMAGE_CROSSBOW_DOWN_0 = "/player/wookster_crossbow_down_0.png";
    public static final String PLAYER_IMAGE_CROSSBOW_DOWN_1 = "/player/wookster_crossbow_down_1.png";
    public static final String PLAYER_IMAGE_CROSSBOW_LEFT_0 = "/player/wookster_crossbow_left_0.png";
    public static final String PLAYER_IMAGE_CROSSBOW_LEFT_1 = "/player/wookster_crossbow_left_1.png";
    public static final String PLAYER_IMAGE_CROSSBOW_RIGHT_0 = "/player/wookster_crossbow_right_0.png";
    public static final String PLAYER_IMAGE_CROSSBOW_RIGHT_1 = "/player/wookster_crossbow_right_1.png";

    public static final String PLAYER_IMAGE_BLASTER_UP_0 = "/player/wookster_blaster_up_0.png";
    public static final String PLAYER_IMAGE_BLASTER_UP_1 = "/player/wookster_blaster_up_1.png";
    public static final String PLAYER_IMAGE_BLASTER_DOWN_0 = "/player/wookster_blaster_down_0.png";
    public static final String PLAYER_IMAGE_BLASTER_DOWN_1 = "/player/wookster_blaster_down_1.png";
    public static final String PLAYER_IMAGE_BLASTER_LEFT_0 = "/player/wookster_blaster_left_0.png";
    public static final String PLAYER_IMAGE_BLASTER_LEFT_1 = "/player/wookster_blaster_left_1.png";
    public static final String PLAYER_IMAGE_BLASTER_RIGHT_0 = "/player/wookster_blaster_right_0.png";
    public static final String PLAYER_IMAGE_BLASTER_RIGHT_1 = "/player/wookster_blaster_right_1.png";

    public static final String PLAYER_IMAGE_FIST_UP_0 = "/player/wookster_fist_up_0.png";
    public static final String PLAYER_IMAGE_FIST_UP_1 = "/player/wookster_fist_up_1.png";
    public static final String PLAYER_IMAGE_FIST_DOWN_0 = "/player/wookster_fist_down_0.png";
    public static final String PLAYER_IMAGE_FIST_DOWN_1 = "/player/wookster_fist_down_1.png";
    public static final String PLAYER_IMAGE_FIST_LEFT_0 = "/player/wookster_fist_left_0.png";
    public static final String PLAYER_IMAGE_FIST_LEFT_1 = "/player/wookster_fist_left_1.png";
    public static final String PLAYER_IMAGE_FIST_RIGHT_0 = "/player/wookster_fist_right_0.png";
    public static final String PLAYER_IMAGE_FIST_RIGHT_1 = "/player/wookster_fist_right_1.png";

    public static final String PLAYER_IMAGE_SWORD_UP_0 = "/player/wookster_sword_up_0.png";
    public static final String PLAYER_IMAGE_SWORD_UP_1 = "/player/wookster_sword_up_1.png";
    public static final String PLAYER_IMAGE_SWORD_DOWN_0 = "/player/wookster_sword_down_0.png";
    public static final String PLAYER_IMAGE_SWORD_DOWN_1 = "/player/wookster_sword_down_1.png";
    public static final String PLAYER_IMAGE_SWORD_LEFT_0 = "/player/wookster_sword_left_0.png";
    public static final String PLAYER_IMAGE_SWORD_LEFT_1 = "/player/wookster_sword_left_1.png";
    public static final String PLAYER_IMAGE_SWORD_RIGHT_0 = "/player/wookster_sword_right_0.png";
    public static final String PLAYER_IMAGE_SWORD_RIGHT_1 = "/player/wookster_sword_right_1.png";

    public static final String PLAYER_IMAGE_DEAD = "/player/wookster_dead.png";

    // NPC
    public static final String TROOPER_IMAGE_UP_0 = "/npc/trooper_up_0.png";
    public static final String TROOPER_IMAGE_UP_1 = "/npc/trooper_up_1.png";
    public static final String TROOPER_IMAGE_DOWN_0 = "/npc/trooper_down_0.png";
    public static final String TROOPER_IMAGE_DOWN_1 = "/npc/trooper_down_1.png";
    public static final String TROOPER_IMAGE_LEFT_0 = "/npc/trooper_left_0.png";
    public static final String TROOPER_IMAGE_LEFT_1 = "/npc/trooper_left_1.png";
    public static final String TROOPER_IMAGE_RIGHT_0 = "/npc/trooper_right_0.png";
    public static final String TROOPER_IMAGE_RIGHT_1 = "/npc/trooper_right_1.png";
    public static final String TROOPER_IMAGE_DEAD = "/npc/trooper_dead.png";

    public static final String TROOPER_IMAGE_IDEL_UP_00 = "/npc/trooper_idel_up.png";
    public static final String TROOPER_IMAGE_IDEL_DOWN_00 = "/npc/trooper_idel_down.png";
    public static final String TROOPER_IMAGE_IDEL_DOWN_01 = "/npc/trooper_idel_down_alt.png";
    public static final String TROOPER_IMAGE_IDEL_RIGHT_00 = "/npc/trooper_idel_right.png";
    public static final String TROOPER_IMAGE_IDEL_LEFT_00 = "/npc/trooper_idel_left.png";

    public static final String TROOPER_IMAGE_FIST_UP_00 = "/npc/trooper_up_0.png";
    public static final String TROOPER_IMAGE_FIST_UP_01 = "/npc/trooper_up_1.png";
    public static final String TROOPER_IMAGE_FIST_DOWN_00 = "/npc/trooper_down_fist_0.png";
    public static final String TROOPER_IMAGE_FIST_DOWN_01 = "/npc/trooper_down_fist_1.png";
    public static final String TROOPER_IMAGE_FIST_RIGHT_00 = "/npc/trooper_right_fist_0.png";
    public static final String TROOPER_IMAGE_FIST_RIGHT_01 = "/npc/trooper_right_fist_1.png";
    public static final String TROOPER_IMAGE_FIST_LEFT_00 = "/npc/trooper_left_fist_0.png";
    public static final String TROOPER_IMAGE_FIST_LEFT_01 = "/npc/trooper_left_fist_1.png";

    // Animals
    public static final String PIG_IMAGE_UP_00 = "/animals/pigs/pig_up_0.png";
    public static final String PIG_IMAGE_UP_01 = "/animals/pigs/pig_up_1.png";
    public static final String PIG_IMAGE_DOWN_00 = "/animals/pigs/pig_down_0.png";
    public static final String PIG_IMAGE_DOWN_01 = "/animals/pigs/pig_down_1.png";
    public static final String PIG_IMAGE_RIGHT_00 = "/animals/pigs/pig_right_0.png";
    public static final String PIG_IMAGE_RIGHT_01 = "/animals/pigs/pig_right_1.png";
    public static final String PIG_IMAGE_LEFT_00 = "/animals/pigs/pig_left_0.png";
    public static final String PIG_IMAGE_LEFT_01 = "/animals/pigs/pig_left_1.png";
    public static final String PIG_IMAGE_DEAD = "/animals/pigs/pig_dead.png";

    // Hat
    public static final String WOOKSER_MOM_HAT = "/player/mom_hat.png";

    // Droids
    public static final String DROIDS_IMAGE_DOWN_0 = "/npc/droids.png";

    // Oldman
    public static final String OLD_MAN_UP_0 = "/npc/oldman_up.png";
    public static final String OLD_MAN_DOWN_0 = "/npc/oldman_down.png";
    public static final String OLD_MAN_LEFT_0 = "/npc/oldman_left.png";
    public static final String OLD_MAN_RIGHT_0 = "/npc/oldman_right.png";

    // Effects
    public static final String SPELL_EFFECT_SPARKLE_0 = "/effects/sparkle_01.png";
    public static final String SPELL_EFFECT_SPARKLE_1 = "/effects/sparkle_02.png";
    public static final String SPELL_EFFECT_SPARKLE_2 = "/effects/sparkle_03.png";
    
    public static final String EFFECT_BLOOD_0 = "/effects/blood_01.png";
    public static final String EFFECT_BLOOD_1 = "/effects/blood_02.png";
    public static final String EFFECT_BLOOD_2 = "/effects/blood_03.png";
    public static final String EFFECT_POOP = "/effects/poop.png";

    public static final String EFFECT_ALERT = "/effects/alert.png";
    public static final String EFFECT_ALERT_LIGHT = "/effects/alert_light.png";
    public static final String EFFECT_UKNOWN = "/effects/questionmark.png";

    // Weapon
    public static final String WEAPON_PROJECTILE_ARROW = "/objects/arrow.png";
    public static final String WEAPON_PROJECTILE_LASER = "/objects/laser.png";

    // Maps
    public static final String WORLD_BUILDER = "/maps/world_builder.txt";
    public static final String WORLD_00 = "/maps/world_00.txt";
    public static final String WORLD_01 = "/maps/world_01.txt";
    public static final String WORLD_02 = "/maps/world_02.txt";

    // Backrounds
    public static final String DEFAULT_BACKGROUND = "/backgrounds/output_image_test.png";
    public static final String WORLD_00_BACKGROUND = "/backgrounds/world_00.png";

    // Fonts
    public static final String FONT_DOS = "/fonts/dos.ttf";

    // World tiles
    public static final String TILE_CLEAR = "/tile/clear.png";
    public static final String TILE_GRASS_00 = "/tile/grass00.png";
    public static final String TILE_GRASS_01 = "/tile/grass01.png";
    public static final String TILE_EARTH = "/tile/earth.png";
    public static final String TILE_WALL = "/tile/wall.png";
    public static final String TILE_TREE_00 = "/tile/tree.png";
    public static final String TILE_TREE_01 = "/tile/tree_alt.png";
    public static final String TILE_TREE_02 = "/tile/tree_alt2.png";
    public static final String TILE_TREE_FALL = "/tile/tree_fall.png";

    // Bridge
    public static final String TILE_BRIDGE_00 = "/tile/bridge00.png";
    public static final String TILE_BRIDGE_01 = "/tile/bridge01.png";
    // Join
    public static final String TILE_BRIDGE_JOIN_00 = "/tile/bridge_join_00.png";
    public static final String TILE_BRIDGE_JOIN_01 = "/tile/bridge_join_01.png";
    public static final String TILE_BRIDGE_JOIN_02 = "/tile/bridge_join_02.png";
    public static final String TILE_BRIDGE_JOIN_03 = "/tile/bridge_join_03.png";
    // Right
    public static final String TILE_BRIDGE_SIDE_00 = "/tile/bridge_side_00.png";
    public static final String TILE_BRIDGE_SIDE_01 = "/tile/bridge_side_01.png";
    public static final String TILE_BRIDGE_SIDE_02 = "/tile/bridge_side_02.png";

    // Tree Large
    public static final String TILE_TREE_LARGE_00 = "/tile/tree_large_00.png";
    public static final String TILE_TREE_LARGE_01 = "/tile/tree_large_01.png";
    public static final String TILE_TREE_LARGE_02 = "/tile/tree_large_02.png";
    public static final String TILE_TREE_LARGE_03 = "/tile/tree_large_03.png";
    // Tree Side
    public static final String TILE_TREE_LARGE_SIDE_00 = "/tile/tree_large_side_00.png";
    public static final String TILE_TREE_LARGE_SIDE_01 = "/tile/tree_large_side_01.png";
    public static final String TILE_TREE_LARGE_SIDE_02 = "/tile/tree_large_side_02.png";
    public static final String TILE_TREE_LARGE_SIDE_03 = "/tile/tree_large_side_03.png";
    // Tree Branch
    public static final String TILE_TREE_LARGE_SIDE_01_BRANCH = "/tile/tree_large_side_01_branch.png";

    // Level 00
    public static final String LEVEL_00_SIGN = "No wookies allowed...";
    public static final String LEVEL_00_JERMEY_SIGN = "Jeremy's house.";
    public static final String LEVEL_00_JERMEY_SOUND_START = "/sounds/jermey_start.wav";
    public static final String LEVEL_00_JERMEY_SOUND_00 = "/sounds/jermey_00.wav";
    public static final String LEVEL_00_JERMEY_SOUND_01 = "/sounds/jermey_01.wav";
    public static final String LEVEL_00_JERMEY_SOUND_02 = "/sounds/jermey_02.wav";
    public static final String LEVEL_00_JERMEY_SOUND_03 = "/sounds/jermey_03.wav";
    public static final String LEVEL_00_JERMEY_SOUND_04 = "/sounds/jermey_04.wav";
    
    // Water tiles
    public static final String TILE_WATER_00 = "/tile/water00.png";
    public static final String TILE_WATER_01 = "/tile/water01.png";
    public static final String TILE_WATER_02 = "/tile/water02.png";
    public static final String TILE_WATER_03 = "/tile/water03.png";
    public static final String TILE_WATER_04 = "/tile/water04.png";
    public static final String TILE_WATER_05 = "/tile/water05.png";
    public static final String TILE_WATER_06 = "/tile/water06.png";
    public static final String TILE_WATER_07 = "/tile/water07.png";
    public static final String TILE_WATER_08 = "/tile/water08.png";
    public static final String TILE_WATER_09 = "/tile/water09.png";
    public static final String TILE_WATER_10 = "/tile/water10.png";
    public static final String TILE_WATER_11 = "/tile/water11.png";
    public static final String TILE_WATER_12 = "/tile/water12.png";
    public static final String TILE_WATER_13 = "/tile/water13.png";
    
    // Animated water tiles
    public static final String TILE_WATER_A01 = "/tile/water01_a01.png";
    public static final String TILE_WATER_A02 = "/tile/water01_a02.png";
    public static final String TILE_WATER_A03 = "/tile/water01_a03.png";

    // Road tiles
    public static final String TILE_ROAD_00 = "/tile/road00.png";
    public static final String TILE_ROAD_01 = "/tile/road01.png";
    public static final String TILE_ROAD_02 = "/tile/road02.png";
    public static final String TILE_ROAD_03 = "/tile/road03.png";
    public static final String TILE_ROAD_04 = "/tile/road04.png";
    public static final String TILE_ROAD_05 = "/tile/road05.png";
    public static final String TILE_ROAD_06 = "/tile/road06.png";
    public static final String TILE_ROAD_07 = "/tile/road07.png";
    public static final String TILE_ROAD_08 = "/tile/road08.png";
    public static final String TILE_ROAD_09 = "/tile/road09.png";
    public static final String TILE_ROAD_10 = "/tile/road10.png";
    public static final String TILE_ROAD_11 = "/tile/road11.png";
    public static final String TILE_ROAD_12 = "/tile/road12.png";

    // Wood tiles
    public static final String TILE_WOOD_BLOCK_00 = "/tile/wood_block_00.png";
    public static final String TILE_WOOD_BLOCK_01 = "/tile/wood_block_01.png";
    public static final String TILE_WOOD_BLOCK_02 = "/tile/wood_block_02.png";

    public static final String TILE_WOOD_02 = "/tile/wood02.png";
    public static final String TILE_WOOD_03 = "/tile/wood03.png";
    public static final String TILE_WOOD_04 = "/tile/wood04.png";
    public static final String TILE_WOOD_05 = "/tile/wood05.png";
    public static final String TILE_WOOD_06 = "/tile/wood06.png";
    public static final String TILE_WOOD_07 = "/tile/wood07.png";
    public static final String TILE_WOOD_08 = "/tile/wood08.png";
    public static final String TILE_WOOD_09 = "/tile/wood09.png";
    public static final String TILE_WOOD_10 = "/tile/wood10.png";
    public static final String TILE_WOOD_11 = "/tile/wood11.png";
    public static final String TILE_WOOD_12 = "/tile/wood12.png";
    public static final String TILE_WOOD_13 = "/tile/wood13.png";

    // Sounds
    public static final String SOUND_BG_01 = "/sounds/backgroundmusic.wav";
    public static final String SOUND_BG_02 = "/sounds/bgsounds1.wav";
    public static final String SOUND_COIN = "/sounds/coin.wav";
    public static final String SOUND_POWER_UP = "/sounds/powerup.wav";
    public static final String SOUND_UNLOCK = "/sounds/unlock.wav";
    public static final String SOUND_LOCK = "/sounds/lockeddoor.wav";
    public static final String SOUND_FLARE = "/sounds/fanflare.wav";
    public static final String SOUND_TEXT = "/sounds/text.wav";
    public static final String SOUND_HURT = "/sounds/hurt.wav";
    public static final String SOUND_STATIC = "/sounds/static.wav";
    public static final String SOUND_TITLE_SCREEN = "/sounds/starwarstheme.wav";
    public static final String SOUND_CURSOR = "/sounds/cursor.wav";
    public static final String SOUND_LEVELUP = "/sounds/levelup.wav";
    public static final String SOUND_ARROW = "/sounds/arrow.wav";
    public static final String SOUND_LASER = "/sounds/laser.wav";
    public static final String SOUND_PUNCH = "/sounds/punch.wav";
    public static final String SOUND_SWORD = "/sounds/sword.wav";
    public static final String SOUND_TROOPER_HURT = "/sounds/trooperhurt.wav";
    public static final String SOUND_DEFLATE = "/sounds/deflate.wav";
    public static final String SOUND_DEATH = "/sounds/youbemorecareful.wav";
    public static final String SOUND_QUEST_COMPLETE = "/sounds/quest_complete.wav";
    
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
            SOUND_ARROW,
            SOUND_LASER,
            SOUND_DEATH,
            SOUND_SWORD,
            SOUND_PUNCH,
            SOUND_TROOPER_HURT,
            SOUND_DEFLATE
        )
    );

    // Weapon Icons
    public static final String WEAPON_BLASER_ICON = "/objects/weapon_blaster_inverted.png";
    public static final String WEAPON_CROSSBOW_ICON = "/objects/weapon_bow_inverted.png";
    public static final String WEAPON_FIST_ICON = "/objects/weapon_fist_inverted.png";
    public static final String WEAPON_SWORD_ICON = "/objects/weapon_sword_inverted.png";
    public static final HashMap<WeaponType, String> WEAPON_ICONS = new HashMap<>() {{
        put(WeaponType.BLASTER, WEAPON_BLASER_ICON);
        put(WeaponType.CROSSBOW, WEAPON_CROSSBOW_ICON);
        put(WeaponType.FIST, WEAPON_FIST_ICON);
        put(WeaponType.SWORD, WEAPON_SWORD_ICON);
    }};

    // Object Icons
    public static final String OBJECT_POTION_ICON = "/objects/object_potion_inverted.png";
    public static final String OBJECT_KEY_ICON = "/objects/object_key_inverted.png";
    public static final HashMap<ObjectType, String> OBJECT_ICONS = new HashMap<>() {{
        put(ObjectType.POTION, OBJECT_POTION_ICON);
        put(ObjectType.KEY, OBJECT_KEY_ICON);
    }};

    public static final String MESSGE_INVENTORY_ADDED = " added to inventory";
    public static final String MESSAGE_INVENTORY_REMOVED = " removed from inventory";

    public static final String MESSAGE_SIGN_READS = "The sign reads ";

    public static final String RANDOM_HURT_DIALOGUE_01 = "A spider bites you, but it appologizes.";
    public static final String RANDOM_HURT_DIALOGUE_02 = "Damn dirty bees! You pull out the stinger.";
    public static final String RANDOM_HURT_DIALOGUE_03 = "You step in ewok poo. Gross.";
    public static final String RANDOM_HURT_DIALOGUE_04 = "You step on a nail. Ouch.";

    public static final String MESSAGE_POTION_GOOD = "The potion warms your heart.";
    public static final String MESSAGE_POTION_BAD = "The potion is wet and smelly.";

    public static final List<String> RANDOM_HURT_DIALOGUE = new ArrayList<>(
        List.of(
            RANDOM_HURT_DIALOGUE_01,
            RANDOM_HURT_DIALOGUE_02,
            RANDOM_HURT_DIALOGUE_03,
            RANDOM_HURT_DIALOGUE_04
        )
    );

    public static final String FONT_ARIAL = "Arial";
}
