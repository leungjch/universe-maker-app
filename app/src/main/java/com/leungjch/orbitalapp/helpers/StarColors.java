package com.leungjch.orbitalapp.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
public class StarColors {
   List<StarColorInfo> StarColorsList;
   int StarColorListSize;
   public class StarColorInfo {
       double bv;
       float temp;
       int r;
       int g;
       int b;

       public StarColorInfo(double mBv, float mTemp, int mR, int mG, int mB) {
           bv = mBv;
           temp = mTemp;
           r = mR;
           g = mG;
           b = mB;
       }

   }
   // Get closest RGB
   public StarColorInfo bv2rgb(double bv) {
        double mBv = bv;
        int index = (int)Math.floor((mBv+0.4f)/0.05);

        return StarColorsList.get(index);

   }

    public StarColors() {
        StarColorsList = Arrays.asList(
                new StarColorInfo (-0.4,113017,155,178,255),
                new StarColorInfo (-0.35,56701,158,181,255),
                new StarColorInfo (-0.3,33605,163,185,255),
                new StarColorInfo (-0.25,22695,170,191,255),
                new StarColorInfo (-0.2,16954,178,197,255),
                new StarColorInfo (-0.15,13674,187,204,255),
                new StarColorInfo (-0.1,11677,196,210,255),
                new StarColorInfo (-0.05,10395,204,216,255),
                new StarColorInfo (0,9531,211,221,255),
                new StarColorInfo (0.05,8917,218,226,255),
                new StarColorInfo (0.1,8455,223,229,255),
                new StarColorInfo (0.15,8084,228,233,255),
                new StarColorInfo (0.2,7767,233,236,255),
                new StarColorInfo (0.25,7483,238,239,255),
                new StarColorInfo (0.3,7218,243,242,255),
                new StarColorInfo (0.35,6967,248,246,255),
                new StarColorInfo (0.4,6728,254,249,255),
                new StarColorInfo (0.45,6500,255,249,251),
                new StarColorInfo (0.5,6285,255,247,245),
                new StarColorInfo (0.55,6082,255,245,239),
                new StarColorInfo (0.6,5895,255,243,234),
                new StarColorInfo (0.65,5722,255,241,229),
                new StarColorInfo (0.7,5563,255,239,224),
                new StarColorInfo (0.75,5418,255,237,219),
                new StarColorInfo (0.8,5286,255,235,214),
                new StarColorInfo (0.85,5164,255,233,210),
                new StarColorInfo (0.9,5052,255,232,206),
                new StarColorInfo (0.95,4948,255,230,202),
                new StarColorInfo (1,4849,255,229,198),
                new StarColorInfo (1.05,4755,255,227,195),
                new StarColorInfo (1.1,4664,255,226,191),
                new StarColorInfo (1.15,4576,255,224,187),
                new StarColorInfo (1.2,4489,255,223,184),
                new StarColorInfo (1.25,4405,255,221,180),
                new StarColorInfo (1.3,4322,255,219,176),
                new StarColorInfo (1.35,4241,255,218,173),
                new StarColorInfo (1.4,4159,255,216,169),
                new StarColorInfo (1.45,4076,255,214,165),
                new StarColorInfo (1.5,3989,255,213,161),
                new StarColorInfo (1.55,3892,255,210,156),
                new StarColorInfo (1.6,3779,255,208,150),
                new StarColorInfo (1.65,3640,255,204,143),
                new StarColorInfo (1.7,3463,255,200,133),
                new StarColorInfo (1.75,3234,255,193,120),
                new StarColorInfo (1.8,2942,255,183,101),
                new StarColorInfo (1.85,2579,255,169,75),
                new StarColorInfo (1.9,2150,255,149,35),
                new StarColorInfo (1.95,1675,255,123,0),
                new StarColorInfo (2,1195,255,82,0)
                );

        StarColorListSize = StarColorsList.size();
    }


}
