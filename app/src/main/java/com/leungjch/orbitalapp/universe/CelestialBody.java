package com.leungjch.orbitalapp.universe;

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
        Log.d("CelestialBody", Double.toString(radius));
        pos.setX(pos.getX() + vel.getX());
        pos.setY(pos.getY() + vel.getY());
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
