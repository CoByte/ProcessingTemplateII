package gui;

import core.interfaces.Draw;
import core.interfaces.Update;
import core.Utilities;
import core.triggers.Trigger;
import input.InputManager;
import main.Main;
import processing.core.PVector;

public abstract class Button extends Trigger implements Update, Draw {
    public enum ButtonState {
        Idle,
        Hover,
        Press,
    }

    private final InputManager input = InputManager.getInstance();

    public ButtonState state = ButtonState.Idle;

    public PVector position;
    public PVector size;

    protected Button(float x, float y) {
        position = new PVector(x, y);
    }

    @Override
    public void update() {
        updateState();
    }

    public void updateState() {
        boolean isPressed = false;

        if (Utilities.inBox(position, PVector.add(position, size), Main.fullscreenMousePosition)) {
            if (input.leftMouse.triggered()) {
                state = ButtonState.Press;
                isPressed = true;
            } else {
                state = ButtonState.Hover;
            }
        } else {
            state = ButtonState.Idle;
        }

        triggerState(isPressed);
    }

    @Override
    public void draw() {
        switch (state) {
            case Idle:
                drawIdle();
                break;
            case Hover:
                drawHover();
                break;
            case Press:
                drawPress();
                break;
        }
    }

    public abstract void drawIdle();
    public void drawHover() { drawIdle(); }
    public void drawPress() { drawIdle(); }
}
