package main;

import input.InputManager;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import sprites.SpriteLoader;
import tiles.Tile;
import tiles.Tilemap;
import tiles.TilemapBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends PApplet {

    public static Main app = null;

    public static final int FRAMERATE = 60;
    public static final int DEFAULT_MODE = CORNER;
    public static final PVector BOARD_SIZE = new PVector(1100, 900);
    public static final String TITLE = "template";

    public static float globalVolume = 0.25f;
    public static boolean debug = false;

    private static final Color BACKGROUND_COLOR = new Color(0, 15, 45);
    private static final boolean FULLSCREEN = true;

    private static float matrixScale;
    private static float matrixOffset;

    public static HashMap<String, PImage> sprites = new HashMap<>();
    public static HashMap<String, PImage[]> animations;

//    public static Sound sound;
//    public static HashMap<String, SoundFile> sounds;
//    public static HashMap<String, StartStopSoundLoop> startStopSoundLoops;
//    public static HashMap<String, FadeSoundLoop> fadeSoundLoops;
//    public static HashMap<String, SoundWithAlts> soundsWithAlts;

    public static PVector priorMatrixMousePosition;
    public static PVector matrixMousePosition;

    private final InputManager inputManager = InputManager.getInstance();

    public static void main(String[] args) {
        PApplet.main("main.Main", args);
    }

    @Override
    public void settings() {
        if (FULLSCREEN) {
            fullScreen(P2D);
            noSmooth();
        } else size((int) BOARD_SIZE.x, (int) BOARD_SIZE.y, P2D);

        app = this;
    }

    @Override
    public void setup() {
        frameRate(FRAMERATE);
        surface.setTitle(TITLE);

        sprites = SpriteLoader.loadSprites();

//        setupSound();
        setupFullscreen();
    }

    /**
     * Gets frames of an animation from loaded sprites. Animations should take the name "path_frameNumber"
     * @param path the path to the animation
     * @return the frames of the animation
     */
    private PImage[] framesFromSprites(String path) {
        ArrayList<PImage> images = new ArrayList<>();
        for (int i = 0; true; i++) {
            String framePath = path + i;
            if (!sprites.containsKey(framePath)) break;
            images.add(sprites.get(framePath));
        }
        return images.toArray(new PImage[images.size()]);
    }

    private void setupFullscreen() {
        if (hasVerticalBars()) {
            matrixScale = height / BOARD_SIZE.y;
            matrixOffset = (width - (BOARD_SIZE.x * matrixScale)) / 2;
        } else {
            matrixScale = width / BOARD_SIZE.x;
            matrixOffset = (height - (BOARD_SIZE.y * matrixScale)) / 2;
        }
    }

//    private void setupSound() {
//        sound = new Sound(this);
//        sounds = new HashMap<>();
//        startStopSoundLoops = new HashMap<>();
//        fadeSoundLoops = new HashMap<>();
//        soundsWithAlts = new HashMap<>();
//        loadSounds(this);
//    }

    @Override
    public void draw() {
        inputManager.update();
        background(BACKGROUND_COLOR.getRGB());
//        drawSound();

        pushFullscreen();
        
        popFullscreen();
    }

//    private void drawSound() {
//        sound.volume(globalVolume);
//        for (StartStopSoundLoop startStopSoundLoop : startStopSoundLoops.values()) startStopSoundLoop.continueLoop();
//        for (FadeSoundLoop fadeSoundLoop : fadeSoundLoops.values()) fadeSoundLoop.main();
//    }

    private void pushFullscreen() {
        pushMatrix();
        if (hasVerticalBars()) translate(matrixOffset, 0);
        else translate(0, matrixOffset);
        scale(matrixScale);
        priorMatrixMousePosition = matrixMousePosition;
        if (hasVerticalBars()) {
            matrixMousePosition = new PVector((mouseX - matrixOffset) / matrixScale, mouseY / matrixScale);
        } else {
            matrixMousePosition = new PVector(mouseX / matrixScale, (mouseY - matrixOffset) / matrixScale);
        }
    }

    private void popFullscreen() {
        popMatrix();
        drawBlackBars();
    }

    private boolean hasVerticalBars() {
        float screenRatio = width / (float) height;
        float boardRatio = BOARD_SIZE.x / BOARD_SIZE.y;
        return boardRatio < screenRatio;
    }

    private void drawBlackBars() {
        fill(0);
        rectMode(CORNER);
        noStroke();
        if (hasVerticalBars()) {
            rect(0, 0, matrixOffset, height);
            rect(width - matrixOffset, 0, matrixOffset, height);
        } else {
            rect(0, 0, width, matrixOffset);
            rect(0, height - matrixOffset, width, matrixOffset);
        }
        rectMode(DEFAULT_MODE);
    }

    @Override
    public void keyPressed() {
        inputManager.testPresses((short) keyCode);
    }

    @Override
    public void keyReleased() {
        inputManager.testReleases((short) keyCode);
    }

    @Override
    public void mousePressed() {
        if (mouseButton == LEFT) inputManager.leftMouse.setState(true);
        else if (mouseButton == RIGHT) inputManager.rightMouse.setState(true);
        else if (mouseButton == CENTER) inputManager.middleMouse.setState(true);
    }

    @Override
    public void mouseReleased() {
        if (mouseButton == LEFT) inputManager.leftMouse.setState(false);
        else if (mouseButton == RIGHT) inputManager.rightMouse.setState(false);
        else if (mouseButton == CENTER) inputManager.middleMouse.setState(false);
    }
}
