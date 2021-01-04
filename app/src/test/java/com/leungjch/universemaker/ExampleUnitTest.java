package com.leungjch.universemaker;

import com.leungjch.universemaker.helpers.Vector2D;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void distance_isCorrect() {
        Vector2D vec = new Vector2D(2,2);
        assertEquals((int)vec.distance(new Vector2D(2,2)), 0);
    }
    @Test
    public void magnitude_isCorrect() {
        Vector2D vec = new Vector2D(3,4);
        assertEquals((int)vec.magnitude(), 5);
    }


    @Test
    public void angle_isCorrect() {
        Vector2D vec = new Vector2D(0,0);
        Vector2D vec2 = new Vector2D(0,1);
//      Accurate within 0.0000001
        assertEquals(vec.angle(vec2), Math.PI/2, 0.0000001);
    }

    @Test
    public void dotProduct_isCorrect() {
        Vector2D vec = new Vector2D(3,4);
        Vector2D vec2 = new Vector2D(5,6);
        assertEquals((int)vec.dotProduct(vec2), 39);
    }

    @Test
    public void getX_isCorrect() {
        Vector2D vec = new Vector2D(3,4);
        assertEquals((int)vec.getX(), 3);
    }

    @Test
    public void getY_isCorrect() {
        Vector2D vec = new Vector2D(3,4);
        assertEquals((int)vec.getY(), 4);
    }

}