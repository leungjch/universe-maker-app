package com.leungjch.orbitalapp.helpers;

// Creates a 2D coordinate system that is used by the universe
// 10000x10000 double precision grid
public class CoordinateSpace2D {
    public double UNIVERSEWIDTH = 10000;
    public double UNIVERSEHEIGHT = 10000;

    // The absolute coordinates of the top left corner of the device view
    private Vector2D cameraPosAbs = new Vector2D(UNIVERSEWIDTH/2, UNIVERSEHEIGHT/2);

    public CoordinateSpace2D() {

    }



}
