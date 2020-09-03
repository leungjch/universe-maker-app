package com.leungjch.orbitalapp.helpers;

import android.graphics.Color;
import android.graphics.Paint;

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

    public ColorGenerator () {
    }

    // Create a unique paint given a celestial body type
    public static Paint generateColor(GameView.ADD_TYPE celestialBodyType, CelestialBody celestialBody) {
        Random rand = new Random();

        Paint paint = new Paint();
        switch (celestialBodyType) {
            case ASTEROID:
                int gray = rand.nextInt(100)+100;
                paint.setColor(Color.rgb(gray,gray,gray));
                break;
            case PLANET:

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
