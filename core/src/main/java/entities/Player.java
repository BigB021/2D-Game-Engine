package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import physics.JumpPhysics;
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

    // Jumping physics
    private float jumpVelocity;
    private static final float JUMP_FORCE = 15f;
    private static final float GRAVITY = 0.5f;
    private static final int GROUND_Y = 210;



    // Cached textures for animations
    private Texture idleTexture;
    private Texture walkTexture;
    private Texture runTexture;
    private Texture jumpTexture;
    private Texture attack1Texture;
    private Texture hurtTexture;
    private Texture deadTexture;


    /**
     * Constructs a Player object with specified position, size, and speed.
     *
     * @param x           Initial x-coordinate of the player.
     * @param y           Initial y-coordinate of the player.
     * @param width       Width of the player.
     * @param height      Height of the player.
     * @param playerSpeed Movement speed of the player.
     */
    public Player(int x, int y, int width, int height, double playerSpeed, int playerHealth) {
        super(x, y, width, height, playerHealth);
        this.playerSpeed = playerSpeed;
        this.isMoving = false;
        this.isDead = false;
        this.playerAction = Constants.IDLE;
        this.playerDirection = Constants.RIGHT;
        this.animation_index = 0;
        this.isJumping = false;
        this.jumpVelocity = 0;

        loadTextures();
        // Initialize sprite with the idle texture
        setSprite(idleTexture);
    }

    /**
     * Loads animation textures once and caches them.
     */
    private void loadTextures() {
        idleTexture = new Texture(Constants.PLAYER_IDLE_ANIMATION);
        walkTexture = new Texture(Constants.PLAYER_WALK_ANIMATION);
        runTexture = new Texture(Constants.PLAYER_RUN_ANIMATION);
        jumpTexture = new Texture(Constants.PLAYER_JUMP_ANIMATION);
        attack1Texture = new Texture(Constants.PLAYER_ATTACK_1_ANIMATION);
        hurtTexture = new Texture(Constants.PLAYER_HURT_ANIMATION);
        deadTexture = new Texture(Constants.PLAYER_DEAD_ANIMATION);
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
            case Constants.HURT:
                if (getSprite() != hurtTexture) {
                    setSprite(hurtTexture);
                }
                break;
            case Constants.DEAD:
                if (getSprite() != deadTexture) {
                    setSprite(deadTexture);
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
        // Apply jump physics if the player is in jump state
        JumpPhysics.applyJumpPhysics(this);

        if(this.playerDirection == Constants.JUMP){
            this.setY((int) (this.getY() + Constants.GRAVITY_SPEED));
        }
        if (isMoving) {
            double speed = (playerAction == Constants.RUN) ? playerSpeed * 2 : playerSpeed;
            // Checking collision with screen borders
            if(this.getX() >= (Constants.maxScreenCol * Constants.tileSize - Constants.FRAME_WIDTH*Constants.camerazoom  )){
                this.setX(this.getX() - 1);

            }else if(this.getX()<= 0){
                this.setX(this.getX() + 1);
            }
            else {
                this.setX(this.getX() + (int) speed * getPlayerDirection());
                updateHitbox();
            }
        }
        else if(isDead){
            setPlayerAction(Constants.DEAD);

            // todo: improve respawning
            this.setX(1000); // respawn player
            this.updateAnimation();
            this.updateHitbox();
            this.setEntityHealth(10);
            isDead = false;
        }
        else if (!this.isAttacking && !this.isJumping) {
            setPlayerAction(Constants.IDLE);

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

    public int getPlayerAction() {
        return playerAction;
    }

    public void setAnimation_index(int animation_index) {
        this.animation_index = animation_index;
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }
    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

}
