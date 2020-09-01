package com.leungjch.orbitalapp.universe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.leungjch.orbitalapp.helpers.Vector2D;

import java.util.Random;

import static java.sql.DriverManager.println;
// Star is a subclass of a celestial body
public class Star extends CelestialBody {

    Random rand = new Random();

//  Constants for radii of stars
    public final class SIZES {
        public static final double SMALL = 10;
        public static final double MEDIUM = 50;
        public static final double LARGE = 100;
    }

    public Star() {

        Paint starPaint = new Paint();
        starPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

//        Vector2D starPos = new Vector2D(500.00,100.00);
        Vector2D starVel = new Vector2D(0,0);
        Vector2D starAcc = new Vector2D(0,0);
        Vector2D starFnet = new Vector2D(0,0);

        super.setRadius(SIZES.MEDIUM);
        super.setMass(10000000);

//        super.setPos(starPos);
        super.setVel(starVel);
        super.setAcc(starAcc);
        super.setFnet(starFnet);
        super.setPaint(starPaint);
    }

}
