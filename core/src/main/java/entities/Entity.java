package entities;

import collision.CollisionSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import mapManager.tileManager.Tile;
import mapManager.tileManager.TileInstance;
import mapManager.tileManager.TileManager;
import physics.JumpPhysics;
import utilities.TileFace;

import static constants.EntityConstants.LEFT;
import static constants.EntityConstants.RIGHT;
import static constants.FramesConstants.FRAME_HEIGHT;
import static constants.FramesConstants.FRAME_WIDTH;

/**
 * Abstract class that represents base entity in the game.
 * Provides common properties and methods for all game entities.
 */
public abstract class Entity {
    protected float x, y;
    protected Texture sprite;
    protected Rectangle hitBox;
    protected Rectangle attackHitBox;

    protected int entityDirection;
    private int entityHealth;

    protected long deathStartTime = 0;

    protected float velocityY = 0f;


    // Entity state
    protected boolean isMoving;
    protected boolean isDead;
    protected boolean isAttacking;
    protected boolean isJumping;
    protected boolean isGrounded;

    // Jumping physics
    private float jumpVelocity;
    private static final float JUMP_FORCE = 15f;
    private static final float GRAVITY = 0.5f;
    // todo: get the y coordinates of the ground
    private static final int GROUND_Y = 192;

    public TileManager tileManager;

    /**
     * Constructs an entity with a specified position and hitbox dimensions.
     *
     * @param x      The initial x-coordinate of the entity.
     * @param y      The initial y-coordinate of the entity.
     * @param width  The width of the entity's sprite.
     * @param height The height of the entity's sprite.
     */
    public Entity(float x, float y, int width, int height, int health,TileManager tileManager) {
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x + width * 0.4f, y, width * 0.3f, height * 0.5f);
        this.attackHitBox = new Rectangle(x + width * 0.4f, y+ (height * 0.2f), width * .5f, height * 0.2f);
        this.entityHealth = health;
        this.entityDirection = RIGHT;
        this.tileManager = tileManager;

        this.jumpVelocity = 0;


    }

    /**
     * Updates the player's hitbox position to match the player's movement.
     */
    public void updateHitboxes(){
        this.getHitBox().x = this.getX() + getHitBox().width ;
        this.getHitBox().y = this.getY();

        // todo: handle positioning of attack hitbox according to entity direction (+ it can have different offsets/sizes)

        if(entityDirection == RIGHT){
            this.attackHitBox.x = this.getX() + (FRAME_WIDTH * 0.2f);
        }
        else if(entityDirection == LEFT){
            this.attackHitBox.x = this.getX() ;
        }
        this.attackHitBox.y = this.getY() + (FRAME_HEIGHT * 0.1f);

    }




    public boolean canMove(float dx, float dy) {
        boolean moved = false;
//        JumpPhysics.applyGravity(this);
        if(CollisionSystem.isStandingOnSolid(this)){
            setGrounded(true);
            System.out.println("CollisionSystem.isStandingOnSolid()");
        }
        else setGrounded(false);

        // Attempt horizontal move
        if (dx != 0) {
            setX(getX() + dx);
            updateHitboxes();

            for (TileInstance tileInstance : tileManager.getOverlappingTiles(0, this)) {
                if (tileInstance.prototype == null || !tileInstance.prototype.collision) continue;

                TileFace face = CollisionSystem.getCollisionFace(this, tileInstance);
                System.out.println("Collision with tile " + tileInstance.prototype.image.toString() +
                    " at " + tileInstance.collisionBox.x + "," + tileInstance.collisionBox.y +
                    " | Face: " + face);

                if (face == TileFace.LEFT || face == TileFace.RIGHT) {
                    CollisionSystem.resolveTileCollision(this, tileInstance, face);
                    dx = 0; // Cancel horizontal movement
                    break;
                }
            }
            moved |= dx != 0;
        }

        // Attempt vertical move
        if (dy != 0) {
            //if (dy < 0) setGrounded(false);
            setY(getY() + dy);
            updateHitboxes();

            for (TileInstance tileInstance : tileManager.getOverlappingTiles(0, this)) {
                if (tileInstance.prototype == null || !tileInstance.prototype.collision) continue;

                TileFace face = CollisionSystem.getCollisionFace(this, tileInstance);

                if (face == TileFace.TOP || face == TileFace.BOTTOM) {
                    CollisionSystem.resolveTileCollision(this, tileInstance, face);
                    dy = 0; // Cancel vertical movement
                    break;
                }
            }
            moved |= dy != 0;
        }

        return moved;
    }





    // Getters & Setters
    public Texture getSprite() {
        return sprite;
    }

    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getAttackHitBox() { return attackHitBox; }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public int getEntityHealth() { return entityHealth; }

    public void setEntityHealth(int health) { this.entityHealth = health; }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isDead(){
        return isDead;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isGrounded() {return isGrounded;}

    public void setJumping(boolean jumping) {isJumping = jumping;}

    public void setMoving(boolean moving) {isMoving = moving;}

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setGrounded(boolean grounded) {isGrounded = grounded;}

    public void setAttackHitBox(Rectangle hitBox) { this.attackHitBox = hitBox; }

    public void setEntityDirection(int entityDirection) {
        this.entityDirection = entityDirection;
    }

    public int getEntityDirection() {
        return entityDirection;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }
    public float getVelocityY() {
        return velocityY;
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }
    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }
}
