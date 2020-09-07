package com.leungjch.universemaker.helpers;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.universemaker.GameView;

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
                Color planetCol = Color.valueOf(Color.rgb(rand.nextInt(255),rand.nextInt(255), rand.nextInt(255) ));//
                Color atmosphereCol = Color.valueOf(Color.rgb(255-planetCol.red(),
                        255-planetCol.green(),
                        255-planetCol.blue()));
                paint.setColor(planetCol.toArgb());
                paint.setShadowLayer(8,0,0, atmosphereCol.toArgb());

                break;
            case STAR:
                StarColors.StarColorInfo starcol = starColors.bv2rgb(rand.nextDouble()*2.4f - 0.4f);
                paint.setColor(Color.rgb(starcol.r, starcol.g, starcol.b));
                paint.setShadowLayer(50,0,0, Color.rgb(starcol.r, starcol.g, starcol.b));

                break;
            case BLACK_HOLE:
                paint.setColor(Color.rgb(0,0,0));
                paint.setShadowLayer(15,0,0, Color.WHITE);
                break;
            case WHITE_HOLE:
                paint.setColor(Color.rgb(255,255,255));
                paint.setShadowLayer(50,0,0, Color.WHITE);

                break;
            case SATELLITE:
                int grayD = rand.nextInt(100)+100;
                paint.setColor(Color.rgb(grayD,grayD,grayD));
                paint.setShadowLayer(10,0,0, Color.GRAY);

                break;
            case PLAYER_SHIP:
                paint.setColor(Color.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
                paint.setShadowLayer(50,0,0, Color.WHITE);

                break;
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
