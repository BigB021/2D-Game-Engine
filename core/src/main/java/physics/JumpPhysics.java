package physics;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import mapManager.tileManager.Tile;
import mapManager.tileManager.TileInstance;

import static constants.EntityConstants.*;
import static constants.MapTilesConstants.TILE_SIZE;
import static constants.PhysicsConstants.*;

public class JumpPhysics {


    // todo : find a way to find landing y position
    private static final int GROUND_Y = ENEMY_SPAWN_Y;


    /**
     * Initiates a jump if the player is not already jumping.
     */
    public static void jumpPlayer(Player player) {

        if (!player.isJumping()) {
            player.setJumping(true);
            // Set jump velocity to initiate upward movement
            player.setJumpVelocity(JUMP_FORCE);
            player.setPlayerAction(JUMP);
            player.setMoving(false);
            player.updateAnimation();
        }
    }


    public static void applyJumpPhysics(Entity entity) {
        if (!(entity instanceof Player)) return;

        if (!entity.isJumping()) return;


        // Apply gravity
        entity.setJumpVelocity(entity.getJumpVelocity() - GRAVITY);

        // Limit max fall speed
        if (entity.getJumpVelocity() < -MAX_FALL_SPEED) {
            entity.setJumpVelocity(-MAX_FALL_SPEED);
        }

        // Move Y position based on velocity
        float newY = entity.getY() + entity.getJumpVelocity();
        entity.setY(newY);
        entity.updateHitboxes();

    }

}
