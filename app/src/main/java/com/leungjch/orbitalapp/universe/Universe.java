package com.leungjch.orbitalapp.universe;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.ColorGenerator;
import com.leungjch.orbitalapp.helpers.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// The universe class contains all celestial objects defined
// It also performs integration of forces here
public class Universe {

    public static final class CONSTANTS {

        // Boundaries of the universe
        // (-UNIVERSEWIDTH, UNIVERSEWIDTH) x (-UNIVERSEHEIGHT, UNIVERSEHEIGHT)
        // Any object past this boundary is deleted
        public static final int UNIVERSEWIDTH = getScreenWidth()*5;
        public static final int UNIVERSEHEIGHT = getScreenHeight()*5;

//      Actual gravitational constant is much smaller
        public static final double G = 2000000000;

        // Maximum force allowed to be exerted
        // This solves problem of extreme acceleration when two objects are near each other
        public static final double MAXFORCE = 1000000000;

        // The exponent to raise distance when calculating gravitational force
        // In real life, this is 2 (Gmm/r^2)
        // But modify it to adjust how "heavy" gravity feels
        public static final double EPSILON = 2;

//      Time step for integration
//      More steps = more precise
        public static final double STEPS_0 = 0;
        public static final double dT_0 = 0;


        public static final double STEPS_1 = 50000;
        public static final double dT_1 = 1.0/ STEPS_1;

        public static final double STEPS_2 = 10000;
        public static final double dT_2 = 1.0/ STEPS_2;

        public static final double STEPS_3 = 5000;
        public static final double dT_3 = 1.0/ STEPS_3;

    }
    private List<Star> stars;
    private List<Planet> planets;
    private List<CelestialBody> objects;
    private List<CelestialBody> objectsToAdd;
    private List<CelestialBody> objectsToRemove;

    // Create one color generator to use for all objects
    private ColorGenerator colorGenerator;

    private double currentSteps;
    private double currentDeltaT;

    public Boolean isPlayerMode = false;
    private Vector2D currentPlayerForce = new Vector2D(0,0);

    public static Random rand = new Random();

    public Universe(GameView.RESET_TYPE resetType) {
        // Set delta T
        currentDeltaT = CONSTANTS.dT_1;

        //  Initialize everything
        stars = new ArrayList<Star>();
        planets = new ArrayList<Planet>();
        objects = new ArrayList<CelestialBody>();
        objectsToAdd = new ArrayList<CelestialBody>();
        objectsToRemove = new ArrayList<CelestialBody>();

        // Create color generator
        colorGenerator = new ColorGenerator();
//      Create stars
        int numPlanets = 100;

        switch (resetType) {

            case BLANK:
                break;
            case SINGLE_STAR_SYSTEM:

                int numSinglePlanets = rand.nextInt(20)+1;
                int numSingleAsteroids =  rand.nextInt(100)+100;

                // Create fixed star at center of universe
                Star star = new Star(GameView.SIZE_TYPE.LARGE, ColorGenerator.generateColor(GameView.ADD_TYPE.STAR, GameView.SIZE_TYPE.LARGE));
//                Log.d("STARMASS", Double.toString(star.getMass()));
                star.isFixed = true;
                int radiusMultiplier = 10;
                star.setPos(new Vector2D(Universe.CONSTANTS.UNIVERSEWIDTH/2,Universe.CONSTANTS.UNIVERSEHEIGHT/2));
                objects.add(star);
                Vector2D posSingleObjects;

                for (int as = 0; as < numSingleAsteroids; as++) {
                    posSingleObjects = randomDistanceFromStar(star.getPos(), star.getRadius(), Math.random()*star.getRadius()*10);
                    addCelestialBody(new Vector2D(posSingleObjects.getX(), posSingleObjects.getY()), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.ASTEROID, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);
                }
                for (int i = 0; i < numSinglePlanets; i++) {
                    posSingleObjects = randomDistanceFromStar(star.getPos(), star.getRadius(), Math.random()*star.getRadius()*10);
                    int randObject = rand.nextInt(2);
                    if (randObject == 0)
                    {
                        addCelestialBody(new Vector2D(posSingleObjects.getX(), posSingleObjects.getY()), new Vector2D(0,0),
                                0, GameView.ADD_TYPE.SATELLITE, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);
                    }
                    else if (randObject == 1){
                        addCelestialBody(new Vector2D(posSingleObjects.getX(), posSingleObjects.getY()), new Vector2D(0,0),
                                0, GameView.ADD_TYPE.PLANET, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);
                    }

                }
                break;
            case BINARY_STAR_SYSTEM:
                int numBinaryPlanets = 20;
                Vector2D starPos1 = new Vector2D(Universe.CONSTANTS.UNIVERSEWIDTH/2 - Universe.CONSTANTS.UNIVERSEWIDTH/32, Universe.CONSTANTS.UNIVERSEHEIGHT/2 - Universe.CONSTANTS.UNIVERSEHEIGHT/32);
                Vector2D starPos2 = new Vector2D(Universe.CONSTANTS.UNIVERSEWIDTH/2 + Universe.CONSTANTS.UNIVERSEWIDTH/32, Universe.CONSTANTS.UNIVERSEHEIGHT/2 + Universe.CONSTANTS.UNIVERSEHEIGHT/32);
                addCelestialBody(starPos1, new Vector2D(0,0),
                            0, GameView.ADD_TYPE.STAR, GameView.SIZE_TYPE.LARGE, GameView.PLACEMENT_TYPE.FIXED);
                addCelestialBody(starPos2, new Vector2D(0,0),
                        0, GameView.ADD_TYPE.STAR, GameView.SIZE_TYPE.LARGE, GameView.PLACEMENT_TYPE.FIXED);


                Vector2D pos;
                Vector2D posBinaryAsteroid;

                int numBinaryAsteroids = 200;

                for (int as = 0; as < numBinaryAsteroids; as++) {
                    // Randomly select which positions to place the planets (relative to star 1 or 2)
                    if (rand.nextInt(2) == 1)
                    {
                        posBinaryAsteroid = randomDistanceFromStar(starPos1, Star.SIZES.LARGE.radius, Math.random()*Star.SIZES.LARGE.radius*10);
                    }
                    else
                    {
                        posBinaryAsteroid = randomDistanceFromStar(starPos2, Star.SIZES.LARGE.radius, Math.random()*Star.SIZES.LARGE.radius*10);

                    }

                    addCelestialBody(new Vector2D(posBinaryAsteroid.getX(), posBinaryAsteroid.getY()), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.ASTEROID, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);
                }

                // Add planets / satellites
                for (int i = 0; i < numBinaryPlanets; i++) {


                    // Randomly select which positions to place the planets (relative to star 1 or 2)
                    if (rand.nextInt(2) == 1)
                    {
                        pos = randomDistanceFromStar(starPos1, Star.SIZES.LARGE.radius, Math.random()*Star.SIZES.LARGE.radius*10);
                    }
                    else
                    {
                        pos = randomDistanceFromStar(starPos2, Star.SIZES.LARGE.radius, Math.random()*Star.SIZES.LARGE.radius*10);

                    }
                    if (rand.nextInt(2) == 1)
                    {
                        addCelestialBody(new Vector2D(pos.getX(), pos.getY()), new Vector2D(0,0),
                                0, GameView.ADD_TYPE.PLANET, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);

                    }
                    else {
                        addCelestialBody(new Vector2D(pos.getX(), pos.getY()), new Vector2D(0,0),
                                0, GameView.ADD_TYPE.SATELLITE, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);

                    }
                }
                break;
            case PLANETS_COALESCING:
                //  Create planets
                for (int j = 0; j < numPlanets; j++) {
                    addCelestialBody(new Vector2D(rand.nextInt(CONSTANTS.UNIVERSEWIDTH), rand.nextInt(CONSTANTS.UNIVERSEHEIGHT)), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.PLANET, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.SCATTER);

                }
                break;
            case CIRCLING_SATELLITES:
                //  Create satellites
                int numSatellites = 100;
                int numAsteroidsinSatellites = 20;
                for (int sa = 0; sa < numSatellites; sa++) {
                    addCelestialBody(new Vector2D(rand.nextInt(CONSTANTS.UNIVERSEWIDTH), rand.nextInt(CONSTANTS.UNIVERSEHEIGHT)), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.SATELLITE, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.SCATTER);
                }
                for (int sast = 0; sast < numAsteroidsinSatellites; sast++) {
                    addCelestialBody(new Vector2D(rand.nextInt(CONSTANTS.UNIVERSEWIDTH), rand.nextInt(CONSTANTS.UNIVERSEHEIGHT)), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.ASTEROID, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.SCATTER);
                }
                break;
            case BLACK_HOLE_ACCCRETION_DISK:
                int numAsteroids = 300;

                // Create fixed star at center of universe
                BlackHole bhole = new BlackHole(GameView.SIZE_TYPE.LARGE, ColorGenerator.generateColor(GameView.ADD_TYPE.BLACK_HOLE, GameView.SIZE_TYPE.LARGE));
                bhole.isFixed = true;
                bhole.setPos(new Vector2D(Universe.CONSTANTS.UNIVERSEWIDTH/2,Universe.CONSTANTS.UNIVERSEHEIGHT/2));
                objects.add(bhole);

                for (int i = 0; i < numAsteroids; i++) {
                    Vector2D posAsteroidsBhole = randomDistanceFromStar(bhole.getPos(), bhole.getRadius(), Math.random()*bhole.getRadius()*25);
                    addCelestialBody(new Vector2D(posAsteroidsBhole.getX(), posAsteroidsBhole.getY()), new Vector2D(0,0),
                            0, GameView.ADD_TYPE.ASTEROID, GameView.SIZE_TYPE.RANDOM, GameView.PLACEMENT_TYPE.ORBIT);
                }
                break;
        }

    }

    //  Perform Euler integration
    //  Calculate Fnet for each object
    public void update() {
        for (CelestialBody object1: objects) {
            Vector2D Fnet = new Vector2D(0,0);
            Vector2D Acc = new Vector2D(0,0);
            Vector2D Vel = new Vector2D(0,0);
            Vector2D Pos = new Vector2D(0,0);

            CelestialBody maxForceObject = object1; // for searching for object exerting most force on object1, only for orbit mode
            double maxForce = 0;

            for (CelestialBody object2: objects) {

//            for (CelestialBody object2 : objects) {
                //  Skip if same object
                if (object1 == object2) {
                    continue;
                }
                // If fixed, set f = 0
                if (object1.isFixed)
                {
                    Fnet.setX(0);
                    Fnet.setY(0);
                    object1.setVel(new Vector2D(0,0));
                    continue;
                }
                // Asteroids exert negligible force, ignore them
                if (object2 instanceof Asteroid)
                {
                    continue;
                }


                // Check if collide
                if (object1.isCollide(object2) && !objectsToRemove.contains(object1)) {
                    handleCollide(object1, object2);
                    continue;
                }

                // Get gravitational attraction
                Vector2D grav = object1.calculateGrav(object2);

                // If current object is set to orbit mode, keep track of the most massive object
                if (object1.isOrbit && grav.magnitude() > maxForce) {
                    maxForceObject = object2;
                    maxForce = grav.magnitude();
                }
                // Increment current FNet
                Fnet.setX(Fnet.getX() + grav.getX());
                Fnet.setY(Fnet.getY() + grav.getY());
//                Log.d("TestGrav", Double.toString(Fnet.getX()));

            }

            // If in player mode, add player force
            if (object1 instanceof PlayerShip) {
                Fnet.setX(Fnet.getX() + currentPlayerForce.getX());
                Fnet.setY(Fnet.getY() + currentPlayerForce.getY());
//                Log.d("PLAYERFORCE", Double.toString(currentPlayerForce.getX()));
            }

            // Integrate by time step
            // FNet = ma -> a = FNet / m

            Acc.setX(Fnet.getX()/object1.getMass());
            Acc.setY(Fnet.getY()/object1.getMass());

            // Obtain velocity by integrating acceleration
            Vel.setX(object1.getVel().getX() + (Acc.getX() * currentDeltaT));
            Vel.setY(object1.getVel().getY() + (Acc.getY() * currentDeltaT));

            // Obtain position by integrating  velocity
            Pos.setX(object1.getPos().getX() + (Vel.getX() * currentDeltaT));
            Pos.setY(object1.getPos().getY() + (Vel.getY() * currentDeltaT));


            // Update object
            object1.setFnet(Fnet);
            object1.setAcc(Acc);
            object1.setVel(Vel);

            // If object set to orbit
            // Orbital velocity
            // Set the initial velocity at an angle perpendicular to the angle of the force vector
            if (object1.isOrbit) {
                double theta_f = Math.atan2(object1.getFnet().getY(), object1.getFnet().getX());

                double vAbs = Math.sqrt((CONSTANTS.G*maxForceObject.getMass()) / Math.pow(object1.getPos().distance(maxForceObject.getPos()),1));
//                Log.d("DIST", Double.toString((object1.getPos().distance(maxForceObject.getPos()))));
                object1.setFnet(new Vector2D(0, 0));
                object1.setAcc(new Vector2D(0, 0));
                object1.setVel(new Vector2D(vAbs*Math.cos(theta_f + Math.PI/2), vAbs*Math.sin(theta_f + Math.PI/2)));
                object1.isOrbit = false;
            }

            // Check if outside screen boundaries before placing
            // If outside boundaries, remove the object
            // Else, place it normally
            if (Pos.getX() > Universe.CONSTANTS.UNIVERSEWIDTH + Universe.CONSTANTS.UNIVERSEWIDTH/4 || Pos.getX() < -Universe.CONSTANTS.UNIVERSEWIDTH/4
            ||  Pos.getY() > Universe.CONSTANTS.UNIVERSEHEIGHT + Universe.CONSTANTS.UNIVERSEHEIGHT/4|| Pos.getY() < -Universe.CONSTANTS.UNIVERSEHEIGHT/4)
            {
                objectsToRemove.add(object1);
            }
            else
            {
                object1.setPos(Pos);
            }

            // Special objects - Drones and player
            if (object1 instanceof DroneAI) {
                object1.control();
            }


        }

        // Remove planets queued for removal
        for (CelestialBody object : objectsToRemove) {
            if (isPlayerMode && object instanceof PlayerShip) {
                setPlayerMode(false);
            }
            objects.remove(object);
        }
        objectsToRemove.clear();
        // Add any new planets by the user
        for (CelestialBody object : objectsToAdd) {
            objects.add(object);
        }
        objectsToAdd.clear();


    }

    public void draw(Canvas canvas, Boolean isTraceMode) {
        for (CelestialBody object : objects) {
            object.draw(canvas, isTraceMode);

        }

    }

    // Add planet on touch
    public void addPlanet(Vector2D pos, GameView.PLACEMENT_TYPE placementType) {
        Planet tempPlanet = new Planet(GameView.SIZE_TYPE.MEDIUM, colorGenerator.generateColor(GameView.ADD_TYPE.PLANET, GameView.SIZE_TYPE.MEDIUM));
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
            Star tempStar = new Star(GameView.SIZE_TYPE.MEDIUM, colorGenerator.generateColor(GameView.ADD_TYPE.STAR, GameView.SIZE_TYPE.MEDIUM));
            tempStar.setPos(pos);
//            planets.add(tempPlanet);
            objectsToAdd.add(tempStar);
        }

    }

    public void addCelestialBody(Vector2D pos, Vector2D vel, int action, GameView.ADD_TYPE addType, GameView.SIZE_TYPE sizeType, GameView.PLACEMENT_TYPE placementType)
    {
        CelestialBody tempObject = new CelestialBody();
        Paint tempPaint = colorGenerator.generateColor(addType, sizeType);

        switch (addType) {
            case ASTEROID:
                tempObject = new Asteroid(sizeType, tempPaint);
                break;
            case PLANET:
                tempObject = new Planet(sizeType, tempPaint);
                break;
            case STAR:
                tempObject = new Star(sizeType, tempPaint);
                break;
            case BLACK_HOLE:
                tempObject = new BlackHole(sizeType, tempPaint);
                break;
            case WHITE_HOLE:
                tempObject = new WhiteHole(sizeType, tempPaint);
                break;
            case SATELLITE:
                tempObject = new DroneAI(sizeType, tempPaint);
                break;
            case PLAYER_SHIP:
                if (!isPlayerMode)
                {
                    tempObject = new PlayerShip(sizeType, tempPaint);
                    setPlayerMode(true);
                }
                // Don't create more than one ship if already in player mode
                else {
                    return;
                }
                break;
        }

        if (objects.size() < 1000)
        {
            switch (placementType) {
                case SCATTER:
                    tempObject.setVel(new Vector2D(rand.nextInt(50000) * (rand.nextInt(2) == 1? -1 : 1),
                            rand.nextInt(50000) * (rand.nextInt(2) == 1? -1 : 1)));
                    tempObject.setPos(pos);
                    objectsToAdd.add(tempObject);

                    break;
                case FLICK:
                    tempObject.setVel(new Vector2D(vel.getX()*20, vel.getY()*20));
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
                case IDLE:
                    tempObject.setVel(new Vector2D(0,0));
                    tempObject.setPos(pos);
                    objectsToAdd.add(tempObject);
                    break;
                case FIXED:
                    tempObject.setVel(new Vector2D(0,0));
                    tempObject.setPos(pos);
                    tempObject.isFixed = true;
                    objectsToAdd.add(tempObject);
                    break;
                case ORBIT:
                    tempObject.setVel(new Vector2D(0,0));
                    tempObject.setPos(pos);
                    tempObject.isOrbit = true;
                    objectsToAdd.add(tempObject);
                    break;

            }
        }
    }
    public void handleCollide(CelestialBody object1, CelestialBody object2) {

        double vAbs1 = object1.getVel().magnitude();
        double vAbs2 = object2.getVel().magnitude();

        double distX = object1.getPos().getX() - object2.getPos().getX();
        double distY = object1.getPos().getY() - object2.getPos().getY();

        double phi = Math.PI - Math.atan2(distY, distX);
        double theta1 = Math.atan2(object1.getVel().getY(), object1.getVel().getX());
        double theta2 = Math.atan2(object2.getVel().getY(), object2.getVel().getX());

        // Biggest planet will absorb the smaller one

        // https://en.wikipedia.org/wiki/Inelastic_collision
        double vx = (object1.getMass() * object1.getVel().getX() + (object2.getMass() * object2.getVel().getX()))/(object1.getMass()+object2.getMass());
        double vy = (object1.getMass() * object1.getVel().getY() + (object2.getMass() * object2.getVel().getY()))/(object1.getMass()+object2.getMass());

        if (object2.getMass() >= object1.getMass())
        {

            // If the object is a player ship and the ship landed slowly, keep it
//            if (object1 instanceof PlayerShip) {
//                object1.setPos(object1.getPos());
//                object1.setVel(object1.getVel());
//                object1.setAcc(new Vector2D(0,0));
//                object1.setFnet(new Vector2D(0,0));
//                object1.setPos(object1.getPos());
//
//            }
//            else {
                object2.setMass(object1.getMass()+object2.getMass());
                object2.setRadius(object2.getRadius()+object1.getRadius()/object2.getRadius());
                object2.setVel(new Vector2D(vx, vy));

                objectsToRemove.add(object1);
//            }
        }
        else
        {
            // If the object is a player ship and the ship landed slowly, keep it
//            if (object2 instanceof PlayerShip) {
//                object2.setFnet(new Vector2D(0,0));
//                object2.setPos(object2.getPos());
//                object2.setVel(object2.getVel());
//                object2.setAcc(new Vector2D(0,0));
//                object2.setPos(object1.getPos());
//
//            }
//            else {
                object1.setMass(object2.getMass()+object1.getMass());
                object1.setRadius(object1.getRadius()+object2.getRadius()/object1.getRadius());
                object1.setVel(new Vector2D(vx, vy));

                objectsToRemove.add(object2);
//            }
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

    // Set current delta time step
    public void setCurrentDeltaT(double newDt) {
        currentDeltaT = newDt;
    }

    // Set play mode
    public void setPlayerMode(Boolean mode) {
        isPlayerMode = mode;
    }
    // Set player force
    public void setPlayerControls(Vector2D newForce){ currentPlayerForce = newForce;}

    public static int randRange(int min, int max) {

        return rand.nextInt((max - min) + 1) + min;

    }

    public static Vector2D randomDistanceFromStar(Vector2D starPos, double starRadius, double dist) {

        double randAngle = Math.random()*Math.PI*2;
        Vector2D randPos = new Vector2D((int)(starPos.getX()+(starRadius*2+dist)*Math.cos(randAngle)),(int)(starPos.getY()+(starRadius*2+dist)*Math.sin(randAngle)));
        return randPos;

    }

}
