package core.triggers;

/** Like a trigger, but with an internal state that can be shut on and off */
public class StateTrigger extends Trigger {
    private boolean state = false;

    public void setState(boolean state) {
        this.state = state;
    }

    public void update() {
        triggerState(state);
    }
}
