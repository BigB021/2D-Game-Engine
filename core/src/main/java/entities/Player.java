package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import utilities.Constants;

public class Player extends Entity {
    private int playerAction;
    private boolean isMoving;
    private boolean isDead;
    private int playerDirection;
    private final float playerSpeed;


    public Player(int x, int y, int width, int height, float playerSpeed) {
        super(x, y, width, height);
        this.playerSpeed = playerSpeed;
    }

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }

    public void setAnimation() {
        switch (playerAction) {
            case Constants.IDLE:
                this.setSprite(new Texture(Constants.IDLE_ANIMATION));
                break;
            case Constants.WALK:
                this.setSprite(new Texture(Constants.WALK_ANIMATION));
                break;
            case Constants.RUN:
                this.setSprite(new Texture(Constants.RUN_ANIMATION));
                break;
            case Constants.JUMP:
                this.setSprite(new Texture(Constants.JUMP_ANIMATION));
                break;
            case Constants.ATTACK_1:
                this.setSprite(new Texture(Constants.ATTACK_1_ANIMATION));
        }

    }


    public  TextureRegion loadAnimation(int x,int y,int width,int height) {
        return new TextureRegion(getSprite() ,x,y,width,height);
    }

    public void movePlayer() {
        this.setAnimation();
        if (isMoving) {
            this.setX(this.getX() + (int) this.getPlayerSpeed() * getPlayerDirection());
        }
    }



    public void setPlayerDirection(int playerDirection) {
        this.playerDirection = playerDirection;
    }

    public int getPlayerDirection() {
        return playerDirection;
    }
    public float getPlayerSpeed() {
        return playerSpeed;
    }
    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

}
