package utilities;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    // Texture Constants
    // Player Assest
    public static  final String IDLE_ANIMATION = "assets/Idle.png";
    public static final String WALK_ANIMATION = "assets/Walk.png";
    public static final String RUN_ANIMATION = "assets/Run.png";
    public static final String JUMP_ANIMATION = "assets/Jump.png";
    public static final String ATTACK_1_ANIMATION = "assets/Attack_1.png";
    public static final String HURT_ANIMATION = "assets/Hurt.png";
    public static  final String DEAD_ANIMATION = "assets/Dead.png";
    // Map Assets
    public static final String tile1 = "assets/assets/tiles/2.png";
    public static final String tile0 = "assets/assets/tiles/1.png";
    public static final String tile59 = "assets/assets/tiles/59.png";
    public static final String tileFolder = "assets/assets/tiles";

    public static final Map<String, String> tilesMap = new HashMap<>();
    // add tile
    public static void addTile(String key, String value) {
        tilesMap.put(key, value);
    }

    // Method to get a tile by key
    public static String getTile(String key) {
        return tilesMap.get(key);
    }


    // Map Tiles Constants
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 1;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int MAX_SCREEN_COL = 81;//800/48
    public static final int MAX_SCREEN_ROWS = 48;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROWS;
    public static final String MAP_1 = "assets/maps/map1.txt";

    // Entity movements Constants
    public  static final int IDLE = 0;
    public  static final int WALK = 1;
    public  static final int RUN = 2;
    public  static final int JUMP = 3;
    public  static final int ATTACK_1 = 4;
    public  static final int HURT = 5;
    public  static final int DEAD = 6;

    public  static final int RIGHT = 1;
    public  static final int LEFT = -1;

    // Enemy pursue distance
    public  static final int DISTANCE = 200;


    // Frame Constants
    public static final float FRAME_DELAY = 0.12f;
    public static final int ATTACK_1_FRAMES = 3;

    public static final int FRAME_WIDTH = 128;
    public static final int FRAME_HEIGHT = 128;




}
