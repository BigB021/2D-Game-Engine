package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {
    public static final String IDLE_ANIMATION = "assets/Idle.png";
    public static final String WALK_ANIMATION = "assets/Walk.png";
    public static final String RUN_ANIMATION = "assets/Run.png";
    public static final String JUMP_ANIMATION = "assets/Jump.png";
    public static final String ATTACK_1_ANIMATION = "assets/Attack_1.png";
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


    ///////
    public static final int originaltilesise = 32;
    public static final int scale = 1;
    public static final int tileSize = originaltilesise * scale;
    public static final int maxScreenCol = 90;//800/48
    public static final int maxScreenrow = 70;
    public static final int NUM_LAYERS = 3;
    public static final List<Integer> animateditems = List.of();

    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenrow;
    public static final String Map1 = "assets/maps/map1.txt";
    public static final String Map2 = "assets/maps/map2.txt";
    public static final String backgoundImg = "assets/assets/2 Background/level_bckground.png";

    public static final int IDLE = 0;
    public static final int WALK = 1;
    public static final int RUN = 2;
    public static final int JUMP = 3;
    public static final int ATTACK_1 = 4;

    public static final int RIGHT = 1;
    public static final int LEFT = -1;


    public static final float FRAME_DELAY = 0.03f; // 30ms per frame
    public static final int ATTACK_1_FRAMES = 6;

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
