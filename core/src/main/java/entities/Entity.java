package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class that represents base entity in the game.
 * Provides common properties and methods for all game entities.
 */
public abstract class Entity {
    protected Texture sprite;
    protected float x, y;
    protected Rectangle hitBox;

    /**
     * Constructs an entity with a specified position and hitbox dimensions.
     *
     * @param x      The initial x-coordinate of the entity.
     * @param y      The initial y-coordinate of the entity.
     * @param width  The width of the entity's sprite.
     * @param height The height of the entity's sprite.
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.hitBox = new Rectangle(x + width * 0.4f, y, width * 0.3f, height * 0.5f);
    }

    /**
     * Updates the player's hitbox position to match the player's movement.
     */
    public void updateHitbox(){
        this.getHitBox().x = this.getX() + 40 ;
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




}
