package utilities;

import java.util.Map;

public class AnimatedConfig {
    public final int cols, rows, speed;
    public static final int DEFAULT_ANIMATION_SPEED = 5;

    public static final Map<Integer,AnimatedConfig> animConfigs = Map.of(
        22, new AnimatedConfig(4,1,5),
        33, new AnimatedConfig(6,2,8)
    );

    public AnimatedConfig(int c,int r,int s){cols=c;rows=r;speed=s;}

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
