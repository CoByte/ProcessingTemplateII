package core;

import main.Main;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.*;

import static main.Main.*;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.round;

public class Utilities {

    /**
     * Finds the distance between two points.
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two points
     */
    public static float getDistanceBetween(PVector p1, PVector p2) {
        PVector p0 = new PVector(p1.x - p2.x, p1.y - p2.y);
        return sqrt(sq(p0.x) + sq(p0.y));
    }

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

    /**
     * Finds the slope of a line.
     * @param p1 the first position
     * @param p2 the second position
     * @return the slope between the positions
     */
    public static float getSlope(PVector p1, PVector p2) {
        float m = (p2.y - p1.y) / (p2.x - p1.x);
        return m * -1;
    }

    /**
     * https://forum.processing.org/one/topic/pvector-anglebetween.html <p></p>
     * DOES NOT return the angle of a line between two points.
     * idk what this does.
     * @param p1 the first position
     * @param p2 the second position
     * @return something
     */
    @Deprecated
    public static float getAngleBetween(PVector p1, PVector p2) {
        float a = atan2((p1.y - p2.y), p1.x - p2.x);
        if (a < 0) a += TWO_PI;
        return a;
    }

    /**
     * Rounds down to a multiple of the rounder. Only works left of the decimal.
     * @param input number to be rounded
     * @param rounder number input will be rounded to a multiple of
     * @return input but rounded by rounder
     */
    public static int roundToInteger(float input, int rounder) {
        return ((int) (input / rounder)) * rounder;
    }

    /**
     * Rounds to a multiple of the rounder. Only works right of the decimal.
     * @param input number to be rounded
     * @param rounder number input will be a multiple of, must be less than 1
     * @return input rounded by the rounder
     */
    public static float roundToDecimal(float input, float rounder) {
        return (round(input / rounder)) * rounder;
    }

    /**
     * Returns the angle of a line between two points om radians.
     * Compensates for Processing y being inverted.
     * @param v1 the first position
     * @param v2 the second position
     * @return the angle of a line between the two positions in radians
     */
    public static float getAngle(PVector v1, PVector v2) {
        float angle = 0;
        PVector ratio = PVector.sub(v2, v1);
        if (v1.x == v2.x) { //if on the same x
            if (v1.y >= v2.y) { //if below target or on same y, angle right
                angle = 0;
            } else if (v1.y < v2.y) { //if above target, angle left
                angle = PI;
            }
        } else if (v1.y == v2.y) { //if on same y
            if (v1.x > v2.x) { //if  right of target, angle down
                angle = 3 * HALF_PI;
            } else if (v1.x < v2.x) { //if left of target, angle up
                angle = HALF_PI;
            }
        } else {
            if (v1.x < v2.x && v1.y > v2.y) { //if to left and below
                angle = (atan(abs(ratio.x) / abs(ratio.y)));
            } else if (v1.x < v2.x && v1.y < v2.y) { //if to left and above
                angle = (atan(abs(ratio.y) / abs(ratio.x))) + HALF_PI;
            } else if (v1.x > v2.x && v1.y < v2.y) { //if to right and above
                angle = (atan(abs(ratio.x) / abs(ratio.y))) + PI;
            } else if (v1.x > v2.x && v1.y > v2.y) { //if to right and below
                angle = (atan(abs(ratio.y) / abs(ratio.x))) + 3 * HALF_PI;
            }
        }
        return angle;
    }

    /**
     * Finds the angle of a line from the origin to a position.
     * @param v the position to draw a line to
     * @return the angle of the line
     */
    public static float getAngle(PVector v) {
        return getAngle(new PVector(0, 0), v);
    }

    /**
     * Converts an angle to be between 0 and TWO_PI.
     * @param a the angle to convert
     * @return an angle between 0 and TWO_PI
     */
    public static float normalizeAngle(float a) {
        return a - TWO_PI * floor(a / TWO_PI);
    }

    /**
     * Finds the difference between two angles.
     * @param target angle to compare to
     * @param current angle to be compared
     * @return the angle between target and current
     */
    public static float getAngleDifference(float target, float current) {
        float diffA = -(current - target);
        float diffB = diffA - TWO_PI;
        float diffC = normalizeAngle(diffB);
        float f = min(abs(diffA), abs(diffB), abs(diffC));
        if (f == abs(diffA)) return diffA;
        if (f == abs(diffB)) return diffB;
        if (f == abs(diffC)) return diffC;
        return 0;
    }

    /**
     * A better tint function that can brighten images.
     * BEWARE OF SHALLOW COPIES
     * @param image image to be tinted
     * @param tintColor what color it should be tinted
     * @param magnitude how much it should be tinted, 0-1
     * @return the new tinted image
     */
    public static PImage superTint(PImage image, Color tintColor, float magnitude) {
        image.loadPixels();
        for (int i = 0; i < image.pixels.length; i++) {
            if (image.pixels[i] != 0) {
                Color pixel = new Color(image.pixels[i]);
                float m = abs(magnitude - 1);
                pixel = new Color (
                        calcColor(pixel.getRed(), m, tintColor.getRed()),
                        calcColor(pixel.getGreen(), m, tintColor.getGreen()),
                        calcColor(pixel.getBlue(), m, tintColor.getBlue()),
                        calcColor(pixel.getAlpha(), m, tintColor.getAlpha())
                );
                image.pixels[i] = pixel.getRGB();
            }
        }
        return image;
    }

    /**
     * Calculates the value of one color channel for <tt>superTint()</tt>
     * @param p input value
     * @param m magnitude of the change
     * @param t target value
     * @return the value of a single channel
     */
    private static int calcColor(float p, float m, float t) {
        float d = t - p;
        float d2 = m * d;
        float c = p + d2;
        return (int) c;
    }

    /**
     * Displays text with a rectangle backing.
     * @param p the PApplet
     * @param text text to be displayed
     * @param position position for text to be displayed
     * @param textColor color of text, RGBA
     * @param highlightColor color of highlight, RGBA
     * @param textSize size of text
     * @param textAlign what alignment to use, defaults to center
     */
    public static void highlightedText(PApplet p, String text, PVector position, Color textColor, Color highlightColor,
                                       float textSize, int textAlign) {
        if (textAlign != LEFT && textAlign != RIGHT) textAlign = CENTER;

        p.rectMode(CENTER);
        p.noStroke();
        p.textAlign(textAlign);
        p.textSize(textSize);

        float textWidth = p.textWidth(text);
        float padding = textSize/6;
        PVector highlightPosition = new PVector(position.x, position.y);
        if (textAlign == LEFT) highlightPosition = new PVector(position.x + (textWidth/2), position.y);
        if (textAlign == RIGHT) highlightPosition = new PVector(position.x - (textWidth/2), position.y);

        p.fill(highlightColor.getRGB(), highlightColor.getAlpha());
        p.rect(highlightPosition.x, highlightPosition.y - ((textSize/2) - padding), textWidth + padding, textSize);
        p.fill(textColor.getRGB(), textColor.getAlpha());
        p.text(text, position.x, position.y);

        p.rectMode(CORNER);
    }

    /**
     * Displays text with a rectangle backing.
     * @param p the PApplet
     * @param text text to be displayed
     * @param position position for text to be displayed
     * @param textAlign what alignment to use, defaults to center
     */
    public static void highlightedText(PApplet p, String text, PVector position, int textAlign) {
        highlightedText(p, text, position, new Color(255, 255, 255, 254), new Color(0, 0, 0, 175), 25, textAlign);
    }

    /**
     * Displays text with a strikethrough.
     * @param p the PApplet
     * @param text text to display
     * @param position where text is displayed
     * @param textColor color of text and strikethrough, RGB
     * @param textSize size of text
     * @param textAlign what alignment to use, defaults to center
     */
    public static void strikethroughText(PApplet p, String text, PVector position, Color textColor, float textSize,
                                         int textAlign) {
        if (textAlign != LEFT && textAlign != RIGHT) textAlign = CENTER;

        p.textAlign(textAlign);
        p.textSize(textSize);
        p.strokeWeight(textSize/10);

        float textWidth = p.textWidth(text);
        PVector center = new PVector(position.x, position.y);
        if (textAlign == LEFT) center = new PVector(position.x + (textWidth/2), position.y);
        if (textAlign == RIGHT) center = new PVector(position.x - (textWidth/2), position.y);
        PVector leftPoint = new PVector(center.x - (textWidth/2), center.y - textSize/2);
        PVector rightPoint = new PVector(center.x + (textWidth/2), center.y - textSize/2);

        p.fill(textColor.getRGB(), textColor.getAlpha());
        p.stroke(textColor.getRGB(), textColor.getAlpha());
        p.text(text, position.x, position.y);
        p.line(leftPoint.x, leftPoint.y, rightPoint.x, rightPoint.y);

        p.strokeWeight(1);
        p.noStroke();
    }

    /**
     * Displays text with a slight 3d effect
     * @param p the PApplet
     * @param text text to be displayed
     * @param position where text will be displayed
     * @param lightColor main text color, RGBA
     * @param darkColor shadow color, RGBA
     * @param textSize size of text
     * @param textAlign alignment of text
     */
    public static void shadowedText(PApplet p, String text, PVector position, Color lightColor, Color darkColor,
                                    float textSize, int textAlign) {
        p.textAlign(textAlign);
        p.textSize(textSize);
        int offset = 2;

        p.fill(darkColor.getRGB(), darkColor.getAlpha());
        p.text(text, position.x + offset, position.y + offset);
        p.fill(lightColor.getRGB(), lightColor.getAlpha());
        p.text(text, position.x, position.y);
    }

    /**
     * Adds by to f, approaching target without overshooting it.
     * @param input starting value
     * @param by amount to increment, must be positive
     * @param target value to approach
     * @return a value closer to but not overshooting the target
     */
    public static float smartIncrement(float input, float by, float target) {
        if (input == target) return target;
        if (input < target) {
            return Math.min(input + by, target);
        }
        return Math.max(input - by, target);
    }

    /**
     * Increments one color to another.
     * @param input starting color
     * @param target target color
     * @param by amount to increment by, must be positive
     * @return input a little closer to target
     */
    public static Color smartIncrementColor(Color input, int by, Color target) {
        int red = input.getRed();
        int green = input.getGreen();
        int blue = input.getBlue();
        red = (int) smartIncrement(red, by, target.getRed());
        green = (int) smartIncrement(green, by, target.getGreen());
        blue = (int) smartIncrement(blue, by, target.getBlue());
        return new Color(red, green, blue);
    }

    /**
     * randomizes the input by a proportion.
     * @param p the PApplet
     * @param by proportion to randomize by
     */
    public static float randomizeBy(PApplet p, float input, float by) {
        return input + p.random(-input * by, input * by);
    }

    /**
     * @return the center of the grid
     */
    public static PVector getCenter() {
        return new PVector(BOARD_SIZE.x / 2, BOARD_SIZE.y / 2);
    }

    /**
     * @param color color to randomize
     * @return color but brighter, darker or the same
     */
    public static Color randomizeColorBrightness(PApplet p, Color color) {
        int r = (int) p.random(0, 3);
        if (r == 0) return color.brighter();
        if (r == 1) return color.darker();
        return color;
    }

//    /**
//     * Checks if a tile has collision
//     * @param x grid x of tile
//     * @param y grid y of tile
//     * @return if collision
//     */
//    public static boolean isTileNotPassable(int x, int y) {
//        Tile tile = tiles.get(x, y);
//        if (tile == null) return true;
//        if (tile.obstacleName != null) return true;
//        return tile.baseName == null;
//    }

//    /**
//     * Converts a world position to a grid position based on tile size
//     * @param position world position
//     * @return position rounded down to tile size
//     */
//    public static IntVector worldPositionToGridPosition(PVector position) {
//        return new IntVector(
//                roundToInteger(position.x, TILE_SIZE) / TILE_SIZE,
//                roundToInteger(position.y, TILE_SIZE) / TILE_SIZE
//        );
//    }

//    /**
//     * Checks for collision with tiles in the tile grid.
//     * Note: this will not work if the speed in pixels/frame is more than the width of the CollisionBox,
//     * or if the collision box is larger than `TILE_SIZE`.
//     * @param collisionBox a CollisionBox to check
//     * @param position position of the thing that owns that CollisionBox
//     * @param speed how fast it's moving
//     * @param xAxis it's x movement for -1 to 1
//     * @param yAxis if's y movement from -1 to 1
//     * @return two booleans describing if the object can't move in the x direction or y direction in that order
//     */
//    public static boolean[] collisionWithTiles(CollisionBox collisionBox, PVector position, float speed, int xAxis, int yAxis) {
//        boolean collideX = false, collideY = false;
//        //Constricts "collisionBox" by the ceiling of "speed" and gets what tiles it's corners are contained in,
//        Tile[] overlappingTiles = new Tile[4];
//        IntVector[] collisionBoxGridPositions = collisionBox.getCornerGridPositions(position, speed);
//        for (int i = 0; i < overlappingTiles.length; i++) {
//            overlappingTiles[i] = tiles.get(collisionBoxGridPositions[i]);
//        }
//        if (debug) {
//            overlappingTiles[0].highlight(Color.RED);
//            overlappingTiles[1].highlight(Color.YELLOW);
//            overlappingTiles[2].highlight(Color.GREEN);
//            overlappingTiles[3].highlight(Color.BLUE);
//            collisionBox.display(position);
//        }
//        //iterates through 8 directions and converts them to axes
//        for (int d = 0; d < 360; d += 45) {
//            PVector v = PVector.fromAngle(radians(d));
//            int x = round(abs(v.x));
//            int y = round(abs(v.y));
//            if (v.x < 0) x *= -1;
//            if (v.y < 0) y *= -1;
//
//            //iterates through all the overlapping tiles, checks collision with tiles in the axes directions
//            if (x != 0 && xAxis == x) {
//                for (int i = 0; i < overlappingTiles.length; i++) {
//                    if (isTileNotPassable(collisionBoxGridPositions[i].x + x, collisionBoxGridPositions[i].y)
//                            && matchCollisionBox(overlappingTiles[i], x, 0, position, collisionBox)) collideX = true;
//                }
//            } if (y != 0 && yAxis == y) {
//                for (int i = 0; i < overlappingTiles.length; i++) {
//                    if (isTileNotPassable(collisionBoxGridPositions[i].x, collisionBoxGridPositions[i].y + y)
//                            && matchCollisionBox(overlappingTiles[i], 0, y, position, collisionBox)) collideY = true;
//                }
//            }
//        }
//        return new boolean[] {collideX, collideY};
//    }

//    /**
//     * ONE AND ONLY ONE OF X OR Y *MUST* BE 0
//     */
//    private static boolean matchCollisionBox(Tile tile, int x, int y, PVector position, CollisionBox collisionBox) {
//        //checks if position matches edges of tile - the edge of the collision box
//        if (x == -1) return position.x <= tile.position.x - collisionBox.getLeftEdge();
//        if (x == 1) return position.x >= tile.position.x + TILE_SIZE - collisionBox.getRightEdge();
//        if (y == -1) return position.y <= tile.position.y - collisionBox.getTopEdge();
//        if (y == 1) return position.y >= tile.position.y + TILE_SIZE - collisionBox.getBottomEdge();
//        return false;
//    }

//    /**
//     * Gets an angle from axes, compensating for collision
//     * @param speed how fast it's moving, prevent from getting stuck
//     * @param position position of collision box
//     * @param collisionBox box to check
//     * @param axes what cardinal direction it's moving in
//     * @return an angle
//     */
//    public static float getDirectionFromAxesWithCollision(float speed, PVector position, CollisionBox collisionBox, IntVector axes) {
//        int xAxis, yAxis, direction = 0;
//        xAxis = axes.x;
//        yAxis = axes.y;
//
//        boolean[] collisions = collisionWithTiles(collisionBox, position, speed, xAxis, yAxis);
//        if (collisions[0]) xAxis = 0;
//        if (collisions[1]) yAxis = 0;
//
//        switch (xAxis) {
//            case -1:
//                switch (yAxis) {
//                    case -1:
//                        direction = 225;
//                        break;
//                    case 0:
//                        direction = 180;
//                        break;
//                    case 1:
//                        direction = 135;
//                        break;
//                }
//                break;
//            case 0:
//                switch (yAxis) {
//                    case -1:
//                        direction = 270;
//                        break;
//                    case 0:
//                        //will not
//                        return -1;
//                    case 1:
//                        direction = 90;
//                        break;
//                }
//                break;
//            case 1:
//                switch (yAxis) {
//                    case -1:
//                        direction = 315;
//                        break;
//                    case 0:
//                        direction = 0;
//                        break;
//                    case 1:
//                        direction = 45;
//                        break;
//                }
//                break;
//        }
//
//        return radians(direction);
//    }

//    /**
//     * @return cardinal axes from WASD or IJKL
//     */
//    public static IntVector getAxesFromMovementKeys() {
//        boolean left, right, up, down;
//        IntVector axes = new IntVector(0, 0);
//
//        left = keysPressed.getPressed('a') || keysPressed.getPressed('j');
//        right = keysPressed.getPressed('d') || keysPressed.getPressed('l');
//        up = keysPressed.getPressed('w') || keysPressed.getPressed('i');
//        down = keysPressed.getPressed('s') || keysPressed.getPressed('k');
//
//        axes.x = (right ? 1 : 0) - (left ? 1 : 0);
//        axes.y = (down ? 1 : 0) - (up ? 1 : 0);
//
//        return axes;
//    }

    /**
     * @param angle angle to check
     * @return if angle is facing towards the left
     */
    public static boolean angleIsFacingLeftStandard(float angle) {
        angle = normalizeAngle(angle);
        return angle < HALF_PI || angle > PI + HALF_PI;
    }

    /**
     * @param angle angle to check
     * @return if angle is facing towards the top of the screen
     */
    public static boolean angleIsFacingUpStandard(float angle) {
        angle = normalizeAngle(angle);
        return angle < PI;
    }

    public static boolean angleIsFacingLeftWeird(float angle) {
        boolean facingRight = true;
        if (angle <= QUARTER_PI) facingRight = false;
        else if (angle >= HALF_PI + QUARTER_PI && angle < PI + QUARTER_PI) facingRight = true;
        else if (angle >= PI + HALF_PI + QUARTER_PI) facingRight = false;
        return !facingRight;
    }
}
