package utilities.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTilesConstants {

    public static final int ORIGINAL_TILE_SIZE = 32;
    public static final int SCALE = 1;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int MAX_SCREEN_COL = 90;
    public static final int MAX_SCREEN_ROW = 70;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public static final int NUM_LAYERS = 3;
    public static final float CAMERA_ZOOM = 0.4f;
    public static final List<Integer> ANIMATED_ITEMS = List.of(22);
    public static final String BACKGROUND_IMAGE = "assets/tilesAssets/2 Background/level_bckground.png";
    public static final String MAP_1 = "assets/maps/map1.txt";
    public static final String MAP_2 = "assets/maps/map2.txt";
    public static final int TOTAL_TILE_IDS = 360;

    public static final Map<String, String> TILES_MAP = new HashMap<>();

    // add tile
    public static void addTile(String key, String value) {
        TILES_MAP.put(key, value);
    }

    // Method to get a tile by key
    public static String getTile(String key) {
        return TILES_MAP.get(key);
    }

}
