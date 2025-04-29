package physics;

import entities.Entity;
import entities.Player;

import static utilities.constants.EntityConstants.ENEMY_SPAWN_Y;
import static utilities.constants.EntityConstants.JUMP;
import static utilities.constants.PhysicsConstants.*;


/**
 * Class responsible for handling jump-related physics for entities, specifically the player.
 * Contains methods for initiating a jump and applying jump physics (velocity and gravity).
 */
public class JumpPhysics {


    /**
     * Initiates a jump for the player if the player is not already jumping.
     * This method sets the jump velocity to initiate upward movement and changes the player's state.
     *
     * @param player The player entity that is attempting to jump.
     */
    public static void jumpPlayer(Player player) {

        if (player.isEntityJumping()) {
            player.setJumping(true);
            // Set jump velocity to initiate upward movement
            player.setJumpVelocity(JUMP_FORCE);
            player.setPlayerAction(JUMP);
            player.setMoving(false);
            player.updateAnimation();
        }
    }

    /**
     * Applies the jump physics to an entity.
     * This method is only intended for players (could be altered to handle Enemy jump alongside Player).
     * It adjusts the entity's Y-position based on its vertical velocity, limits the fall speed,
     * and updates the hitboxes.
     *
     * @param entity The entity (Player) to apply the jump physics to.
     */
    public static void applyJumpPhysics(Entity entity) {
        if (!(entity instanceof Player)) return;

        if (entity.isEntityJumping()) return;

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
