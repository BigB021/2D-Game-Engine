package inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;
import entities.Player;
import utilities.Constants;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles input processing for the game.
 * This class implements the {@link InputProcessor} interface to manage keyboard and touch inputs.
 * It tracks currently pressed keys and updates the player's state (movement, running, and attacking)
 * based on the input events.
 */
public class InputsManager implements InputProcessor {
    private final Player player;

    // A set to store the currently pressed keys.
    private final Set<Integer> pressedKeys = new HashSet<>();


    /**
     * Constructs an InputsManager for the specified player.
     *
     * @param player the player instance to control.
     */
    public InputsManager(Player player) {
        this.player = player;
    }


    /**
     * Handles key press events.
     * Depending on which keys are pressed, this method updates the player's action,
     * direction, and movement state. It also handles initiating the attack action with a cooldown.
     *
     * @param keycode the keycode of the key that was pressed.
     * @return true if the input was processed; false otherwise.
     */
    @Override
    public boolean keyDown(int keycode) {
        // Add the keycode to the set of currently pressed keys.
        pressedKeys.add(keycode);

        // Handle movement to the right
        if (pressedKeys.contains(Input.Keys.D)) {
            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                player.setPlayerAction(Constants.RUN);
            } else {
                player.setPlayerAction(Constants.WALK);
            }
            player.setPlayerDirection(Constants.RIGHT);
            player.setMoving(true);
            return true;
        }

        // Handle movement to the left
        if (pressedKeys.contains(Input.Keys.A)) {
            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                player.setPlayerAction(Constants.RUN);
            } else {
                player.setPlayerAction(Constants.WALK);
            }
            player.setPlayerDirection(Constants.LEFT);
            player.setMoving(true);
            return true;
        }

        // Handle attack action
        if (pressedKeys.contains(Input.Keys.X) && !player.isAttacking()) {
            long currentTimeMs = System.currentTimeMillis();

            // Check for cooldown period
            if (currentTimeMs - player.getLastAttackTime() > player.getCooldown()) {
                player.setLastAttackTime(currentTimeMs);
                player.setPlayerAction(Constants.ATTACK_1);
                player.setMoving(false);
                player.setAttacking(true);
                player.updateAnimation();
                player.setAnimation_index(3*Constants.FRAME_WIDTH);

                float attackDuration = player.getAttackAnimationDuration();

                // Reset the attack state once the attack animation is complete.
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setAttacking(false);
                        boolean shouldMove = false;
                        int newAction = Constants.IDLE;
                        int direction = player.getPlayerDirection();

                        if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.A)) {
                            shouldMove = true;
                            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                                newAction = Constants.RUN;
                            } else {
                                newAction = Constants.WALK;
                            }
                            // Update direction based on currently pressed key
                            if (pressedKeys.contains(Input.Keys.D)) {
                                direction = Constants.RIGHT;
                            } else {
                                direction = Constants.LEFT;
                            }
                        }

                        // Update the player's state after completing the attack.
                        player.setPlayerDirection(direction);
                        player.setPlayerAction(newAction);
                        player.setMoving(shouldMove);
                        player.updateAnimation();
                    }
                }, attackDuration);
            }
            return true;
        }

        return false;
    }


    /**
     * Handles key release events.
     * This method updates the player's state when keys are released, such as stopping movement
     * or reverting the action back to idle.
     *
     * @param keycode the keycode of the key that was released.
     * @return false as the event is not consumed.
     */
    @Override
    public boolean keyUp(int keycode) {
        // Remove the key from the set of currently pressed keys.
        pressedKeys.remove(keycode);

        // Switch to walking if shift is released
        if (keycode == Input.Keys.SHIFT_RIGHT) {
            if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.A)) {
                // Switch to walk if shift is released
                player.setPlayerAction(Constants.WALK);
            }
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.A) {
            // Stop movement if left or right keys are released.
            player.setMoving(false);
            player.setPlayerAction(Constants.IDLE);
        }

        // Revert to idle state if attack key is released
        if (keycode == Input.Keys.X) {
            player.setPlayerAction(Constants.IDLE);
            player.updateAnimation();
        }

        return false;
    }


    @Override
    public boolean keyTyped(char c) {
        System.out.println("keyTyped: " + c);
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        System.out.println("touchDown: " + i);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        System.out.println("touchUp: " + i);
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        System.out.println("touchCancelled: " + i);
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {

        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        System.out.println("mouseMoved: " + i);
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        System.out.println("scrolled: " + v);
        return false;
    }
}
