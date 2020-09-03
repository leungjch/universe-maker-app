package com.leungjch.orbitalapp.helpers;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.universe.CelestialBody;
import com.leungjch.orbitalapp.universe.Asteroid;
import com.leungjch.orbitalapp.universe.Planet;
import com.leungjch.orbitalapp.universe.Star;

import java.util.Random;

public class ColorGenerator {

    public ColorGenerator () {

    }

    // Create a unique paint given a celestial body type
    public static Paint generateColor(GameView.ADD_TYPE celestialBodyType) {
        Random rand = new Random();

        Paint paint = new Paint();
        switch (celestialBodyType) {
            case ASTEROID:
                int gray = rand.nextInt(100)+100;
                paint.setColor(Color.rgb(gray,gray,gray));
                break;
            case STAR:

        }


        return paint;
    }

}
