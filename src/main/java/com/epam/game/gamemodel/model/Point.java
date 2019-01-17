package com.epam.game.gamemodel.model;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Creates a new {@link Point} object with position of {@code this}, rotated
     * around {@code center} by {@code angle}
     * 
     * @param center
     * @param angle
     * @return
     */
    public Point rotateAround(Point center, double angle) {
        Point result = new Point(0, 0);
        result.x = center.x + (x - center.x) * Math.cos(angle) - (y - center.y) * Math.sin(angle);
        result.y = center.y + (x - center.x) * Math.sin(angle) + (y - center.y) * Math.cos(angle);
        return result;
    }

    /**
     * Creates a new {@link Point} object with position of {@code this},
     * distanced from {@code center} point by multiplier.
     * 
     * @param x
     * @return
     */
    public Point changeDistance(Point center, double mult) {
        Point result = new Point(0, 0);
        result.x = center.x + ((this.x - center.x) * mult);
        result.y = center.y + ((this.y - center.y) * mult);
        return result;
    }

    public double getDistance(Point another) {
        return Math.hypot(another.x - this.x, another.y - this.y);
    }

    /**
     * Creates a new {@link Point} object with position of {@code this},
     * distanced from {@code center} point by specified distance.
     * 
     * @param center
     * @param distance
     * @return
     */
    public Point changeDistance(Point center, int distance) {
        double multiplier = (getDistance(center) + distance) / getDistance(center);
        return changeDistance(center, multiplier);
    }
}
