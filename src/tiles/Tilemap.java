package tiles;

import core.interfaces.Draw;
import main.Main;
import processing.core.PConstants;
import processing.core.PVector;

public class Tilemap implements Draw {

    public final int width;
    public final int height;
    public final float tileSize;

    public Tile[][] map;

    public Tilemap(int width, int height, float tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

        map = new Tile[width][height];
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
}