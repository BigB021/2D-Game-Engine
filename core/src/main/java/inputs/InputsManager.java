package inputs;

import com.badlogic.gdx.InputProcessor;

public class InputsManager implements InputProcessor {


    @Override
    public boolean keyDown(int i) {
        System.out.println("keyDown: " + i);
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        System.out.println("keyUp: " + i);
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
