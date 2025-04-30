package utilities.constants;

public class EntityConstants {

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

    public static final int PLAYER_ATTACK_DAMAGE = 2;
    public static final int ENEMY_ATTACK_DAMAGE = 1;

    // Enemy pursue distance
    public  static final int DISTANCE = 100;

    // Spawn Constants
    public static final int PLAYER_SPAWN_X = 1;
    public static final int PLAYER_SPAWN_Y = 256;
    public static final int[] ENEMY_SPAWN_X = {735,1200,1700};
    public static final int[] ENEMY_SPAWN_Y = {192,160,160};

    public static final float DX = 0.000000001f;
    public static final float DY = 0.000000001f;

}
