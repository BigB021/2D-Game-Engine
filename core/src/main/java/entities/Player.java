package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import utilities.Constants;

public class Player extends Entity {
    private int playerAction;
    private boolean isMoving;
    private boolean isDead;
    private boolean isAttacking;
    private boolean isJumping;
    private int playerDirection;
    private double playerSpeed;
    private double cooldown;
    private long lastAttackTime = 0;





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
            double speed = (playerAction == Constants.RUN) ? playerSpeed * 2 : playerSpeed;
            this.setX(this.getX() + speed * getPlayerDirection());
        }
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

    public boolean isAttacking() {
        return isAttacking;
    }
    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public double getCooldown() {
        return cooldown;
    }
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public float getAttackAnimationDuration() {
        int frameCount = Constants.ATTACK_1_FRAMES; // Total frames in attack animation
        float frameDelay = Constants.FRAME_DELAY;   // Delay per frame in seconds
        return frameCount * frameDelay; // Total animation duration in seconds
    }


}
