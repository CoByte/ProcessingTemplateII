package sound;

import processing.core.PApplet;
import processing.sound.SoundFile;

import static core.Utilities.smartIncrement;

public class FadeSoundLoop {

    private final int AUTO_STOP_TIME;
    private final SoundFile SOUND_FILE;

    private int timer;
    private float volume;

    public float targetVolume;

    /**
     * A constantly playing loop that fades to audible then back to inaudible.
     * @param p the PApplet
     * @param name identifier
     * @param autoStopTime how long it will run before automatically stopping
     */
    public FadeSoundLoop(PApplet p, String name, int autoStopTime) {
        SOUND_FILE = new SoundFile(p, "sounds/loops/" + name + ".wav");
        AUTO_STOP_TIME = autoStopTime;
        SOUND_FILE.loop(1, 0.001f);
        targetVolume = 0.001f;
        //never goes to 0 because that throws errors for some reason :/
    }

    public FadeSoundLoop(PApplet p, String name) {
        this(p, name, -1);
    }

    public void main() {
        volume = smartIncrement(volume, 0.05f, targetVolume);
        SOUND_FILE.amp(volume);
        if (timer > AUTO_STOP_TIME && AUTO_STOP_TIME != -1) targetVolume = 0.01f;
        timer++;
    }

    /**
     * @param targetVolume will slowly increment in volume to this
     */
    public void setTargetVolume(float targetVolume) {
        this.targetVolume = targetVolume;
        timer = 0;
    }
}
