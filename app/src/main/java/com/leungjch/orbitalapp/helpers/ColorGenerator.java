package com.leungjch.orbitalapp.helpers;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.universe.CelestialBody;
import com.leungjch.orbitalapp.universe.Asteroid;
import com.leungjch.orbitalapp.universe.Planet;
import com.leungjch.orbitalapp.universe.Star;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;
import java.util.Random;

public class ColorGenerator {

    static StarColors starColors;
    public ColorGenerator () {
        starColors = new StarColors();
    }

    // Create a unique paint given a celestial body type
    public static Paint generateColor(GameView.ADD_TYPE celestialBodyType, GameView.SIZE_TYPE sizeType) {
        Random rand = new Random();

        Paint paint = new Paint();
        switch (celestialBodyType) {
            case ASTEROID:
                int grayA = rand.nextInt(100)+100;
                paint.setColor(Color.rgb(grayA,grayA,grayA));
                break;
            case PLANET:
                paint.setColor(Color.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
                break;
            case STAR:
                StarColors.StarColorInfo starcol = starColors.bv2rgb(rand.nextDouble()*2.4f - 0.4f);
                Log.d("COLS", Integer.toString(starcol.r));
                paint.setColor(Color.rgb(starcol.r, starcol.g, starcol.b));
                break;
            case BLACK_HOLE:
                paint.setColor(Color.rgb(0,0,0));

                break;
            case WHITE_HOLE:
                paint.setColor(Color.rgb(255,255,255));
        }


        return paint;
    }
    // "Pastel-izes" a colour by mixing more white into it
    // passes specifies how many times to mix white (default 1), higher passes = more white
    public static Paint generatePastel(Paint oldPaint, int passes) {
        Paint pastel = new Paint();
        return pastel;
    }

}
