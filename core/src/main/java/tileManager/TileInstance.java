package tileManager;

import com.badlogic.gdx.math.Rectangle;

/**
 * Represents an instance of a tile in the game world.
 * Each instance holds a reference to its prototype tile and its collision box.
 */
public class TileInstance {
    public Tile prototype;
    public Rectangle collisionBox;

    /**
     * Constructs a TileInstance with a specified prototype tile and a collision box.
     *
     * @param prototype The prototype tile associated with this instance.
     * @param collisionBox The collision box that defines the area of the tile for collision detection.
     */
    public TileInstance(Tile prototype, Rectangle collisionBox) {
        this.prototype = prototype;
        this.collisionBox = collisionBox;
    }
}
