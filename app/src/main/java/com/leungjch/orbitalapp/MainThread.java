package com.leungjch.orbitalapp;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean isRunning;
    public static Canvas canvas;

    private final static int MAX_FPS = 60;   // desired fps
    private final static int MAX_FRAME_SKIPS = 5;    // maximum number of frames to be skipped
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;  // the frame period

    private Object pauseLock;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        isRunning = false;
        pauseLock = new Object();

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        // https://stackoverflow.com/questions/21838523/annoying-lags-stutters-in-an-android-game
        long beginTime;     // the time when the cycle begun
        long timeDiff;      // the time it took for the cycle to execute
        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped


        sleepTime = 0;
        while (true) {
            while (isRunning) {

                synchronized (pauseLock) {
                    while (!isRunning) {
                        try {
                            Log.d("MAINTHREAD", "PAUSELOCK");
                            pauseLock.wait();

                        } catch (InterruptedException e) {
                        }
                    }
                }

                canvas = null;
                beginTime = System.currentTimeMillis();
                framesSkipped = 0;
                try {
//              Lock canvas to prevent multiple threads writing to canvas at once
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gameView.update();
                        this.gameView.draw(canvas);
                    }
                } catch (Exception e) {

                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                timeDiff = System.currentTimeMillis() - beginTime;
                sleepTime = (int) (FRAME_PERIOD - timeDiff);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        //
                    }
                }
                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    // catch up - update w/o render
                    gameView.update();
                    sleepTime += FRAME_PERIOD;
                    framesSkipped++;
                }

            }
        }
    }

    public void onPause() {
        synchronized (pauseLock) {
            isRunning = false;
        }
    }
    public void onResume() {
        synchronized (pauseLock) {
            isRunning = true;
            pauseLock.notifyAll();
        }
    }
}