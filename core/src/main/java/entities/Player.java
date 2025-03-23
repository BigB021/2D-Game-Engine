package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.Constants;

/**
 * Represents the player entity in the game, handling movement, actions, and animations.
 */
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
    private int animation_index;


    // Cached textures for animations
    private Texture idleTexture;
    private Texture walkTexture;
    private Texture runTexture;
    private Texture jumpTexture;
    private Texture attack1Texture;


    /**
     * Constructs a Player object with specified position, size, and speed.
     *
     * @param x           Initial x-coordinate of the player.
     * @param y           Initial y-coordinate of the player.
     * @param width       Width of the player.
     * @param height      Height of the player.
     * @param playerSpeed Movement speed of the player.
     */
    public Player(int x, int y, int width, int height, double playerSpeed) {
        super(x, y, width, height);
        this.playerSpeed = playerSpeed;
        this.isMoving = false;
        this.isDead = false;
        this.playerAction = Constants.IDLE;
        this.playerDirection = Constants.RIGHT;
        this.animation_index = 0;

        loadTextures();
        // Initialize sprite with the idle texture
        setSprite(idleTexture);
    }

    /**
     * Loads animation textures once and caches them.
     */
    private void loadTextures() {
        idleTexture = new Texture(Constants.IDLE_ANIMATION);
        walkTexture = new Texture(Constants.WALK_ANIMATION);
        runTexture = new Texture(Constants.RUN_ANIMATION);
        jumpTexture = new Texture(Constants.JUMP_ANIMATION);
        attack1Texture = new Texture(Constants.ATTACK_1_ANIMATION);
    }

    /**
     * Updates the player's animation based on the current action.
     */
    public void updateAnimation() {
        switch (playerAction) {
            case Constants.IDLE:
                if (getSprite() != idleTexture) {
                    setSprite(idleTexture);
                }
                break;
            case Constants.WALK:
                if (getSprite() != walkTexture) {
                    setSprite(walkTexture);
                }
                break;
            case Constants.RUN:
                if (getSprite() != runTexture) {
                    setSprite(runTexture);
                }
                break;
            case Constants.JUMP:
                if (getSprite() != jumpTexture) {
                    setSprite(jumpTexture);
                }
                break;
            case Constants.ATTACK_1:
                if (getSprite() != attack1Texture) {
                    setSprite(attack1Texture);
                }
                break;
        }
    }

    /**
     * Loads and returns the appropriate animation frame based on direction.
     *
     * @param x      X-coordinate of the animation frame.
     * @param y      Y-coordinate of the animation frame.
     * @param width  Width of the animation frame.
     * @param height Height of the animation frame.
     * @return TextureRegion containing the selected frame.
     */
    public  TextureRegion loadAnimation(int x,int y,int width,int height) {
        if (getPlayerDirection() == Constants.RIGHT) {

            return new TextureRegion(getSprite() ,x,y,getPlayerDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y,getPlayerDirection() * width,height);
    }

    /**
     * Moves the player based on their current state and updates their hitBox.
     */
    public void movePlayer() {
        this.updateAnimation();
        if (isMoving) {
            double speed = (playerAction == Constants.RUN) ? playerSpeed * 2 : playerSpeed;
            // Checking collision with screen borders
            if(this.getX() >= (Constants.SCREEN_WIDTH - Constants.FRAME_WIDTH * 0.6f)){
                this.setX(this.getX() - 1);

            }else if(this.getX()<= 0){
                this.setX(this.getX() + 1);
            }
            else {
                this.setX(this.getX() + (int) speed * getPlayerDirection());
                updateHitbox();
            }
        }
    }


    /**
     * Calculates and returns the duration of the attack animation.
     *
     * @return Duration of attack animation in seconds.
     */
    public float getAttackAnimationDuration() {
        return Constants.ATTACK_1_FRAMES * Constants.FRAME_DELAY; // Returns seconds

    }

    // Getters and Setters
    public void setPlayerDirection(int playerDirection) {
        this.playerDirection = playerDirection;
    }

    public int getPlayerDirection() {
        return playerDirection;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public double getCooldown() {
        return cooldown;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public int getAnimation_index() {
        return animation_index;
    }

    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public void setLastAttackTime(long lastAttackTime) { this.lastAttackTime = lastAttackTime; }

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }

    public void setAnimation_index(int animation_index) {
        this.animation_index = animation_index;
    }

}
