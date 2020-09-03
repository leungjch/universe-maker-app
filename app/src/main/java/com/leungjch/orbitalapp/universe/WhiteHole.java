package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.MassRadiusTuple;
import com.leungjch.orbitalapp.helpers.Vector2D;

public class WhiteHole extends CelestialBody {
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(-10000000,5);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(-1000000,10);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(-2000000,20);
    }

    public WhiteHole(GameView.SIZE_TYPE size) {
        Paint whiteHolePaint = new Paint();
        whiteHolePaint.setStyle(Paint.Style.FILL);
        whiteHolePaint.setColor(Color.argb(255, 0,0,0));

//         White Stroke
        whiteHolePaint.setStyle(Paint.Style.STROKE);
        whiteHolePaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        whiteHolePaint.setStrokeWidth(1.0f);

        Vector2D whiteHolePos = new Vector2D(0,0);
        Vector2D whiteHoleVel = new Vector2D( 0,0);
        Vector2D whiteHoleAcc = new Vector2D(0,0);
        Vector2D whiteHoleFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(WhiteHole.SIZES.SMALL.mass);
                super.setRadius(WhiteHole.SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(WhiteHole.SIZES.MEDIUM.mass);
                super.setRadius(WhiteHole.SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(WhiteHole.SIZES.LARGE.mass);
                super.setRadius(WhiteHole.SIZES.LARGE.radius);
                break;
            case RANDOM:
                double randRadius = WhiteHole.SIZES.SMALL.radius + rand.nextDouble()*(WhiteHole.SIZES.LARGE.radius - WhiteHole.SIZES.SMALL.radius);
                super.setRadius(WhiteHole.SIZES.SMALL.radius + rand.nextDouble()*(WhiteHole.SIZES.LARGE.radius - WhiteHole.SIZES.SMALL.radius));
                // apply a standard density
                super.setMass(randRadius * WhiteHole.SIZES.LARGE.mass/ WhiteHole.SIZES.LARGE.radius);
                break;
        }
        
        super.setPos(whiteHolePos);
        super.setVel(whiteHoleVel);
        super.setAcc(whiteHoleAcc);
        super.setFnet(whiteHoleFnet);
        super.setPaint(whiteHolePaint);
    }
}
