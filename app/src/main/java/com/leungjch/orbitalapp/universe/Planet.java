package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.MassRadiusTuple;
import com.leungjch.orbitalapp.helpers.Vector2D;

public class Planet extends CelestialBody {
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(0.01,1);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(0.1,3);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(1,5);
    }

    public Planet(GameView.SIZE_TYPE size) {
        Paint planetPaint = new Paint();
        planetPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

        Vector2D planetPos = new Vector2D(0,0);
        Vector2D planetVel = new Vector2D( 0,0);
        Vector2D planetAcc = new Vector2D(0,0);
        Vector2D planetFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(Planet.SIZES.SMALL.mass);
                super.setRadius(Planet.SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(Planet.SIZES.MEDIUM.mass);
                super.setRadius(Planet.SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(Planet.SIZES.LARGE.mass);
                super.setRadius(Planet.SIZES.LARGE.radius);
                break;
        }
        
        super.setPos(planetPos);
        super.setVel(planetVel);
        super.setAcc(planetAcc);
        super.setFnet(planetFnet);
        super.setPaint(planetPaint);
    }
}
