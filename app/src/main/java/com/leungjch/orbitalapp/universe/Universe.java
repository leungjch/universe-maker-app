package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.leungjch.orbitalapp.helpers.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The universe class contains all celestial objects defined
// It also performs integration of forces here
public class Universe {

    public final class CONSTANTS {
//      Actual gravitational constant is much smaller
        public static final double G = 0.01;

//      Time step for integration
//      Smaller time step is more precise
        public static final double dT = 0.01;
    }
    private List<Star> stars;
    private List<Planet> planets;
    private List<CelestialBody> objects;
    Random rand = new Random();

    public Universe() {

//      Initialize everything
        stars = new ArrayList<Star>();
        planets = new ArrayList<Planet>();
        objects = new ArrayList<CelestialBody>();

//      Create stars
        Star star = new Star();
        star.setRadius(Star.SIZES.LARGE);
        star.setPos(new Vector2D(getScreenWidth()/2,getScreenHeight()/2));
        stars.add(star);
        objects.add(star);

//      Create planets
        int numPlanets = 10;
        for (int i = 0; i < numPlanets; i++) {
            Planet tempPlanet = new Planet();
            tempPlanet.setPos(new Vector2D(rand.nextInt(getScreenWidth()), rand.nextInt(getScreenHeight())));
            planets.add(tempPlanet);
            objects.add(tempPlanet);
        }
    }
//  Perform Euler integration
//  Calculate Fnet for each object
    public void update() {

    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).draw(canvas);
        }

        for (int i = 0; i < planets.size(); i++) {
            planets.get(i).draw(canvas);
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
