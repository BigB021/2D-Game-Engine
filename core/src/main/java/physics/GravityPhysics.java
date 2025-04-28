package physics;

import entities.Entity;

import static constants.PhysicsConstants.*;

public class GravityPhysics {
//    public static void applyGravity(Entity entity, float deltaTime) {
//        if (!entity.isGrounded()) {
//            // Accelerate downward due to gravity (assuming negative Y is downward in your system)
//            entity.setVelocityY(entity.getVelocityY() - GRAVITY * deltaTime);
//
//            // Cap at terminal velocity
//            if (entity.getVelocityY() < -TERMINAL_VELOCITY) {
//                entity.setVelocityY(-EARTH_GRAVITY*TERMINAL_VELOCITY);
//            }
//
//            // Apply the velocity to move the entity
//            entity.canMove(0, entity.getVelocityY() * deltaTime);
//        } else {
//            entity.setVelocityY(0);
//        }
//    }
}
