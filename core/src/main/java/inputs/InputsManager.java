package inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;
import entities.Player;
import utilities.Constants;
import java.util.HashSet;
import java.util.Set;

public class InputsManager implements InputProcessor {
    private final Player player;
    private final Set<Integer> pressedKeys = new HashSet<>();


    public InputsManager(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        pressedKeys.add(keycode);

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

        // todo: cooldown needs more refining
        if (pressedKeys.contains(Input.Keys.X) && !player.isAttacking()) {
            long currentTimeMs = System.currentTimeMillis();

            if (currentTimeMs - player.getLastAttackTime() > player.getCooldown()) {
                player.setLastAttackTime(currentTimeMs); // Set last attack time
                player.setPlayerAction(Constants.ATTACK_1);
                player.setMoving(false);
                player.setAttacking(true);
                player.setAnimation();

                float attackDuration = player.getAttackAnimationDuration();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setAttacking(false);
                        player.setPlayerAction(Constants.IDLE);
                        player.setAnimation();
                    }
                }, attackDuration);
            }
            return true;
        }

        return false;
    }



    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(keycode);

        if (keycode == Input.Keys.SHIFT_RIGHT) {
            if (pressedKeys.contains(Input.Keys.D) || pressedKeys.contains(Input.Keys.A)) {
                // Switch to walk if shift is released
                player.setPlayerAction(Constants.WALK);
            }
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.A) {
            player.setMoving(false);
            player.setPlayerAction(Constants.IDLE);
        }

        if (keycode == Input.Keys.X) {

            player.setPlayerAction(Constants.IDLE);
            player.setAnimation();
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
