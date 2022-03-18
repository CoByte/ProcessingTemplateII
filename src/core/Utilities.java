package core;

import main.Main;
import processing.core.PImage;
import processing.core.PVector;

public class Utilities {
    /**
     * Detects if a point in inside the box created by top left and bottom right
     * @param topLeft the top left coordinate of the box
     * @param bottomRight the bottom right coordinate of the box
     * @param point the point to test
     * @return whether the point is inside the box
     */
    public static boolean inBox(PVector topLeft, PVector bottomRight, PVector point) {
        return topLeft.x <= point.x &&
                point.x <= bottomRight.x &&
                topLeft.y <= point.y &&
                point.y <= bottomRight.y;
    }

    public static PImage copyImage(PImage image) {
        int width = image.width;
        int height = image.height;
        PImage clone = Main.app.createImage(width, height, image.format);
        clone.copy(image, 0, 0, width, height, 0, 0, width, height);
        return clone;
    }
}
