package physics;

import entities.Entity;

import static utilities.constants.PhysicsConstants.GRAVITY;
import static utilities.constants.PhysicsConstants.MAX_FALL_SPEED;

public class GravityPhysics {

    /**
     * Applies gravity to the given entity in a game,
     * adjusting its vertical velocity and position based on whether it is grounded or not
     * @param entity : The entity on which gravity is being applied.
     */
    public static void applyGravity(Entity entity) {
        // Don't apply gravity if the player is grounded

        if (entity.isGrounded()) {
            entity.setVelocityY(0);
            return;
        }

        // Apply gravity by adjusting the Y position
        float currentY = entity.getY();
        float newY = currentY - GRAVITY * entity.getVelocityY(); // Apply downward force

        entity.setY(newY); // Update Y position
        if(entity.getVelocityY() <= MAX_FALL_SPEED) entity.setVelocityY(entity.getVelocityY() + GRAVITY); // Increment Y velocity

        // Update hitboxes (ensure collision checks are accurate)
        entity.updateHitboxes();
    }
}
