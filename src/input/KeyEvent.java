package input;

import core.triggers.StateTrigger;

public class KeyEvent extends StateTrigger {

    public final short key;

    public KeyEvent(short key) {
        this.key = key;
    }

    public void testPress(short testKey) {
        if (testKey == key) setState(true);
    }

    public void testRelease(short testKey) {
        if (testKey == key) setState(false);
    }
}
