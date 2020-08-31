package com.leungjch.orbitalapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("MyApp","I am here");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));    }
}