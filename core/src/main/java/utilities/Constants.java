package utilities;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {
    public static final String IDLE_ANIMATION = "assets/Idle.png";
    public static final String WALK_ANIMATION = "assets/Walk.png";
    public static final String RUN_ANIMATION = "assets/Run.png";
    public static final String JUMP_ANIMATION = "assets/Jump.png";
    public static final String ATTACK_1_ANIMATION = "assets/Attack_1.png";

    // Texture Constants
    // Player Assets
    public static  final String PLAYER_IDLE_ANIMATION = "assets/playerTextures/Idle.png";
    public static final String PLAYER_WALK_ANIMATION = "assets/playerTextures/Walk.png";
    public static final String PLAYER_RUN_ANIMATION = "assets/playerTextures/Run.png";
    public static final String PLAYER_JUMP_ANIMATION = "assets/playerTextures/Jump.png";
    public static final String PLAYER_ATTACK_1_ANIMATION = "assets/playerTextures/Attack_1.png";
    public static final String PLAYER_HURT_ANIMATION = "assets/playerTextures/Hurt.png";
    public static  final String PLAYER_DEAD_ANIMATION = "assets/playerTextures/Dead.png";
    // Enemy Assets
    public static  final String ENEMY_IDLE_ANIMATION = "assets/enemyTextures/Idle.png";
    public static final String ENEMY_WALK_ANIMATION = "assets/enemyTextures/Walk.png";
    public static final String ENEMY_RUN_ANIMATION = "assets/enemyTextures/Run.png";
    public static final String ENEMY_JUMP_ANIMATION = "assets/enemyTextures/Jump.png";
    public static final String ENEMY_ATTACK_1_ANIMATION = "assets/enemyTextures/Attack_1.png";
    public static final String ENEMY_HURT_ANIMATION = "assets/enemyTextures/Hurt.png";
    public static  final String ENEMY_DEAD_ANIMATION = "assets/enemyTextures/Dead.png";
    // Map Assets
    public static final String tile1 = "assets/assets/tiles/2.png";
    public static final String tile0 = "assets/assets/tiles/1.png";
    public static final String tile59 = "assets/assets/tiles/59.png";
    public static final String tileFolder = "assets/assets/tiles";
    ///////

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
    ///////
    public static final int originaltilesise = 32;
    public static final int scale = 1;
    public static final int tileSize = originaltilesise * scale;
    public static final int maxScreenCol = 90;//800/48
    public static final int maxScreenrow = 70;
    public static final int NUM_LAYERS = 3;
    public static final float camerazoom = 0.4f;
    public static final List<Integer> animateditems = List.of(22);

    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenrow;
    public static final String Map1 = "assets/maps/map1.txt";
    public static final String Map2 = "assets/maps/map2.txt";
    public static final String backgoundImg = "assets/assets/2 Background/level_bckground.png";

    // Entity movements Constants
    public  static final int IDLE = 0;
    public  static final int WALK = 1;
    public  static final int RUN = 2;
    public  static final int JUMP = 3;
    public  static final int ATTACK_1 = 4;
    public  static final int HURT = 5;
    public  static final int DEAD = 6;


    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    // Gravity Speed
    public  static final int GRAVITY_SPEED = 5;
    // Enemy pursue distance
    public  static final int DISTANCE = 200;


    // Frame Constants
    public static final float FRAME_DELAY = 0.12f;
    public static final int ATTACK_1_FRAMES = 3;

    public static final int FRAME_WIDTH = 128;
    public static final int FRAME_HEIGHT = 128;


//    public static final int mapsize() throws FileNotFoundException {
//        try(BufferedReader br = new BufferedReader(new FileReader(Map1))){
//            String line=br.readLine();
//            String [] l=null;
//            while (line!=null){
//
//                l=line.split(" ");
//            }
//            int len=l.length;
//            return len;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//}
}
