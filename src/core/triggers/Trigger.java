package core.triggers;

/**
 * Represents a thing that can trigger, like a button or timer. Has a few methods for determining useful stuff.
 */
public class Trigger {
    private boolean wasTriggered = false;
    private boolean isTriggered = false;

    public void triggerState(boolean active) {
        wasTriggered = isTriggered;
        isTriggered = active;
    }

    /**
     * Call when the trigger is activated.
     */
    public void activate() {
        triggerState(true);
    }

    /**
     * Call when the trigger is deactivated
     */
    public void deactivate() {
        triggerState(false);
    }

    /**
     * @return if the trigger is on the rising edge of its activation (e.g. if a button was just pressed)
     */
    public boolean rising() {
        return !wasTriggered && isTriggered;
    }

    /**
     * @return if the trigger is on the falling edge of its activation (e.g. if a button was just released)
     */
    public boolean falling() {
        return wasTriggered && !isTriggered;
    }

    public boolean triggered() {
        return isTriggered;
    }
}
