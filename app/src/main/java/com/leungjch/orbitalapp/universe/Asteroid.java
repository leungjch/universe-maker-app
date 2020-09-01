package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.MassRadiusTuple;
import com.leungjch.orbitalapp.helpers.Vector2D;

public class Asteroid extends CelestialBody {
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(0.01,1);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(0.1,3);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(1,5);
    }

    public Asteroid(GameView.SIZE_TYPE size) {
        Paint asteroidPaint = new Paint();
        asteroidPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

        Vector2D asteroidPos = new Vector2D(0,0);
        Vector2D asteroidVel = new Vector2D( 0,0);
        Vector2D asteroidAcc = new Vector2D(0,0);
        Vector2D asteroidFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(SIZES.SMALL.mass);
                super.setRadius(SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(SIZES.MEDIUM.mass);
                super.setRadius(SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(SIZES.LARGE.mass);
                super.setRadius(SIZES.LARGE.radius);
                break;
        }

        super.setPos(asteroidPos);
        super.setVel(asteroidVel);
        super.setAcc(asteroidAcc);
        super.setFnet(asteroidFnet);
        super.setPaint(asteroidPaint);
    }
}
