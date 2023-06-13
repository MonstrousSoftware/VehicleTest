package com.monstrous.vehicletest;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

// just captures key presses and translate them to logical state variables

public class CarController extends InputAdapter {

    public boolean leftPressed;
    public boolean rightPressed;
    public boolean forwardPressed;
    public boolean backwardPressed;
    private int gearShift;       // -1, 0, 1, to be reset to 0 on processing


    public CarController() {
        leftPressed = false;
        rightPressed = false;
        forwardPressed = false;
        backwardPressed = false;
        gearShift = 0;
    }

    // -1, 0, 1 : shift down, do nothing, shift up
    public int getGearShift() {
        int ret = gearShift;
        gearShift = 0;              // make sure each shift change is only reported once
        return ret;
    }

    @Override
    public boolean keyDown(int keycode) {
        return setKeyState(keycode, true);
    }

    @Override
    public boolean keyUp(int keycode) {
        return setKeyState(keycode, false);
    }

    private boolean setKeyState(int keycode, boolean state) {

        boolean handled = true;
        switch (keycode) {
            case Input.Keys.W:
                forwardPressed = state;
                break;
            case Input.Keys.A:
                leftPressed = state;
                break;
            case Input.Keys.S:
                backwardPressed = state;
                break;
            case Input.Keys.D:
                rightPressed = state;
                break;
            case Input.Keys.UP:
                if(state)
                    gearShift = 1;
                break;
            case Input.Keys.DOWN:
                if(state)
                    gearShift = -1;
                break;
            default:
                handled = false;    // if none of the above cases, the key press is not handled
                break;
        }
       //Gdx.app.log("key state", "WASD: "+forwardPressed+leftPressed+backwardPressed+rightPressed+gearShift);
        return handled;    // did we process the key event?
    }
}
