package com.leungjch.orbitalapp.universe;

import android.graphics.Color;
import android.graphics.Paint;

import com.leungjch.orbitalapp.GameView;
import com.leungjch.orbitalapp.helpers.MassRadiusTuple;
import com.leungjch.orbitalapp.helpers.Vector2D;

public class BlackHole extends CelestialBody {
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(1000,5);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(10000,10);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(20000,20);
    }



    public BlackHole(GameView.SIZE_TYPE size) {
        Paint blackHolePaint = new Paint();
        blackHolePaint.setColor(Color.argb(255, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

        Vector2D blackHolePos = new Vector2D(0,0);
        Vector2D blackHoleVel = new Vector2D( 0,0);
        Vector2D blackHoleAcc = new Vector2D(0,0);
        Vector2D blackHoleFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(BlackHole.SIZES.SMALL.mass);
                super.setRadius(BlackHole.SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(BlackHole.SIZES.MEDIUM.mass);
                super.setRadius(BlackHole.SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(BlackHole.SIZES.LARGE.mass);
                super.setRadius(BlackHole.SIZES.LARGE.radius);
                break;
            case RANDOM:
                double randRadius = BlackHole.SIZES.SMALL.radius + rand.nextDouble()*(BlackHole.SIZES.LARGE.radius - BlackHole.SIZES.SMALL.radius);
                super.setRadius(BlackHole.SIZES.SMALL.radius + rand.nextDouble()*(BlackHole.SIZES.LARGE.radius - BlackHole.SIZES.SMALL.radius));
                // apply a standard density
                super.setMass(randRadius * BlackHole.SIZES.LARGE.mass/ BlackHole.SIZES.LARGE.radius);
                break;
        }
        
        super.setPos(blackHolePos);
        super.setVel(blackHoleVel);
        super.setAcc(blackHoleAcc);
        super.setFnet(blackHoleFnet);
        super.setPaint(blackHolePaint);
    }
}
