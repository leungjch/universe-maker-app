package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.leungjch.orbitalapp.helpers.Vector2D;

// The universe class contains all celestial objects defined
// It also performs integration of forces here
public class Universe {
    private Star star;
    private Planet[] planets;
    public Universe() {
        star = new Star();
        star.setRadius(Star.SIZES.LARGE);
        star.setPos(new Vector2D(getScreenWidth()/2,getScreenHeight()/2));

//      Create planets
        int numPlanets = 10;
        planets = new Planet[numPlanets];
        for (int i = 0; i < numPlanets; i++)
        {
            planets[i] = new Planet();
        }
    }

    public void update() {
        star.update();
    }

    public void draw(Canvas canvas) {
        star.draw(canvas);
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
