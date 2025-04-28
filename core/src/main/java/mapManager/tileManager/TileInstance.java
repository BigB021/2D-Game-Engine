package mapManager.tileManager;

import com.badlogic.gdx.math.Rectangle;

public class TileInstance {
    public Tile prototype;
    public Rectangle collisionBox;

    public TileInstance(Tile prototype, Rectangle collisionBox) {
        this.prototype = prototype;
        this.collisionBox = collisionBox;
    }
}
