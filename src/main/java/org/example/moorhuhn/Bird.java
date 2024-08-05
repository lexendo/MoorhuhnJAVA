package org.example.moorhuhn;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * Represents a bird in the game, which is a type of chicken with animated frames.
 */
public class Bird extends Chicken {
    private Image[] frames;
    private IntegerProperty currentFrameIndex = new SimpleIntegerProperty(0);

    /**
     * Constructs a Bird with the specified coordinates, size, and animation frames.
     *
     * @param x the x-coordinate of the bird.
     * @param y the y-coordinate of the bird.
     * @param size the size of the bird.
     * @param frames the animation frames of the bird.
     */
    public Bird(double x, double y, int size, Image[] frames) {
        super(x, y, size);
        this.frames = frames;
    }

    /**
     * Gets the current animation frame of the bird.
     *
     * @return the current animation frame.
     */
    public Image getCurrentFrame() {
        return frames[currentFrameIndex.get()];
    }

    /**
     * Advances to the next animation frame of the bird.
     */
    public void nextFrame() {
        currentFrameIndex.set((currentFrameIndex.get() + 1) % frames.length);
    }
}
