package collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import mapManager.tileManager.Tile;
import mapManager.tileManager.TileInstance;
import utilities.TileFace;

import static constants.EntityConstants.LEFT;
import static constants.EntityConstants.RIGHT;
import static constants.MapTilesConstants.SCREEN_WIDTH;
import static constants.MapTilesConstants.TILE_SIZE;

public class CollisionSystem {

    // Check collision with player
    public static boolean checkPlayerCollision(Player player, Enemy enemy) {

        Rectangle intersection = new Rectangle();

        // Determines whether the given rectangles intersect and, if they do, sets the supplied intersection rectangle to the area of overlap (libGdx documentation)
        if (Intersector.intersectRectangles(enemy.getHitBox(),player.getHitBox(),intersection)) {
            if(enemy.getEntityDirection() == RIGHT) {
                enemy.setX(enemy.getX() - intersection.width);
            }
            else if (enemy.getEntityDirection() == LEFT) {
                enemy.setX(enemy.getX() + intersection.width);
            }
            enemy.updateHitboxes();
            return true;
        }
        return false;
    }

    // Check Collision with screen borders
    public static boolean checkScreenCollision(Entity entity) {
        if (entity.getHitBox().x >= SCREEN_WIDTH) {
            if(entity instanceof Enemy){
                ((Enemy)entity).setEntityDirection(LEFT);
            }
            return true;
        }else if (entity.getHitBox().x <= 0) {
            if(entity instanceof Enemy){
                ((Enemy)entity).setEntityDirection(RIGHT);
            }
            return true;
        }
        return false;
    }

    public static TileFace getCollisionFace(Entity entity, TileInstance tile) {
        Rectangle intersection = new Rectangle();
        if (!Intersector.intersectRectangles(entity.getHitBox(), tile.collisionBox, intersection)) {
            return TileFace.NONE;
        }

        float width = intersection.getWidth();
        float height = intersection.getHeight();

        // Which side has the smallest overlap → assume collision from that side
        if (width < height) {
            // horizontal collision
            if (entity.getHitBox().x < tile.collisionBox.x) {
                return TileFace.LEFT;
            } else {
                return TileFace.RIGHT;
            }
        } else {

            // vertical collision
            if (entity.getHitBox().y > tile.collisionBox.y) {
                return TileFace.TOP; // landed on tile
            } else {
                return TileFace.BOTTOM; // hit head on tile
            }
        }
    }

    public static void resolveTileCollision(Entity entity, TileInstance tile, TileFace face) {
        Rectangle overlap = new Rectangle();

        if (!Intersector.intersectRectangles(entity.getHitBox(), tile.collisionBox, overlap)) {
            return;
        }

        switch (face) {
            case TOP:  // landing on tile
                // 1) snap to top of the tile
                entity.setY(tile.collisionBox.y + tile.collisionBox.height);
                // 2) stop vertical motion
                entity.setVelocityY(0);
                // 3) mark as grounded so gravity stops next frame
                //entity.setGrounded(true);
                entity.setJumping(false);
                break;
            case BOTTOM: // hit head
                entity.setY(tile.collisionBox.y - tile.collisionBox.height);
                break;
            case LEFT:
                entity.setX(tile.collisionBox.x - tile.collisionBox.width);
                break;
            case RIGHT:
                entity.setX(tile.collisionBox.x + tile.collisionBox.width - entity.getHitBox().width);
                break;
            default:
                break;
        }

        entity.updateHitboxes();
    }


    public static boolean isStandingOnSolid(Entity entity) {
        entity.setGrounded(false);

        Rectangle hit = entity.getHitBox();
        int ts       = TILE_SIZE;
        float feetY  = hit.y;

        int colStart = (int)(hit.x / ts);
        int colEnd   = (int)((hit.x + hit.width) / ts);
        int rowBelow = (int)((feetY - 1) / ts);

        for (int c = colStart; c <= colEnd; c++) {
            TileInstance t = entity.tileManager.getTileInstance(c, rowBelow, 0);
            if(t.prototype != null) {
            if (t != null && t.prototype.collision) {
                // Optionally a tiny overlap test here:
                if (t.collisionBox.y + t.collisionBox.height >= feetY) {
                    entity.setGrounded(true);
                    //System.out.println("Grounded true at tile row " + rowBelow);
                    return true;
                }
            }
            }
        }

        //System.out.println("Grounded false");
        return false;
    }


}
