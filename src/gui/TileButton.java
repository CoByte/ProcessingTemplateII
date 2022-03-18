package gui;

import core.Utilities;
import main.Main;
import processing.core.PImage;
import processing.core.PVector;
import tiles.Tile;

public class TileButton extends Button {
    public boolean selected = false;

    public Tile tile;
    private PImage tileSprite;

    public TileButton(float x, float y, Tile tile) {
        super(x, y);
        this.size = new PVector(50, 50);

        this.tile = tile;
        tileSprite = Utilities.copyImage(tile.getSprite());
        if (tileSprite.width > tileSprite.height) {
            tileSprite.resize(50, 0);
        } else {
            tileSprite.resize(0, 50);
        }
    }

    @Override
    public void drawIdle() {
        Main.app.image(tileSprite, position.x , position.y);
        if (selected)
            Main.app.image(Main.app.sprites.get("gui_button_selected"), position.x, position.y);
    }

    @Override
    public void drawHover() {
        drawIdle();
        if (!selected)
            Main.app.image(Main.app.sprites.get("gui_button_hover"), position.x, position.y);
    }
}
