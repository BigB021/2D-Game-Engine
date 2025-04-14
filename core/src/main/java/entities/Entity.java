package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import utilities.Constants;

/**
 * Abstract class that represents base entity in the game.
 * Provides common properties and methods for all game entities.
 */
public abstract class Entity {
    protected Texture sprite;
    protected float x, y;
    protected Rectangle hitBox;

    private int entityHealth;

    // Entity state
    protected boolean isMoving;
    protected boolean isDead;
    protected boolean isAttacking;
    protected boolean isJumping;




    /**
     * Constructs an entity with a specified position and hitbox dimensions.
     *
     * @param x      The initial x-coordinate of the entity.
     * @param y      The initial y-coordinate of the entity.
     * @param width  The width of the entity's sprite.
     * @param height The height of the entity's sprite.
     */
    public Entity(float x, float y, int width, int height, int health) {
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x + width * 0.4f, y, width * 0.3f, height * 0.5f);
        this.entityHealth = health;
    }

    /**
     * Updates the player's hitbox position to match the player's movement.
     */
    public void updateHitbox(){
        this.getHitBox().x = this.getX() + getHitBox().width ;
        this.getHitBox().y = this.getY();
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

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

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

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }



}
