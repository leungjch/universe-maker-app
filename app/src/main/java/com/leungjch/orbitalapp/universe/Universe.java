package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

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
        public static final double G = 100000;

        // The exponent to raise distance when calculating gravitational force
        // In real life, this is 2 (Gmm/r^2)
        // But modify it to adjust how "heavy" gravity feels
        public static final double EPSILON = 2;

//      Time step for integration
//      Smaller time step is more precise
        public static final double STEPS = 300;
        public static final double dT = 1.0/STEPS;
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
        star.setRadius(Star.SIZES.SMALL);
        star.setPos(new Vector2D(getScreenWidth()/2,getScreenHeight()/2));
        stars.add(star);
        objects.add(star);

//      Create planets
        int numPlanets = 100;
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
        for (CelestialBody object1 : objects) {
            Vector2D Fnet = new Vector2D(0,0);
            Vector2D Acc = new Vector2D(0,0);
            Vector2D Vel = new Vector2D(0,0);
            Vector2D Pos = new Vector2D(0,0);

            for (CelestialBody object2 : objects) {
                //  Skip if same object
                if (object1 == object2) {
                    continue;
                }
                // Skip if star
                if (object1 instanceof Star)
                {
                    Fnet.setX(0);
                    Fnet.setY(0);

                    continue;
                }
                // Check if collide
                if (object1.isCollide(object2)) {
                    Fnet = new Vector2D(0,0);
                    Acc = new Vector2D(0,0);
                    Vel = new Vector2D(0,0);
//                    Log.d("Collision", "cloided");

                    // Absorb
                    if (object2.getRadius() > object1.getRadius())
                    {
//                        object2.setRadius(object2.getRadius()+ object1.getRadius());
//                        object1.setRadius(0);

//                        object2.setMass(object1.getMass()+object2.getMass());
//                        object2.setRadius(object2.getRadius()+Math.sqrt(object1.getRadius()));

//                        objects.remove(object1);
                    }
                    else
                    {
//                        object1.setMass(object2.getMass()+object1.getMass());
//                        object1.setRadius(object1.getRadius()+Math.sqrt(object2.getRadius()));

//                        objects.remove(object2);

                    }
                    continue;
                }

                // Get gravitational attraction
                Vector2D grav = object1.calculateGrav(object2);

                // Increment current FNet
                Fnet.setX(Fnet.getX() + grav.getX());
                Fnet.setY(Fnet.getY() + grav.getY());
//                Log.d("TestGrav", Double.toString(Fnet.getX()));

            }

            // Integrate by time step
            // FNet = ma -> a = FNet / m

            Acc.setX(Fnet.getX()/object1.getMass());
            Acc.setY(Fnet.getY()/object1.getMass());

            // Obtain velocity by integrating acceleration
            Vel.setX(object1.getVel().getX() + (Acc.getX() * Universe.CONSTANTS.dT));
            Vel.setY(object1.getVel().getY() + (Acc.getY() * Universe.CONSTANTS.dT));

            // Obtain position by integrating  velocity
            Pos.setX(object1.getPos().getX() + (Vel.getX() * Universe.CONSTANTS.dT));
            Pos.setY(object1.getPos().getY() + (Vel.getY() * Universe.CONSTANTS.dT));

            // Update object
            object1.setFnet(Fnet);
            object1.setAcc(Acc);
            object1.setVel(Vel);
            // Check if past screen boundaries
//            if (Pos.getX() > getScreenWidth()*5 || Pos.getX() < -getScreenWidth()*5
//            ||  Pos.getY() > getScreenHeight()*5|| Pos.getY() < -getScreenHeight()*5)
//            {
//                objects.remove(object1);
//            }
//            else
//            {
//                object1.setPos(Pos);
//            }
            object1.setPos(Pos);


        }
    }

    public void draw(Canvas canvas) {
        for (CelestialBody object : objects) {
            object.draw(canvas);
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
