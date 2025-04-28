package physics;

import entities.Entity;

import static constants.PhysicsConstants.*;

public class GravityPhysics {

    public static void applyGravity(Entity entity) {
        if (entity.isGrounded()) {
            entity.setVelocityY(0);
            return; // Don't apply gravity if the player is grounded
        }

        // Apply gravity by adjusting the Y position
        float currentY = entity.getY();
        float newY = currentY - GRAVITY * entity.getVelocityY(); // Apply downward force

        entity.setY(newY); // Update Y position
        entity.setVelocityY(entity.getVelocityY() + GRAVITY);

        // Update hitboxes (ensure collision checks are accurate)
        entity.updateHitboxes();
    }
}
