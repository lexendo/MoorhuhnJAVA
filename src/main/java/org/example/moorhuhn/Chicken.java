package org.example.moorhuhn;

import javafx.beans.property.*;

/**
 * Represents a chicken in the game with x and y coordinates and a size.
 */
public class Chicken {
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final IntegerProperty size = new SimpleIntegerProperty();

    /**
     * Constructs a Chicken with the specified coordinates and size.
     *
     * @param x the x-coordinate of the chicken.
     * @param y the y-coordinate of the chicken.
     * @param size the size of the chicken.
     */
    public Chicken(double x, double y, int size) {
        this.x.set(x);
        this.y.set(y);
        this.size.set(size);
    }

    /**
     * Gets the x-coordinate of the chicken.
     *
     * @return the x-coordinate of the chicken.
     */
    public double getX() {
        return x.get();
    }

    /**
     * Sets the x-coordinate of the chicken.
     *
     * @param x the new x-coordinate of the chicken.
     */
    public void setX(double x) {
        this.x.set(x);
    }

    /**
     * Gets the x property of the chicken.
     *
     * @return the x property of the chicken.
     */
    public DoubleProperty xProperty() {
        return x;
    }

    /**
     * Gets the y-coordinate of the chicken.
     *
     * @return the y-coordinate of the chicken.
     */
    public double getY() {
        return y.get();
    }

    /**
     * Sets the y-coordinate of the chicken.
     *
     * @param y the new y-coordinate of the chicken.
     */
    public void setY(double y) {
        this.y.set(y);
    }

    /**
     * Gets the y property of the chicken.
     *
     * @return the y property of the chicken.
     */
    public DoubleProperty yProperty() {
        return y;
    }

    /**
     * Gets the size of the chicken.
     *
     * @return the size of the chicken.
     */
    public int getSize() {
        return size.get();
    }

    /**
     * Sets the size of the chicken.
     *
     * @param size the new size of the chicken.
     */
    public void setSize(int size) {
        this.size.set(size);
    }
}
