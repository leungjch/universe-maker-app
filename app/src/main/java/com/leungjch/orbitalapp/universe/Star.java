package com.leungjch.orbitalapp.universe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.leungjch.orbitalapp.helpers.Vector2D;

import java.util.Random;

import static java.sql.DriverManager.println;

public class Star extends CelestialBody {

    Random rand = new Random();


    public Star() {
        Paint starPaint = new Paint();
        starPaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        double starRadius = 50;
        Vector2D starPos = new Vector2D(500.00,100.00);
        Vector2D starVel = new Vector2D(0,0);
        Vector2D starAcc = new Vector2D(0,0);
        Vector2D starFnet = new Vector2D(0,0);

        super.set_radius(50);
        super.set_pos(starPos);
        super.set_vel(starVel);
        super.set_acc(starAcc);
        super.set_fnet(starFnet);
        super.set_paint(starPaint);
    }

}
