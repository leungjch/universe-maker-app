package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.helpers.Vector2D;

public class Asteroid extends CelestialBody {
    public final class SIZES {
        public static final double SMALL = 1;
        public static final double MEDIUM = 3;
        public static final double LARGE = 5;
    }

    public Asteroid() {
        Paint asteroidPaint = new Paint();
        asteroidPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

        Vector2D asteroidPos = new Vector2D(0,0);
        Vector2D asteroidVel = new Vector2D( 0,0);
        Vector2D asteroidAcc = new Vector2D(0,0);
        Vector2D asteroidFnet = new Vector2D(0,0);

        super.setRadius(Asteroid.SIZES.SMALL);
        super.setMass(0.1);

        super.setPos(asteroidPos);
        super.setVel(asteroidVel);
        super.setAcc(asteroidAcc);
        super.setFnet(asteroidFnet);
        super.setPaint(asteroidPaint);
    }
}
