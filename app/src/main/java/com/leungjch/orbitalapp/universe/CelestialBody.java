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
    public void set_pos(Vector2D new_pos) {
        pos = new_pos;
    }

    //  Set velocity
    public void set_vel(Vector2D new_vel) {
        vel = new_vel;
    }

    //  Set acceleration
    public void set_acc(Vector2D new_acc) {
        acc = new_acc;
    }

    // Set net force
    public void set_fnet(Vector2D new_fnet) {
        fnet = new_fnet;
    }

    // Set radius
    public void set_radius(double new_radius) {
        radius = new_radius;
    }

    // Set paint
    public void set_paint(Paint new_paint) {
        paint = new_paint;
    }
    //
    //  Getter functions
    //
    //  Get position
//    public void get_p(double new_px, double new_py) {
//        px = new_px;
//        py = new_py;
//    }
//
//    //  Get velocity
//    public void set_v(double new_vx, double new_vy) {
//        vx = new_vx;
//        vy = new_vy;
//    }
//
//    //  Get acceleration
//    public void set_a(double new_ax, double new_ay) {
//        ax = new_ax;
//        ay = new_ay;
//    }
}
