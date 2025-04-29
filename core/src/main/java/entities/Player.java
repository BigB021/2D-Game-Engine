package entities;

import collision.CollisionSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import tileManager.TileManager;
import physics.JumpPhysics;

import static utilities.constants.EntityConstants.*;
import static utilities.constants.FramesConstants.*;
import static utilities.constants.MapTilesConstants.*;
import static utilities.constants.PhysicsConstants.GRAVITY_SPEED;
import static utilities.constants.TextureConstants.*;

/**
 * Represents the player entity in the game, handling movement, actions, and animations.
 */
public class Player extends Entity {

    private int  playerAction;
    private boolean isMoving;
    private boolean isDead;
    private boolean isAttacking;
    private boolean isJumping;
    private double playerSpeed;
    private double cooldown;
    private long lastAttackTime = 0;
    private int animation_index;
    private int checkpointX;
    private int checkpointY;

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

        this.checkpointX = PLAYER_SPAWN_X;
        this.checkpointY = PLAYER_SPAWN_Y;

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
     * Loads the animation frame based on the player's direction and returns a TextureRegion for rendering.
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
     *  Moves the player entity based on the current action,
     *  updates the player's position, handles collisions,
     *  and manages player health.
     */
    public void movePlayer() {

        JumpPhysics.applyJumpPhysics(this);

        if (getEntityHealth() <= 0) {
            setPlayerAction(DEAD);
            this.updateAnimation();
            respawnPlayer();

        }

        if(canMove(DX,DY)) {

            this.updateAnimation();

            if (this.getEntityDirection() == JUMP) {
                this.setY((int) (this.getY() + GRAVITY_SPEED));
            }
            if (isMoving) {
                double speed = (playerAction == RUN) ? playerSpeed * 2 : playerSpeed;
                // Checking collision with screen borders
                if (this.getX() >= (MAX_SCREEN_COL * TILE_SIZE - FRAME_WIDTH * CAMERA_ZOOM)) {
                    this.setX(this.getX() - 1);

                } else if (this.getX() <= 0) {
                    this.setX(this.getX() + 1);
                } else {
                    this.setX(this.getX() + (int) speed * getEntityDirection());
                    updateHitboxes();
                }
            }
            else if(isAttacking()) setPlayerAction(ATTACK_1);
            else if(CollisionSystem.isStandingOnSolid(this) && !isJumping) setPlayerAction(IDLE);
            if (getEntityHealth() <= 0) {
                setDead(true);
                setPlayerAction(DEAD);
                updateAnimation();
                respawnPlayer();
            }

        }
    }

    /**
     * Resets the player’s position to the last checkpoint and restores health.
     * This method is called when the player dies.
     */
    private void respawnPlayer(){
        this.setX(checkpointX);
        this.setY(checkpointY);
        this.updateHitboxes();
        this.setEntityHealth(10);
        isDead = false;
    }

    // todo: implement a method that updates checkPoint
    private void updateCheckPoint() {

    }


    /**
     * Calculates and returns the duration of the attack animation.
     *
     * @return Duration of attack animation in seconds.
     */
    public float getAttackAnimationDuration() {
        return ATTACK_1_FRAMES * FRAME_DELAY;

    }


    //=====================Getters & Setters=====================

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

    public void setCheckPointX(int checkpointX) {
        this.checkpointX = checkpointX;
    }
    public void setCheckPointY(int checkpointY) {
        this.checkpointY = checkpointY;
    }

    public int getCheckPointX() {
        return this.checkpointX;
    }

    public int getCheckPointY() {
        return this.checkpointY;
    }





}
