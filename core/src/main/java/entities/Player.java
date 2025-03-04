package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Entity {
    private int playerAction;
    private boolean isMoving;
    private boolean isDead;
    private boolean playerDirection;
    private float playerSpeed;


    public Player(Texture sprite, int x, int y, int width, int height, int playerAction, boolean isMoving, boolean isDead, boolean playerDirection, float playerSpeed) {
        super(sprite, x, y, width, height);
        this.playerAction = playerAction;
        this.isMoving = isMoving;
        this.isDead = isDead;
        this.playerDirection = playerDirection;
        this.playerSpeed = playerSpeed;
    }

    public void loadAnimation() {

    }
}
