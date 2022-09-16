package sound;

import core.Walker;
import main.Main;
import processing.sound.SoundFile;
import sprites.SpriteLoader;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class SoundLoader {

    /**
     * Loads all sprites from resources/sounds. Sounds are loaded as SoundFiles, and their names are based on
     * their filepaths. For example, a sound with path resources/sounds/player/jump.wav will be named
     * player_jump.
     * @return a hashmap of sounds
     */
    public static HashMap<String, SoundFile> loadSounds() {
        Walker<SoundFile> walker = new Walker<>(
                "sounds",
                ".wav",
                (path) -> new SoundFile(Main.app, path));
        return walker.walk();
    }
}
