package com.leungjch.universemaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leungjch.universemaker.helpers.Vector2D;
import com.leungjch.universemaker.universe.Universe;

public class GameView extends SurfaceView implements View.OnClickListener, SurfaceHolder.Callback, Runnable{

    // Thread
    Thread thread = null;
    volatile boolean isRunning = false;

    // Contains all celestial bodies
    private Universe universe;

    // Canvas to draw on screen
    Canvas canvas;

    // Surfaceholder
    SurfaceHolder holder;

    // Track fps
    long fps;
    private long frameTime;

    // For scale control
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 0.7f;

    // For scale pivot to center
    private float focusX;
    private float focusY;

    // For pan control
    private float dx = -Universe.CONSTANTS.UNIVERSEWIDTH/4;
    private float dy = -Universe.CONSTANTS.UNIVERSEHEIGHT/4;

    private boolean isPanning = false;
    private float panStartX = 0.f;
    private float panStartY = 0.f;
    private float panEndX = 0.f;
    private float panEndY = 0.f;

    // User settings
    // Zoom mode
    public boolean isZoomMode = false;

    // Trace paths
    public boolean isTraceMode = false;

    // Player control mode
    public boolean isPlayerShipMode = false;

    // Paint for bondary
    public Paint boundaryPaint = new Paint();
    public Paint joystickPaint = new Paint();
    public Rect boundaryRect = new Rect(0,0, Universe.CONSTANTS.UNIVERSEWIDTH, Universe.CONSTANTS.UNIVERSEHEIGHT);

    // State enums
    public RESET_TYPE currentLoadType = RESET_TYPE.SINGLE_STAR_SYSTEM;
    public ADD_TYPE currentAddType = ADD_TYPE.PLANET;
    public PLACEMENT_TYPE currentPlacementType = PLACEMENT_TYPE.SCATTER;
    public SIZE_TYPE currentSizeType = SIZE_TYPE.MEDIUM;

    // Scale factor constants
    public static class SCALECONSTANTS {
        public static float MINSCALE = 0.2f;
        public static float MAXSCALE = 5.0f;
        public static float DEFAULTSCALE = 0.7f;
    }

        // Control which default preset to load when clear
    public static enum RESET_TYPE{

        BLANK, SINGLE_STAR_SYSTEM, BINARY_STAR_SYSTEM , PLANETS_COALESCING, CIRCLING_SATELLITES, BLACK_HOLE_ACCCRETION_DISK;

        // Return string enum with only first letter capitalized
        public static String[] getString() {
            String[] strs = new String[RESET_TYPE.values().length];
            int i = 0;
            for (RESET_TYPE p: RESET_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase().replace("_", " ");
            }
            return strs;
        }
    }

    // Control which type of celestial body to add
    public static enum ADD_TYPE{

        ASTEROID, PLANET, STAR, BLACK_HOLE, WHITE_HOLE, SATELLITE, PLAYER_SHIP;

        // Return string enum with only first letter capitalized
        public static String[] getString() {
        String[] strs = new String[ADD_TYPE.values().length];
        int i = 0;
        for (ADD_TYPE p: ADD_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase().replace("_", " ");
            }
        return strs;
        }
    }

    // Control current placement mode
    public static enum PLACEMENT_TYPE{

        SCATTER, FLICK, TARGET, IDLE, FIXED, ORBIT;
        // Return string enum with only first letter capitalized
        public static String[] getString() {
            String[] strs = new String[PLACEMENT_TYPE.values().length];
            int i = 0;
            for (PLACEMENT_TYPE p: PLACEMENT_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase();
            }
            return strs;
        }
    }

    // Control current size mode
    public static enum SIZE_TYPE{

        SMALL, MEDIUM, LARGE, RANDOM;

        public static String getAbbreviation(SIZE_TYPE type) {
            switch (type) {
                case SMALL:
                    return "SM";
                case MEDIUM:
                    return "MD";
                case LARGE:
                    return "LG";
                case RANDOM:
                    return "RND";
            }
            return "";
        }

        // Return string enum with only first letter capitalized
        public static String[] getString() {
            String[] strs = new String[SIZE_TYPE.values().length];
            int i = 0;
            for (SIZE_TYPE p: SIZE_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase();
            }
            return strs;
        }
    }


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();

        universe = new Universe(RESET_TYPE.BLANK);

        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        boundaryPaint = new Paint();
        boundaryPaint.setStyle(Paint.Style.FILL);
        boundaryPaint.setColor(Color.argb(255, 20,20,20));

        joystickPaint = new Paint();
        joystickPaint.setStyle(Paint.Style.FILL);
        joystickPaint.setColor(Color.argb(255, 255,255,255));

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        resume();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public void update() {
        universe.update();
    }

    private VelocityTracker mVelocityTracker = null;

    // If the user holds down touch, track the original touch position
    private int xOriginal = 0;
    private int yOriginal = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int x = (int)event.getX();
        int y = (int)event.getY();

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        // For zooming
        scaleDetector.onTouchEvent(event);


        String DEBUG_TAG = "Gesture";
        switch(action) {
            // Single tap
            case (MotionEvent.ACTION_DOWN) :
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the
                    // velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
//                    Log.d("Touched at", "dx = " + Double.toString(dx) + ", dy = " + Double.toString(dy));
                }
                else
                {
                    mVelocityTracker.clear();
                }
                if (!isZoomMode && !isPanning)
                {
                    mVelocityTracker.addMovement(event);

                }
                // If zoom mode, start panning
                if (isZoomMode) {
                    panStartX = dx + x;
                    panStartY = dy + y;
                    isPanning = true;

                }
                else {
                    universe.addCelestialBody(new Vector2D(Math.round((x-dx)/scaleFactor), Math.round((y-dy)/scaleFactor)),
                            new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                            action, currentAddType, currentSizeType, currentPlacementType);

                }

                    // Track original position
                    // Scale after
                    xOriginal = x;
                    yOriginal = y;

                return true;
            case (MotionEvent.ACTION_MOVE) :
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
//              // Printing ccurrent position
//                Log.d("Positions: ", Double.toString(dx) + " " + Double.toString(dy));
                if (isPanning && isZoomMode)
                {
                    dx = panStartX - x;
                    dy = panStartY - y;
                    if (-(dx-universe.getScreenWidth())/scaleFactor > (Universe.CONSTANTS.UNIVERSEWIDTH + Universe.CONSTANTS.UNIVERSEWIDTH*0.00f)) {
                        dx = -(Universe.CONSTANTS.UNIVERSEWIDTH + Universe.CONSTANTS.UNIVERSEWIDTH*0.00f)*scaleFactor + universe.getScreenWidth();
                    }
                    else if (-(dx+universe.getScreenWidth()*0) < 0) {
                        dx = universe.getScreenWidth()*0.0f;
                    }
                    // Check top boundary
                    if (-(dy-universe.getScreenHeight())/scaleFactor > (Universe.CONSTANTS.UNIVERSEHEIGHT + Universe.CONSTANTS.UNIVERSEHEIGHT*0.00f)) {
                        dy = -(Universe.CONSTANTS.UNIVERSEHEIGHT + Universe.CONSTANTS.UNIVERSEHEIGHT*0.00f)*scaleFactor + universe.getScreenHeight();
                    }
                    else if (-(dy+universe.getScreenHeight()*0.0f) < 0) {
                        dy = universe.getScreenHeight()*0.0f;
                    }
                }
                else if (universe.isPlayerMode && currentAddType == ADD_TYPE.PLAYER_SHIP) {
                    universe.setPlayerControls(new Vector2D((x-xOriginal), (y-yOriginal)));

//                    Log.d("PLAYERCONTROLS", Double.toString(mVelocityTracker.getXVelocity(pointerId)/scaleFactor));
                }
                else
                {
                    if (!isZoomMode)
                    {
                        universe.addCelestialBody(new Vector2D(Math.round(((x-dx)/scaleFactor)), Math.round((y-dy)/scaleFactor)),
                                new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                                action, currentAddType, currentSizeType, currentPlacementType);
                    }

                }

                return true;

            case (MotionEvent.ACTION_UP) :
                if (!isZoomMode && !isPanning)
                {
                    universe.addCelestialBody(new Vector2D((xOriginal-dx)/scaleFactor, (yOriginal-dy)/scaleFactor),
                            new Vector2D((xOriginal-x)/scaleFactor, (yOriginal-y)/scaleFactor),
                            action, currentAddType, currentSizeType, currentPlacementType);
                }
                // In zoom mode and stopped touching, stop panning
                if (isPanning)
                {
                    panEndX = x;
                    panEndY = y;
                    isPanning = false;

                }
                if (universe.isPlayerMode) {
                    universe.setPlayerControls(new Vector2D(0,0));
                }

                return true;
            case (MotionEvent.ACTION_CANCEL) :
                mVelocityTracker.recycle();

                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                return true;
            // Both fingers down
//            case (MotionEvent.ACTION_POINTER_DOWN):
//
//                return true;
//            case (MotionEvent.ACTION_POINTER_UP):
//                return true;


            default :
                return super.onTouchEvent(event);
        }
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.resetButton:
                break;
        }
    }


    public void reset(GameView.RESET_TYPE requestedPreset, float scale) {
        universe = new Universe(requestedPreset);

        switch (requestedPreset) {
            case PLANETS_COALESCING:
            case CIRCLING_SATELLITES:
                dx = 0;
                dy = 0;
                scaleFactor = SCALECONSTANTS.MINSCALE;
                break;
            default:
                dx = -Universe.CONSTANTS.UNIVERSEWIDTH/4;
                dy = -Universe.CONSTANTS.UNIVERSEHEIGHT/4;
                scaleFactor = SCALECONSTANTS.DEFAULTSCALE;
                break;
        }

//        dx = 0;
//        dy = 0;
        xOriginal = 0;
        yOriginal = 0;
        panStartY = 0;
        panStartX = 0;
        panEndY = 0;
        panEndX = 0;


    }

    // Called by MainActivity after selecting radio button

    public void setCurrentResetType(RESET_TYPE newLoadType){
        currentLoadType = newLoadType;
    }

    public void setCurrentAddType(ADD_TYPE newAddType) {
        currentAddType = newAddType;
    }
    public void setCurrentPlacementType(PLACEMENT_TYPE newPlacementType) {
        currentPlacementType = newPlacementType;
    }


    public void setCurrentSizeType(SIZE_TYPE newSizeType) {
        currentSizeType = newSizeType;
    }
    public void toggleZoomMode() {
        isZoomMode = !isZoomMode;
    }
    public void toggleTraceMode() { isTraceMode = !isTraceMode; }
    public void setCurrentDeltaT(double newDt) {
        universe.setCurrentDeltaT(newDt);
    }
    public void setCurrentSteps(int newSteps) {
        universe.setCurrentDeltaT((double)(1.0/newSteps));
//        Log.d("CURRENTDT", Double.toString(newSteps) + " " + Double.toString((double)(1.0/newSteps)));
    }

    public void setCurrentGravity(double newGravity) {
        universe.setGravity(newGravity);
//        Log.d("GRAVITY", Double.toString(newGravity));

    }
    public int getCurrentSteps() {
        return universe.getCurrentSteps();
    }
    public double getCurrentGravity() {
        return universe.getCurrentGravity();
    }

    public void draw() {

        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            canvas.save();

            super.draw(canvas);
            canvas.drawColor(1);

            canvas.translate(dx, dy);
            canvas.translate(Universe.CONSTANTS.UNIVERSEWIDTH / 2, Universe.CONSTANTS.UNIVERSEHEIGHT / 2);
            canvas.scale(scaleFactor, scaleFactor);
            canvas.translate(-Universe.CONSTANTS.UNIVERSEWIDTH / 2 / scaleFactor, -Universe.CONSTANTS.UNIVERSEHEIGHT / 2 / scaleFactor);


            if (canvas != null) {

                // draw boundary area
                canvas.drawRect(boundaryRect, boundaryPaint);
                // draw all objects
                universe.draw(canvas, isTraceMode);
            }
            canvas.restore();
            holder.unlockCanvasAndPost(canvas);
        }
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            focusX = (detector.getFocusX()-dx)/scaleFactor;
            focusY = (detector.getFocusY()-dy)/scaleFactor;
//            Log.d("FOCUS", Float.toString(focusX) + " " + Float.toString(focusY));
            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (isZoomMode) {
                scaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                scaleFactor = Math.max(SCALECONSTANTS.MINSCALE, Math.min(scaleFactor, SCALECONSTANTS.MAXSCALE));

                invalidate();

            }
            return true;

        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            this.update();

            // Draw the frame
            this.draw();

            //            // Calculate the fps this frame
            //            // We can then use the result to
            //            // time animations and more.
            //            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            //            if (timeThisFrame > 0) {
            //                fps = 1000 / timeThisFrame;
            //            }
        }
    }

    public void resume(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("ERROR", "JOINING THREAD");
        }
    }

}
