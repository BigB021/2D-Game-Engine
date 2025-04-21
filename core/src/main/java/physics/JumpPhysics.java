package physics;

import entities.Entity;
import entities.Player;

import static constants.EntityConstants.*;

public class JumpPhysics {

    // Jumping physics
    private static final float JUMP_FORCE = 15f;
    private static final float GRAVITY = 0.9f;
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
            player.updateAnimation();
        }
    }

    // todo: implement jumping method for enemy if needed

    /**
     * Applies gravity to the player if jumping.
     * Updates the Y position based on jump velocity and applies gravity.
     */
    public static void applyJumpPhysics(Entity entity) {
        if (entity instanceof Player){
            if (entity.isJumping()) {
                // Update Y position with current jump velocity
                entity.setY((int) (entity.getY() + ((Player)entity).getJumpVelocity()));
                // Apply gravity: reduce jump velocity
                ((Player)entity).setJumpVelocity(((Player)entity).getJumpVelocity() - GRAVITY);
                // If player falls back to ground level, end jump
                if (entity.getY() <= GROUND_Y) {
                    entity.setY(GROUND_Y);
                    entity.setJumping(false);
                    ((Player)entity).setJumpVelocity(0);
                    ((Player)entity).setPlayerAction(IDLE);
                }
                entity.updateHitboxes();
                ((Player)entity).updateAnimation();
            }
        }

    }
}
