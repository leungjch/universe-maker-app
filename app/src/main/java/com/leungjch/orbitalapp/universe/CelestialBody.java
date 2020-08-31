package com.leungjch.orbitalapp.universe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;
import android.util.Log;


// Superclass describing all objects in the simulation


public class CelestialBody {
//  Mass determines the strength of its gravitational attraction
    private double mass;

//  Radius determines the body's size
    private double radius = 50;

//  Position
    private double px, py;

//  Velocity
    private double vx, vy;

//  Acceleration
    private double ax, ay;

//  Net Force
    private double fx, fy;

//  Color
    private final Paint paint;

    Random rand = new Random();


    //  Constructor
    public CelestialBody() {
        paint = new Paint();
        paint.setColor(Color.argb(255, 55,255,255));

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((int)px/2, (int)py/2, (int)radius, paint);
    }

    public void update() {
        Log.d("CelestialBody", Double.toString(radius));
        paint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        px += rand.nextInt(10) * (rand.nextInt(2) != 1 ? 1 : -1);
        py += rand.nextInt(10)  * (rand.nextInt(2) != 1 ? 1 : -1);
    }
    //
    //  Setter functions
    //
    //  Set position
    public void set_p(double new_px, double new_py) {
        px = new_px;
        py = new_py;
    }

    //  Set velocity
    public void set_v(double new_vx, double new_vy) {
        vx = new_vx;
        vy = new_vy;
    }

    //  Set acceleration
    public void set_a(double new_ax, double new_ay) {
        ax = new_ax;
        ay = new_ay;
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
