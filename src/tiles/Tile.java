package tiles;

import main.Main;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;

public class Tile {

    private String spriteName;
    private PImage sprite;

    public Tile(String spriteName) {
        this.spriteName = spriteName;
        sprite = Main.sprites.get(spriteName);
    }

    public Tile(JSONObject serial) {
        spriteName = serial.getString("sprite");
        sprite = Main.sprites.get(spriteName);
    }

    /**
     * Draws the tile at a specified offset
     * @param offset the offset
     */
    public void draw(PVector offset) {
        Main.app.rectMode(PConstants.CENTER);
        Main.app.image(sprite, offset.x, offset.y);
    }

    /**
     * @return the tile's sprite
     */
    public PImage getSprite() {
        return sprite;
    }

    public JSONObject serialize() {
        JSONObject data = new JSONObject();
        data.setString("sprite", spriteName);
        return data;
    }
}
