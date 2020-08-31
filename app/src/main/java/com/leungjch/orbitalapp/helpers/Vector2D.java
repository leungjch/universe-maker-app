package com.leungjch.orbitalapp.helpers;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double new_x, double new_y) {
        x = new_x;
        y = new_y;
    }

    // Return distance
    public double distance(Vector2D vec2) {
        return Math.sqrt(Math.pow(x-vec2.getX(),2) + Math.pow(y-vec2.getY(),2));
    }

    // Return angle between two vectors
    public double angle(Vector2D vec2) {
        return Math.atan((vec2.getX() - x)/(vec2.getY() - y));
    }

    // Getter and setter functions
    public void set(double new_x, double new_y) {
        x = new_x;
        y = new_y;
    }
    public void setX(double new_x) {
        x = new_x;
    }
    public void setY(double new_y) {
        y = new_y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
