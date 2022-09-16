package sprites;

import core.Walker;
import main.Main;
import processing.core.PImage;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.function.Function;

public class SpriteLoader {
    /**
     * Loads all sprites from resources/sprites. Sprites are loaded as PImages, and their names are based on
     * their filepaths. For example, a sprite with path resources/sprites/player/jump.png will be named
     * player_jump.
     * @return a hashmap of sprites
     */
    public static HashMap<String, PImage> loadSprites() {
        Walker<PImage> walker = new Walker<>(
                "sprites",
                ".png",
                (path) -> Main.app.loadImage(path));
        return walker.walk();
    }
}
