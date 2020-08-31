package com.leungjch.orbitalapp.universe;
import com.leungjch.orbitalapp.universe.Universe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;
import android.util.Log;

import com.leungjch.orbitalapp.helpers.Vector2D;

// Superclass describing all objects in the simulation


public class CelestialBody {
//  Mass determines the strength of its gravitational attraction
    private double mass;

//  Radius determines the body's size
    private double radius;

//  Position
    private Vector2D pos;

//  Velocity
    private Vector2D vel;

//  Acceleration
    private Vector2D acc;

//  Net Force
    private Vector2D fnet;

//  Color
    private Paint paint;

    Random rand = new Random();

    public void draw(Canvas canvas) {
        canvas.drawCircle((int)pos.getX(), (int)pos.getY(), (int)radius, paint);
    }

    public void update() {
        pos.setX(pos.getX() + vel.getX());
        pos.setY(pos.getY() + vel.getY());
    }

//  Calculate gravitational force of attraction induced by another object
//  https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation
    public Vector2D calculateGrav(CelestialBody object2) {


        double d = pos.distance(object2.getPos()) - radius - object2.getRadius();
        double fGravAbs = (Universe.CONSTANTS.G * mass * object2.getMass()) / Math.pow(d, 2);

        // Get x and y components
        double angle = pos.angle(object2.getPos());
        Vector2D fGrav = new Vector2D(fGravAbs*Math.cos(angle),fGravAbs*Math.sin(angle));

        return fGrav;
    }

    //
    //  Setter functions
    //
    //  Set position
    public void setPos(Vector2D newPos) {
        pos = newPos;
    }

    //  Set velocity
    public void setVel(Vector2D newVel) {
        vel = newVel;
    }

    //  Set acceleration
    public void setAcc(Vector2D new_acc) {
        acc = new_acc;
    }

    // Set net force
    public void setFnet(Vector2D new_fnet) {
        fnet = new_fnet;
    }

    // Set radius
    public void setRadius(double new_radius) {
        radius = new_radius;
    }
    // Set mass
    public void setMass(double new_mass) {
        mass = new_mass;
    }

    // Set paint
    public void setPaint(Paint new_paint) {
        paint = new_paint;
    }
    //
    //  Getter functions
    //
    //  Get position
    public Vector2D getPos() {
        return pos;
    }

    //  Get velocity
    public Vector2D getVel() {
        return vel;
    }

    //  Get acceleration
    public Vector2D getAcc() {
        return acc;
    }

    //  Get net force
    public Vector2D getFnet() {
        return fnet;
    }

    //  Get radius
    public double getRadius() {
        return radius;
    }
    //  Get mass
    public double getMass() {
        return mass;
    }
}
