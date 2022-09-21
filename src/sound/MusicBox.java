package sound;

import core.Utilities;
import core.interfaces.Update;
import main.Main;
import processing.sound.SoundFile;

import java.util.HashMap;

public class MusicBox implements Update {

    private static final float MIN_VOLUME = 0.001f;
    private static final float DEFAULT_INCREMENT_AMOUNT = 0.05f;

    private final HashMap<String, SoundFile> tracks = new HashMap<>();

    private String currentTrack;
    private String nextTrack;
    private float volume = MIN_VOLUME;
    private float incrementAmount;
    private float targetVolume;

    private static MusicBox instance;
    public static MusicBox getInstance() {
        if (instance == null) instance = new MusicBox();
        return instance;
    }
    private MusicBox() {
        for (String key : Main.sounds.keySet()) {
            if (key.contains("music_")) {
                tracks.put(key.replace("music_", ""), Main.sounds.get(key));
            }
        }
    }

    @Override
    public void update() {
        if (volume == MIN_VOLUME) {
            if (currentTrack != null) tracks.get(currentTrack).stop();
            if (nextTrack != null) {
                currentTrack = nextTrack;
                nextTrack = null;
                tracks.get(currentTrack).loop();
                tracks.get(currentTrack).amp(volume);
            }
        }
        if (currentTrack == null) return;
        volume = Utilities.smartIncrement(volume, incrementAmount, targetVolume);
        tracks.get(currentTrack).amp(volume);
    }

    public void setVolume(float volume, float proportionPerFrame) {
        targetVolume = Math.min(Math.max(MIN_VOLUME, volume), 1);
        incrementAmount = proportionPerFrame;
    }

    public void setVolume(float volume) {
        targetVolume = Math.min(Math.max(MIN_VOLUME, volume), 1);
        incrementAmount = DEFAULT_INCREMENT_AMOUNT;
    }

    public void switchTrack(String newTrack, float volume, float proportionPerFrame) {
        SoundFile track = tracks.get(newTrack);
        if (track == null) {
            System.out.println("\"" + newTrack + "\" not found, available options:\n    " + tracks);
            return;
        }
        nextTrack = newTrack;
        setVolume(volume, proportionPerFrame);
    }

    public void switchTrack(String newTrack, float volume) {
        switchTrack(newTrack, volume, DEFAULT_INCREMENT_AMOUNT);
    }

    public void stop() {
        setVolume(0, DEFAULT_INCREMENT_AMOUNT);
    }
}