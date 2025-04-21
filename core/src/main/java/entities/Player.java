package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mapManager.tileManager.TileManager;
import physics.JumpPhysics;

import static constants.EntityConstants.*;
import static constants.FramesConstants.*;
import static constants.MapTilesConstants.*;
import static constants.TextureConstants.*;

/**
 * Represents the player entity in the game, handling movement, actions, and animations.
 */
public class Player extends Entity {

    private int playerAction;
    private boolean isMoving;
    private boolean isDead;
    private boolean isAttacking;
    private boolean isJumping;
    //private int playerDirection;
    private double playerSpeed;
    private double cooldown;
    private long lastAttackTime = 0;
    private int animation_index;

    // Jumping physics

    private float jumpVelocity;
    private static final float JUMP_FORCE = 15f;
    private static final float GRAVITY = 0.5f;
    // todo: get the y coordinates of the ground
    private static final int GROUND_Y = 192;



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
    public Player(int x, int y, int width, int height, double playerSpeed, int playerHealth, TileManager tileManager) {
        super(x, y, width, height, playerHealth, tileManager);
        this.playerSpeed = playerSpeed;
        this.isMoving = false;
        this.isDead = false;
        this.playerAction = IDLE;
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
        idleTexture = new Texture(PLAYER_IDLE_ANIMATION);
        walkTexture = new Texture(PLAYER_WALK_ANIMATION);
        runTexture = new Texture(PLAYER_RUN_ANIMATION);
        jumpTexture = new Texture(PLAYER_JUMP_ANIMATION);
        attack1Texture = new Texture(PLAYER_ATTACK_1_ANIMATION);
        hurtTexture = new Texture(PLAYER_HURT_ANIMATION);
        deadTexture = new Texture(PLAYER_DEAD_ANIMATION);
    }

    /**
     * Updates the player's animation based on the current action.
     */
    public void updateAnimation() {
        switch (playerAction) {
            case IDLE:
                if (getSprite() != idleTexture) {
                    setSprite(idleTexture);
                }
                break;
            case WALK:
                if (getSprite() != walkTexture) {
                    setSprite(walkTexture);
                }
                break;
            case RUN:
                if (getSprite() != runTexture) {
                    setSprite(runTexture);
                }
                break;
            case JUMP:
                if (getSprite() != jumpTexture) {
                    setSprite(jumpTexture);
                }
                break;
            case ATTACK_1:
                if (getSprite() != attack1Texture) {
                    setSprite(attack1Texture);
                }
                break;
            case HURT:
                if (getSprite() != hurtTexture) {
                    setSprite(hurtTexture);
                }
                break;
            case DEAD:
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
        if (getEntityDirection() == RIGHT) {

            return new TextureRegion(getSprite() ,x,y,getEntityDirection() * width,height);
        }
        return new TextureRegion(getSprite() ,x + width,y,getEntityDirection() * width,height);
    }

    /**
     * Moves the player based on their current state and updates their hitBox.
     */
    public void movePlayer() {

        canMove();
        this.updateAnimation();
        // Apply jump physics if the player is in jump state
        JumpPhysics.applyJumpPhysics(this);

        if(this.getEntityDirection() == JUMP){
            this.setY((int) (this.getY() + GRAVITY_SPEED));
        }
        if (isMoving) {
            double speed = (playerAction == RUN) ? playerSpeed * 2 : playerSpeed;
            // Checking collision with screen borders
            if(this.getX() >= (MAX_SCREEN_COL * TILE_SIZE - FRAME_WIDTH*CAMERA_ZOOM)){
                this.setX(this.getX() - 1);

            }else if(this.getX()<= 0){
                this.setX(this.getX() + 1);
            }
            else {
                this.setX(this.getX() + (int) speed * getEntityDirection());
                updateHitboxes();
            }
        }
        else if(isDead){
            setPlayerAction(DEAD);

            // todo: improve respawning
            this.setX(PLAYER_SPAWN_X); // respawn player
            this.setY(PLAYER_SPAWN_Y);
            this.updateAnimation();
            this.updateHitboxes();
            this.setEntityHealth(10);
            isDead = false;
        }
        else if (!this.isAttacking && !this.isJumping) {
            setPlayerAction(IDLE);

        }
    }

    /**
     * Calculates and returns the duration of the attack animation.
     *
     * @return Duration of attack animation in seconds.
     */
    public float getAttackAnimationDuration() {
        return ATTACK_1_FRAMES * FRAME_DELAY; // Returns seconds

    }



    // Getters and Setters

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
