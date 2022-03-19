package tiles;

import core.interfaces.Draw;
import main.Main;
import processing.core.PConstants;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Tilemap implements Draw {

    public final String name;
    public final int width;
    public final int height;
    public final float tileSize;

    public Tile[][] map;

    public Tilemap(String name, int width, int height, float tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.name = name;

        map = new Tile[width][height];
        try {
            if (!new File("resources/tilemaps/" + name + ".json").createNewFile()) {
                JSONArray tileData = Main.app.loadJSONArray("resources/tilemaps/" + name + ".json");
                for (int i = 0; i < tileData.size(); i++) {
                    JSONObject tile = tileData.getJSONObject(i);
                    int x = tile.getInt("x");
                    int y = tile.getInt("y");
                    if (0 <= x && x < width && 0 <= y && y < height) {
                        JSONObject t = tile.getJSONObject("data");
                        map[x][y] = new Tile(t);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create file resources/tilemaps/" + name + ".json" + " (" + e.toString() + ")");
        }
    }

    public Tile get(int x, int y) {
        return map[x][y];
    }

    public void set(Tile t, int x, int y) {
        map[x][y] = t;
    }

    @Override
    public void draw() {
        Main.app.rectMode(PConstants.CENTER);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile t = map[x][y];
                if (t == null) continue;

                PVector offset = new PVector(
                        tileSize * x,
                        tileSize * y
                );

                t.draw(offset);
            }
        }
    }

    public void save() {
        Main.app.saveJSONArray(serializeTiles(), "resources/tilemaps/" + name + ".json");
    }

    public JSONArray serializeTiles() {
        JSONArray serializedTiles = new JSONArray();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile t = map[x][y];
                if (t == null) continue;
                JSONObject tileData = new JSONObject();
                tileData.setInt("x", x);
                tileData.setInt("y", y);
                tileData.setJSONObject("data", t.serialize());
                serializedTiles.append(tileData);
            }
        }
        return serializedTiles;
    }
}