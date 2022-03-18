package sprites;

import main.Main;
import processing.core.PImage;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class SpriteLoader {
    /**
     * Loads all sprites from resources/sprites. Sprites are loaded as PImages, and their names are based on
     * their filepaths. For example, a sprite with path resources/sprites/player/jump.png will be named
     * player_jump.
     * @return a hashmap of sprites
     */
    public static HashMap<String, PImage> loadSprites() {
        SpriteWalker walker = new SpriteWalker();
        return walker.walk();
    }

    private static class SpriteWalker extends SimpleFileVisitor<Path> {
        private static final Path ROOT_PATH = Paths.get("resources").toAbsolutePath();

        private HashMap<String, PImage> sprites;

        public SpriteWalker() {
            this.sprites = new HashMap<>();
        }

        public HashMap<String, PImage> walk() {
            sprites = new HashMap<>();

            try {
                Files.walkFileTree(
                        ROOT_PATH.resolve("sprites"),
                        this
                );
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }

            return sprites;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException
        {
            if (!file.toString().endsWith(".png")) return FileVisitResult.CONTINUE;

            String path = ROOT_PATH.relativize(file).toString();
            String name = path
                    .substring(8, path.length() - 4)
                    .replace('\\', '_')
                    .replace('/', '_');

            System.out.println(path);

            sprites.put(name, Main.app.loadImage(path));

            return FileVisitResult.CONTINUE;
        }
    }
}
