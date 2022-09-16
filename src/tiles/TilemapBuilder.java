package tiles;

import com.jogamp.newt.event.KeyEvent;
import core.BoxCollider;
import core.Utilities;
import core.interfaces.Draw;
import core.interfaces.Update;
import gui.TileButton;
import input.InputManager;
import main.Main;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class TilemapBuilder implements Update, Draw {

    private final InputManager input = InputManager.getInstance();

    private final Tilemap map;
    private final ArrayList<TileButton> tileButtons;
    private TileButton selectedTile;

    private final PVector offset = new PVector(-300, 0);
    private PVector offsetMouse = new PVector(0, 0);

    private BoxCollider tileButtonBox;

    public TilemapBuilder(Tilemap map, String... spriteNames) {
        this.map = map;

        tileButtons = new ArrayList<>();
        for (int i = 0; i < spriteNames.length; i++) {
            int x = i % 4;
            int y = i / 4;
            tileButtons.add(new TileButton(
                    x * 50, y * 50,
                    new Tile(spriteNames[i])
            ));
        }

        tileButtonBox = new BoxCollider(
                new PVector(0, 0),
                new PVector(300, Main.BOARD_SIZE.y));
    }

    @Override
    public void update() {
        offsetMouse = PVector.add(offset, Main.fullscreenMousePosition);

        for (TileButton button : tileButtons) {
            button.update();
            if (button.rising()) {
                if (selectedTile != null) selectedTile.selected = false;
                button.selected = true;
                selectedTile = button;
            }
        }

        if (input.middleMouse.triggered()) {
            offset.sub(PVector.sub(Main.fullscreenMousePosition, Main.priorMatrixMousePosition));
        }

        if (mouseOnGrid()) {
            if (input.leftMouse.triggered() && selectedTile != null) {
                int tile_x = (int) offsetMouse.x / (int) map.tileSize;
                int tile_y = (int) offsetMouse.y / (int) map.tileSize;
                map.set(selectedTile.tile, tile_x, tile_y);
            }

            if (input.rightMouse.triggered()) {
                int tile_x = (int) offsetMouse.x / (int) map.tileSize;
                int tile_y = (int) offsetMouse.y / (int) map.tileSize;
                map.set(null, tile_x, tile_y);
            }
        }

        if (input.getEvent(KeyEvent.VK_Z).rising()) {
            System.out.println("Wack");
            map.save();
        }
    }

    @Override
    public void draw() {
        Main.app.pushMatrix();

        Main.app.translate(-offset.x, -offset.y);

        map.draw();

        Main.app.rectMode(PConstants.CORNER);
        Main.app.stroke(0, 0, 255);
        Main.app.strokeWeight(4);
        Main.app.fill(0,0);
        if (mouseOnGrid()) {
            PVector gridOffset = PVector.sub(
                    offsetMouse,
                    new PVector(offsetMouse.x % map.tileSize, offsetMouse.y % map.tileSize)
            );
            Main.app.rect(gridOffset.x, gridOffset.y, map.tileSize, map.tileSize);
        }

        Main.app.popMatrix();

        // draws the tile rect
        Main.app.rectMode(PConstants.CORNER);
        Main.app.fill(255);
        Main.app.rect(0, 0, 300, Main.app.BOARD_SIZE.y);
        tileButtons.forEach(TileButton::draw);
    }

    private boolean mouseOnGrid() {
        return !tileButtonBox.inBox(Main.matrixMousePosition)
                && map.tileMapBox.inBox(offsetMouse);
    }
}
