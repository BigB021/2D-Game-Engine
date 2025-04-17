package collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import utilities.Constants;

public class CollisionSystem {

    // Check collision with player
    public static boolean checkPlayerCollision(Player player, Enemy enemy) {

        Rectangle intersection = new Rectangle();

        // Determines whether the given rectangles intersect and, if they do, sets the supplied intersection rectangle to the area of overlap (libGdx documentation)
        if (Intersector.intersectRectangles(enemy.getHitBox(),player.getHitBox(),intersection)) {
            if(enemy.getEnemyDirection() == Constants.RIGHT) {
                enemy.setX(enemy.getX() - intersection.width);
            }
            else if (enemy.getEnemyDirection() == Constants.LEFT) {
                enemy.setX(enemy.getX() + intersection.width);
            }
            enemy.updateHitboxes();
            return true;
        }
        return false;
    }

    // Check Collision with screen borders
    public static boolean checkScreenCollision(Entity entity) {
        if (entity.getHitBox().x >= Constants.SCREEN_WIDTH) {
            if(entity instanceof Enemy){
                ((Enemy)entity).setEnemyDirection(Constants.LEFT);
            }
            return true;
        }else if (entity.getHitBox().x <= 0) {
            if(entity instanceof Enemy){
                ((Enemy)entity).setEnemyDirection(Constants.RIGHT);
            }
            return true;
        }
        return false;
    }

}
