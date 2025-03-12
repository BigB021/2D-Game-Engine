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
    private double playerSpeed;


    public Player(int x, int y, int width, int height, double playerSpeed) {
        super(x, y, width, height);
        this.playerSpeed = playerSpeed;
        this.isMoving = false;
        this.isDead = false;
        this.playerAction = Constants.IDLE;
        this.playerDirection = Constants.RIGHT;
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
        if (getPlayerDirection() == Constants.RIGHT) {

            return new TextureRegion(getSprite() ,x,y,getPlayerDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y,getPlayerDirection() * width,height);
    }

    public void movePlayer() {
        this.setAnimation();
        if (isMoving) {
            this.setX(this.getX() + this.getPlayerSpeed() * getPlayerDirection());
        }
        // temp solution
        // todo: implement method to switch between walk and dash speed
        setPlayerSpeed(3.);
    }



    public void setPlayerDirection(int playerDirection) {
        this.playerDirection = playerDirection;
    }

    public int getPlayerDirection() {
        return playerDirection;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }
    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }
    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

}
