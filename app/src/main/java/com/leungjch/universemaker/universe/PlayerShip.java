package com.leungjch.universemaker.universe;

import android.graphics.Paint;

import com.leungjch.universemaker.GameView;
import com.leungjch.universemaker.helpers.MassRadiusTuple;
import com.leungjch.universemaker.helpers.Vector2D;

public class PlayerShip extends CelestialBody {
    public static final class SIZES {
        public static final MassRadiusTuple SMALL = new MassRadiusTuple(0.000001,5);
        public static final MassRadiusTuple MEDIUM = new MassRadiusTuple(0.00001,10);
        public static final MassRadiusTuple LARGE = new MassRadiusTuple(0.00001,15);
    }

    public PlayerShip(GameView.SIZE_TYPE size, Paint playerPaint) {

        Vector2D playerPos = new Vector2D(0,0);
        Vector2D playerVel = new Vector2D( 0,0);
        Vector2D playerAcc = new Vector2D(0,0);
        Vector2D playerFnet = new Vector2D(0,0);

        switch (size) {
            case SMALL:
                super.setMass(SIZES.SMALL.mass);
                super.setRadius(SIZES.SMALL.radius);
                break;
            case MEDIUM:
                super.setMass(SIZES.MEDIUM.mass);
                super.setRadius(SIZES.MEDIUM.radius);
                break;
            case LARGE:
                super.setMass(SIZES.LARGE.mass);
                super.setRadius(SIZES.LARGE.radius);
                break;
            case RANDOM:
                double randRadius = SIZES.SMALL.radius + rand.nextDouble()*(SIZES.LARGE.radius - SIZES.SMALL.radius);
                super.setRadius(SIZES.SMALL.radius + rand.nextDouble()*(SIZES.LARGE.radius - SIZES.SMALL.radius));
                // apply a standard density
                super.setMass(randRadius * SIZES.LARGE.mass/ SIZES.LARGE.radius);
                break;
        }
        super.setPos(playerPos);
        super.setVel(playerVel);
        super.setAcc(playerAcc);
        super.setFnet(playerFnet);
        super.setPaint(playerPaint);
    }

    @Override
    public void control() {
        // Calculate angle of force vector
        double theta_v = Math.atan2(super.getVel().getY(), super.getVel().getX());
        double theta_f = Math.atan2(super.getFnet().getY(), super.getFnet().getX());

        double vAbs = super.getVel().magnitude();
        Vector2D f = super.getFnet();
        Vector2D a = super.getAcc();
        Vector2D v = super.getVel();

        super.setFnet(new Vector2D(-f.getX(), -f.getY()));
        super.setAcc(new Vector2D(-a.getX(), -a.getY()));
        // Set velocity 90 deg away from force vector
        super.setVel(new Vector2D(vAbs*Math.cos(theta_f + Math.PI/2), vAbs*Math.sin(theta_f + Math.PI/2)));

    }
}
