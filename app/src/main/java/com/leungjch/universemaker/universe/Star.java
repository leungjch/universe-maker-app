package com.leungjch.universemaker.universe;

import android.graphics.Paint;

import com.leungjch.universemaker.GameView;
import com.leungjch.universemaker.helpers.MassRadiusTuple;
import com.leungjch.universemaker.helpers.Vector2D;

import java.util.Random;

// Star is a subclass of a celestial body
public class Star extends CelestialBody {

    Random rand = new Random();

//  Constants for radii of stars
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(1000, 25);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(3000,50);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(10000,100);
    }

    public Star(GameView.SIZE_TYPE size, Paint starPaint) {
        Vector2D starVel = new Vector2D(0,0);
        Vector2D starAcc = new Vector2D(0,0);
        Vector2D starFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(Star.SIZES.SMALL.mass);
                super.setRadius(Star.SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(Star.SIZES.MEDIUM.mass);
                super.setRadius(Star.SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(Star.SIZES.LARGE.mass);
                super.setRadius(Star.SIZES.LARGE.radius);
                break;
            case RANDOM:
                double randRadius = Star.SIZES.SMALL.radius + rand.nextDouble()*(Star.SIZES.LARGE.radius - Star.SIZES.SMALL.radius);
                super.setRadius(Star.SIZES.SMALL.radius + rand.nextDouble()*(Star.SIZES.LARGE.radius - Star.SIZES.SMALL.radius));
                // apply a standard density
                super.setMass(randRadius * Star.SIZES.LARGE.mass/ Star.SIZES.LARGE.radius);
                break;

        }

        super.setVel(starVel);
        super.setAcc(starAcc);
        super.setFnet(starFnet);
        super.setPaint(starPaint);
    }

}
