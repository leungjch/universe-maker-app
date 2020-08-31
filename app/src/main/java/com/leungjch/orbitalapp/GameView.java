package com.leungjch.orbitalapp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.leungjch.orbitalapp.universe.Universe;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;
    private Universe universe;
    public GameView(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        universe = new Universe();

        Log.d("Star","Star created");

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        universe.update();

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        if(canvas!=null){
            universe.draw(canvas);
        }
    }
}
