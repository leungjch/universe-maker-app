package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.Vector2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// The universe class contains all celestial objects defined
// It also performs integration of forces here
public class Universe {

    public final class CONSTANTS {
//      Actual gravitational constant is much smaller
        public static final double G = 100000000;

        // Maximum force allowed to be exerted
        // This solves problem of extreme acceleration when two objects are near each other
        public static final double MAXFORCE = 100000000;

        // The exponent to raise distance when calculating gravitational force
        // In real life, this is 2 (Gmm/r^2)
        // But modify it to adjust how "heavy" gravity feels
        public static final double EPSILON = 1.5;

//      Time step for integration
//      Smaller time step is more precise
        public static final double STEPS = 10000;
        public static final double dT = 1.0/STEPS;
    }
    private List<Star> stars;
    private List<Planet> planets;
    private List<CelestialBody> objects;
    private List<CelestialBody> objectsToAdd;
    private List<CelestialBody> objectsToRemove;

    Random rand = new Random();

    public Universe() {

//      Initialize everything
        stars = new ArrayList<Star>();
        planets = new ArrayList<Planet>();
        objects = new ArrayList<CelestialBody>();
        objectsToAdd = new ArrayList<CelestialBody>();
        objectsToRemove = new ArrayList<CelestialBody>();
//      Create stars
//        Star star = new Star(GameView.SIZE_TYPE.MEDIUM);
//        star.setPos(new Vector2D(getScreenWidth()/2,getScreenHeight()/2));
//        objects.add(star);

//      Create planets
        int numPlanets = 50;
        for (int i = 0; i < numPlanets; i++) {
            Planet tempPlanet = new Planet(GameView.SIZE_TYPE.MEDIUM);
            tempPlanet.setPos(new Vector2D(rand.nextInt(getScreenWidth()), rand.nextInt(getScreenHeight())));
            objects.add(tempPlanet);
        }
    }
//  Perform Euler integration
//  Calculate Fnet for each object
    public void update() {
        ListIterator<CelestialBody> iter1 = objects.listIterator();
        while (iter1.hasNext()) {
//        for (CelestialBody object1 : objects) {
            CelestialBody object1 = iter1.next();
            Vector2D Fnet = new Vector2D(0,0);
            Vector2D Acc = new Vector2D(0,0);
            Vector2D Vel = new Vector2D(0,0);
            Vector2D Pos = new Vector2D(0,0);

            ListIterator<CelestialBody> iter2 = objects.listIterator();
            while (iter2.hasNext()) {
                CelestialBody object2 = iter2.next();

//            for (CelestialBody object2 : objects) {
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
                // Asteroids exert negligible force, ignore them
                if (object2 instanceof Asteroid)
                {
                    continue;
                }
                // Check if collide
                if (object1.isCollide(object2)) {
//                    Fnet = new Vector2D(0,0);
//                    Acc = new Vector2D(0,0);
//                    Vel = new Vector2D(0,0);
//                    Log.d("Collision", "cloided");
                    // Biggest planet will absorb the smaller one
                    if (object2.getRadius() > object1.getRadius())
                    {
                        object2.setMass(object1.getMass()+object2.getMass());
                        object2.setRadius(object2.getRadius()+object1.getRadius()/object2.getRadius());

                        objectsToRemove.add(object1);
                    }
                    else
                    {
                        object1.setMass(object2.getMass()+object1.getMass());
                        object1.setRadius(object1.getRadius()+object2.getRadius()/object1.getRadius());
                        objectsToRemove.add(object2);

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
            if (Pos.getX() > getScreenWidth()*2 || Pos.getX() < -getScreenWidth()*2
            ||  Pos.getY() > getScreenHeight()*2|| Pos.getY() < -getScreenHeight()*2)
            {
                if (object1 instanceof Planet)
                {
//                    objects.remove(object1);
                    objectsToRemove.add(object1);
                }
            }
            else
            {
                object1.setPos(Pos);
            }
            object1.setPos(Pos);


        }

        // Remove planets queued for removal
        for (CelestialBody object : objectsToRemove) {
            objects.remove(object);
        }
        objectsToRemove.clear();
        // Add any new planets by the user
        for (CelestialBody object : objectsToAdd) {
            objects.add(object);
        }
        objectsToAdd.clear();


    }

    public void draw(Canvas canvas) {
        for (CelestialBody object : objects) {
            object.draw(canvas);
        }

    }

    // Add planet on touch
    public void addPlanet(Vector2D pos, GameView.PLACEMENT_TYPE placementType) {
        Planet tempPlanet = new Planet(GameView.SIZE_TYPE.MEDIUM);
        tempPlanet.setPos(pos);

        if (objects.size() < 1000)
        {
            switch (placementType) {
                case SCATTER:
                    tempPlanet.setVel(new Vector2D(rand.nextInt(100000) * (rand.nextInt(2) == 1? -1 : 1),
                            rand.nextInt(100000) * (rand.nextInt(2) == 1? -1 : 1)));
                case TARGET:

            }
//            planets.add(tempPlanet);
            objectsToAdd.add(tempPlanet);
        }

    }
    // Add star on touch
    public void addStar(Vector2D pos) {
        if (objects.size() < 1000)
        {
            Star tempStar = new Star(GameView.SIZE_TYPE.MEDIUM);
            tempStar.setPos(pos);
//            planets.add(tempPlanet);
            objectsToAdd.add(tempStar);
        }

    }

    public void addCelestialBody(Vector2D pos, Vector2D vel, int action, GameView.ADD_TYPE addType, GameView.SIZE_TYPE sizeType, GameView.PLACEMENT_TYPE placementType)
    {
        CelestialBody tempObject = new CelestialBody();
        switch (addType) {
            case ASTEROID:
                tempObject = new Asteroid(sizeType);
                break;
            case PLANET:
                tempObject = new Planet(sizeType);
                break;
            case STAR:
                tempObject = new Star(sizeType);
                break;
            case BLACK_HOLE:
                tempObject = new BlackHole(sizeType);
                break;
            case WHITE_HOLE:
                tempObject = new WhiteHole(sizeType);
                break;

        }

        if (objects.size() < 1000)
        {
            switch (placementType) {
                case SCATTER:
                    tempObject.setVel(new Vector2D(rand.nextInt(20000) * (rand.nextInt(2) == 1? -1 : 1),
                            rand.nextInt(20000) * (rand.nextInt(2) == 1? -1 : 1)));
                    tempObject.setPos(pos);
                    objectsToAdd.add(tempObject);

                    break;
                case TARGET:
                    if (action == MotionEvent.ACTION_UP)
                    {
                        tempObject.setVel(new Vector2D(vel.getX()*300, vel.getY()*300));
                        tempObject.setPos(pos);
                        objectsToAdd.add(tempObject);

                    }
                    break;
                case FLICK:
                    tempObject.setVel(new Vector2D(vel.getX()*20, vel.getY()*20));
                    tempObject.setPos(pos);
                    objectsToAdd.add(tempObject);

                    break;
            }
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
