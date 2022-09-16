package sound;

import processing.core.PApplet;
import processing.sound.SoundFile;

import static sound.SoundUtilities.playSound;
import static sound.SoundUtilities.playSoundRandomSpeed;

public class SoundWithAlts {

    private final PApplet P;
    private final SoundFile[] SOUNDS;

    /**
     * A collection of related sound files that can be shuffled
     * @param p the PApplet
     * @param name identifier
     * @param count how many alts there are
     */
    public SoundWithAlts(PApplet p, String name, int count) {
        P = p;
        SOUNDS = new SoundFile[count];
        for (int i = 0; i < count; i++) {
            SOUNDS[i] = new SoundFile(P, "sounds/withAlts/" + name + "/" + PApplet.nf(i, 3) + ".wav");
        }
    }

    /**
     * Plays a random sound from the collection with random pitch shifting.
     * @param volume play at this amp
     */
    public void playRandomWithRandomSpeed(float volume) {
        playSoundRandomSpeed(P, SOUNDS[(int) P.random(SOUNDS.length)], volume);
    }

    /**
     * Plays a random sound from the collection.
     * @param speed play at this speed
     * @param volume play at this amp
     */
    public void playRandom(float speed, float volume) {
        playSound(SOUNDS[(int) P.random(SOUNDS.length)], speed, volume);
    }
}
