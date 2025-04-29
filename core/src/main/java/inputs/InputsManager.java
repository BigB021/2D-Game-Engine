package inputs;

import com.badlogic.gdx.Gdx;
import physics.JumpPhysics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;
import entities.Player;

import java.util.HashSet;
import java.util.Set;

import static utilities.constants.EntityConstants.*;
import static utilities.constants.FramesConstants.FRAME_WIDTH;


import static utilities.constants.AudiConstants.*;
import static utilities.constants.EntityConstants.*;
import static utilities.constants.FramesConstants.FRAME_WIDTH;
import audio.SoundController;
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
    SoundController soundController;

    /**
     * Constructs an InputsManager for the specified player.
     *
     * @param player the player instance to control.
     */
    public InputsManager(Player player) {
        this.player = player;
        soundController = new SoundController();

        /*
        if (soundController.isSoundEnabled()) {
            soundController.playSound("FAST_SIMPLE_CHOP", 1f, true);
            //soundController.playSound("METAL_PLATE", 1f, false);
        }

         */
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
        // Sound effects for player
        // TEST : testing player's sounds
        soundController.addNewSound("RUNNING_SOUND", soundController.generateSoundFromPath(RUNNING_SOUND));
        soundController.addNewSound("FAST_SIMPLE_CHOP", soundController.generateSoundFromPath(FAST_SIMPLE_CHOP));
        soundController.addNewSound("METAL_PLATE", soundController.generateSoundFromPath(METAL_PLATE));
        soundController.addNewSound("ATTACK_1_SOUND", soundController.generateSoundFromPath(ATTACK_1_SOUND));


        // Add the keycode to the set of currently pressed keys.
        pressedKeys.add(keycode);

        // Handle movement to the right
        if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.RIGHT)) {
            soundController.playSound("RUNNING_SOUND", 1f, true);
            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                player.setPlayerAction(RUN);
            } else {
                player.setPlayerAction(WALK);
            }
            player.setEntityDirection(RIGHT);
            player.setMoving(true);
            return true;
        }

        // Handle movement to the left
        if (pressedKeys.contains(Input.Keys.A) || pressedKeys.contains(Input.Keys.LEFT)) {
            soundController.playSound("RUNNING_SOUND", 1f, true);
            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                player.setPlayerAction(RUN);
            } else {
                player.setPlayerAction(WALK);
            }
            player.setEntityDirection(LEFT);
            player.setMoving(true);
            return true;
        }

        // Handle attack action
        if (pressedKeys.contains(Input.Keys.X) && !player.isAttacking()) {
            long currentTimeMs = System.currentTimeMillis();
            Gdx.app.log("ATTACK", "Lancement de l'attaque !");
            /*
            soundController.stopSound("METAL_PLATE");
            soundController.playSound("METAL_PLATE", 1f, false);

             */
            soundController.stopSound("ATTACK_1_SOUND");
            soundController.playSound("ATTACK_1_SOUND", 1f, false);
            // Check for cooldown period
            if (currentTimeMs - player.getLastAttackTime() > player.getCooldown()) {
                player.setLastAttackTime(currentTimeMs);
                player.setPlayerAction(ATTACK_1);
                player.setMoving(false);
                player.setAttacking(true);
                player.updateAnimation();
                player.setAnimation_index(3*FRAME_WIDTH);

                float attackDuration = player.getAttackAnimationDuration();

                // Reset the attack state once the attack animation is complete.
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setAttacking(false);
                        boolean shouldMove = false;
                        int newAction = IDLE;
                        int direction = player.getEntityDirection();

                        if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.A) || pressedKeys.contains(Input.Keys.LEFT)) {
                            shouldMove = true;
                            if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT)) {
                                newAction = RUN;
                                /*
                                soundController.stopSound("RUNNING_SOUND");
                                double rand = (Math.random() * 2.1)+0.5;
                                soundController.playSound("RUNNING_SOUND", 1f, true, (float) rand);

                                 */
                            } else {
                                newAction = WALK;
                            }
                            // Update direction based on currently pressed key
                            if (pressedKeys.contains(Input.Keys.A)|| pressedKeys.contains(Input.Keys.LEFT) ) {
                                direction = LEFT;
                            } else {
                                direction = RIGHT;
                            }
                        }

                        // Update the player's state after completing the attack.
                        player.setEntityDirection(direction);
                        player.setPlayerAction(newAction);
                        player.setMoving(shouldMove);
                        player.updateAnimation();
                    }
                }, attackDuration);
            }
            return true;
        }

        // Handle jumping
        if (keycode == Input.Keys.SPACE) {
            if (player.isEntityJumping()) {
                soundController.stopSound("FAST_SIMPLE_CHOP");
                soundController.playSound("FAST_SIMPLE_CHOP", 1f, false);
                JumpPhysics.jumpPlayer(player);
                player.setPlayerAction(JUMP);    // ← switch into jump action
                player.setMoving(false);         // optional: stop any horizontal walk/run
                player.updateAnimation();        // force the jump texture to load immediately
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
            if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.A) || pressedKeys.contains(Input.Keys.LEFT) || pressedKeys.contains(Input.Keys.RIGHT)) {
                // Switch to walk if shift is released
                player.setPlayerAction(WALK);
            }
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.A || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            // Stop movement if left or right keys are released.
            soundController.stopSound("RUNNING_SOUND");
            player.setMoving(false);
            player.setPlayerAction(IDLE);
        }

        // Revert to idle state if attack key is released
        if (keycode == Input.Keys.X) {
            player.setPlayerAction(IDLE);
            player.updateAnimation();
        }

        if(keycode == Input.Keys.P){
            soundController.stopSound("METAL_PLATE");
            soundController.playSound("METAL_PLATE", 1f, false);
        }



        return false;
    }

    public SoundController getSoundController(){
        return soundController;
    }


    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {

        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
