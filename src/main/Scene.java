package main;

import com.jogamp.newt.event.KeyEvent;
import core.interfaces.Draw;
import core.interfaces.Update;
import input.InputManager;
import tiles.Tilemap;
import tiles.TilemapBuilder;

import java.util.ArrayList;

public abstract class Scene implements Update, Draw {
    private boolean editTiles = false;
    private int tileLayer = 0;

    public ArrayList<Tilemap> tilemaps;
    public ArrayList<TilemapBuilder> builders;

    private InputManager input = InputManager.getInstance();

    @Override
    public void update() {
        if (input.getEvent(KeyEvent.VK_X).rising()) {
            editTiles = !editTiles;
        }

        if (editTiles) {
            for (short i = KeyEvent.VK_1; i <= KeyEvent.VK_9; i++) {
                if (input.getEvent(i).rising()) {
                    int index = i - KeyEvent.VK_1;
                    if (index < builders.size()) tileLayer = index;
                }
            }
            builders.get(tileLayer).update();
        } else {
            gameUpdate();
        }
    }

    public abstract void gameUpdate();

    @Override
    public void draw() {
        if (editTiles) {
            builders.get(tileLayer).draw();
        } else {
            drawUpdate();
        }
    }

    public abstract void drawUpdate();
}
