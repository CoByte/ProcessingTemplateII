package sound;

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
        SoundLoader.SoundWalker walker = new SoundWalker();
        return walker.walk();
    }

    private static class SoundWalker extends SimpleFileVisitor<Path> {
        private static final Path ROOT_PATH = Paths.get("resources").toAbsolutePath();

        private HashMap<String, SoundFile> sounds;

        public SoundWalker() {
            this.sounds = new HashMap<>();
        }

        public HashMap<String, SoundFile> walk() {
            sounds = new HashMap<>();

            try {
                Files.walkFileTree(
                        ROOT_PATH.resolve("sounds"),
                        this
                );
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }

            return sounds;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException
        {
            if (!file.toString().endsWith(".wav")) return FileVisitResult.CONTINUE;

            String path = ROOT_PATH.relativize(file).toString();
            String name = path
                    .substring(8, path.length() - 4)
                    .replace('\\', '_')
                    .replace('/', '_');

            System.out.println(path);

            sounds.put(name, new SoundFile(Main.app, path));

            return FileVisitResult.CONTINUE;
        }
    }
}
