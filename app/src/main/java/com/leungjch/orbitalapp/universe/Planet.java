package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.helpers.Vector2D;

public class Planet extends CelestialBody {
    public final class SIZES {
        public static final double SMALL = 10;
        public static final double MEDIUM = 50;
        public static final double LARGE = 100;
    }

    public Planet() {
        Paint planetPaint = new Paint();
        planetPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

        Vector2D planetPos = new Vector2D(0,0);
        Vector2D planetVel = new Vector2D(0,0);
        Vector2D planetAcc = new Vector2D(0,0);
        Vector2D planetFnet = new Vector2D(0,0);

        super.setRadius(Planet.SIZES.SMALL);
        super.setMass(50);

        super.setPos(planetPos);
        super.setVel(planetVel);
        super.setAcc(planetAcc);
        super.setFnet(planetFnet);
        super.setPaint(planetPaint);
    }
}
