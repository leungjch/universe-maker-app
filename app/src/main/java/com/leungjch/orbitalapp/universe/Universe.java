package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.leungjch.orbitalapp.helpers.Vector2D;

import java.util.Random;

// The universe class contains all celestial objects defined
// It also performs integration of forces here
public class Universe {
    private Star star;
    private Planet[] planets;

    Random rand = new Random();

    public Universe() {
        star = new Star();
        star.setRadius(Star.SIZES.LARGE);
        star.setPos(new Vector2D(getScreenWidth()/2,getScreenHeight()/2));

//      Create planets
        int numPlanets = 10;
        planets = new Planet[numPlanets];
        for (int i = 0; i < numPlanets; i++) {
            planets[i] = new Planet();
            planets[i].setPos(new Vector2D(rand.nextInt(getScreenWidth()), rand.nextInt(getScreenHeight())));
        }
    }

    public void update() {
        star.update();
    }

    public void draw(Canvas canvas) {
        star.draw(canvas);
        for (int i = 0; i < planets.length; i++) {
            planets[i].draw(canvas);
        }
    }

    // Return screen width
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    // Return screen height
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
