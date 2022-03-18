package tiles;

import core.interfaces.Draw;
import main.Main;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Tile {

    private PImage sprite;

    public Tile(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Draws the tile at a specified offset
     * @param offset the offset
     */
    public void draw(PVector offset) {
        Main.app.rectMode(PConstants.CENTER);
        PVector center = offset.add(getCenter());
        Main.app.image(sprite, center.x, center.y);
    }

    /**
     * @return where the image should be centered on the tile
     */
    public PVector getCenter() {
        return new PVector(0,0);
    }

    /**
     * @return the tile's sprite
     */
    public PImage getSprite() {
        return sprite;
    }
}
