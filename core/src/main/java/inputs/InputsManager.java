package inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import entities.Player;
import utilities.Constants;
import java.util.HashSet;
import java.util.Set;

public class InputsManager implements InputProcessor {
    private final Player player;
    private final Set<Integer> pressedKeys = new HashSet<>();  // Store pressed keys


    public InputsManager(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {

        pressedKeys.add(keycode);

        // todo: fix running mechanism
        if (pressedKeys.contains(Input.Keys.SHIFT_RIGHT) && pressedKeys.contains(Input.Keys.D)) {
            player.setPlayerSpeed(5);
            player.setPlayerAction(Constants.RUN);
            player.setPlayerDirection(Constants.RIGHT);
            player.setMoving(true);

            System.out.println("Player running");

            return true;
        }
        if (keycode == Input.Keys.D) {
            player.setPlayerAction(Constants.WALK);
            player.setPlayerDirection(1);
            player.setMoving(true);
        } else if (keycode == Input.Keys.A) {
            player.setPlayerAction(Constants.WALK);
            player.setPlayerDirection(-1);
            player.setMoving(true);
        }

        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(keycode);  // Remove key when released

        if (pressedKeys.isEmpty()) {
            player.setMoving(false);
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
