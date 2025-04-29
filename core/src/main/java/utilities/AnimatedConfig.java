package utilities;

import java.util.Map;


/**
 * Represents configuration settings for animations.
 * Defines the number of columns, rows, and the speed of the animation.
 */
public class AnimatedConfig {
    public final int cols, rows, speed;
    public static final int DEFAULT_ANIMATION_SPEED = 5;

    // Map storing animation configurations by a unique key
    public static final Map<Integer,AnimatedConfig> animConfigs = Map.of(
        22, new AnimatedConfig(4,1,5),
        33, new AnimatedConfig(6,2,8)
    );

    /**
     * Constructs an AnimatedConfig object with specified columns, rows, and speed.
     *
     * @param c The number of columns in the animation frame grid.
     * @param r The number of rows in the animation frame grid.
     * @param s The speed of the animation (in frames per second).
     */
    public AnimatedConfig(int c,int r,int s){
        cols = c;
        rows = r;
        speed= s;
    }

    //================= Getters =================
    public int getCols() {
        return cols;
    }
    public int getRows() {
        return rows;
    }
    public int getSpeed() {
        return speed;
    }
}
