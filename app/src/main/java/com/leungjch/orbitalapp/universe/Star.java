package com.leungjch.orbitalapp.universe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import static java.sql.DriverManager.println;


public class Star extends CelestialBody {

    Random rand = new Random();

//    private int radius = 100;
//    private int px = 100;
//    private int py = 100;



    public Star() {
//        paint = new Paint();
//        paint.setColor(Color.argb(255, 55,255,255));
        super.set_p(500,100);
    }
//
//    public void draw(Canvas canvas) {
//        canvas.drawCircle(px, py, radius, paint);
//    }
//
//    public void update() {
//        paint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
//        x += rand.nextInt(10) * (rand.nextInt(2) != 1 ? 1 : -1);
//        y += rand.nextInt(10)  * (rand.nextInt(2) != 1 ? 1 : -1);
//    }

}
