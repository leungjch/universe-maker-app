package com.leungjch.orbitalapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioPlaybackCaptureConfiguration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;


import com.leungjch.orbitalapp.helpers.Vector2D;
import com.leungjch.orbitalapp.universe.Universe;

public class GameView extends SurfaceView implements View.OnClickListener, SurfaceHolder.Callback{
    private MainThread thread;
    private Universe universe;

    // For scale control
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 0.7f;
    // For scale pivot to center
    private float focusX;
    private float focusY;

    // For pan control
    private float dx = -Universe.CONSTANTS.UNIVERSEWIDTH/4;
    private float dy = -Universe.CONSTANTS.UNIVERSEHEIGHT/4;
//    private float dx = 0;
//    private float dy = 0;

    private boolean isPanning = false;
    private float panStartX = 0.f;
    private float panStartY = 0.f;
    private float panEndX = 0.f;
    private float panEndY = 0.f;

    // User settings
    // Zoom mode
    public boolean isZoomMode = false;

    // Paint for bondary
    public Paint boundaryPaint = new Paint();
    public Rect boundaryRect = new Rect(0,0, Universe.CONSTANTS.UNIVERSEWIDTH, Universe.CONSTANTS.UNIVERSEHEIGHT);



    // Control which type of celestial body to add
    public static enum ADD_TYPE{

        ASTEROID, PLANET, STAR, BLACK_HOLE, WHITE_HOLE, DRONE;

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
    public ADD_TYPE currentAddType = ADD_TYPE.PLANET;
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
    public PLACEMENT_TYPE currentPlacementType = PLACEMENT_TYPE.SCATTER;

    // Control current size mode
    public static enum SIZE_TYPE{

        SMALL, MEDIUM, LARGE, RANDOM;
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
    public SIZE_TYPE currentSizeType = SIZE_TYPE.MEDIUM;

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        boundaryPaint = new Paint();
        boundaryPaint.setStyle(Paint.Style.FILL);
        boundaryPaint.setColor(Color.argb(255, 20,20,200));

        boundaryPaint.setStyle(Paint.Style.STROKE);
        boundaryPaint.setColor(Color.argb(255, 255,255,255));
        boundaryPaint.setStrokeWidth(20f);

    }
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        boundaryPaint = new Paint();
        boundaryPaint.setStyle(Paint.Style.FILL);
        boundaryPaint.setColor(Color.argb(255, 20,20,20));
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        boundaryPaint = new Paint();
        boundaryPaint.setStyle(Paint.Style.FILL);
        boundaryPaint.setColor(Color.argb(255, 20,20,20));

    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);


        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        boundaryPaint = new Paint();
        boundaryPaint.setStyle(Paint.Style.FILL);
        boundaryPaint.setColor(Color.argb(255, 20,20,20));

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

                }
                else
                {
                    mVelocityTracker.clear();
                }
                if (!isZoomMode && !isPanning)
                {


                    mVelocityTracker.addMovement(event);

                    universe.addCelestialBody(new Vector2D(Math.round((x-dx)/scaleFactor), Math.round((y-dy)/scaleFactor)),
                            new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                            action, currentAddType, currentSizeType, currentPlacementType);
                }
                // If zoom mode, start panning
                if (isZoomMode) {
                    panStartX = dx + x;
                    panStartY = dy + y;
                    isPanning = true;

                }

                    // Track original position
                    // Scale after
                    xOriginal = x;
                    yOriginal = y;

                return true;
            case (MotionEvent.ACTION_MOVE) :

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
                else
                {
                    mVelocityTracker.addMovement(event);
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    mVelocityTracker.computeCurrentVelocity(1000);
                    if (!isZoomMode)
                    {
                        universe.addCelestialBody(new Vector2D(Math.round(((x-dx)/scaleFactor)), Math.round((y-dy)/scaleFactor)),
                                new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                                action, currentAddType, currentSizeType, currentPlacementType);
                    }


                }

                return true;

            case (MotionEvent.ACTION_UP) :
                if (!isZoomMode)
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
                Log.d("Hello", "Press");
                break;
        }
    }

    public void reset() {
        universe = new Universe();
        dx = -Universe.CONSTANTS.UNIVERSEWIDTH/4;
        dy = -Universe.CONSTANTS.UNIVERSEHEIGHT/4;
        xOriginal = 0;
        yOriginal = 0;
        panStartY = 0;
        panStartX = 0;
        scaleFactor = 0.7f;

    }

    // Called by MainActivity after selecting radio button
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
    public void setCurrentDeltaT(double newDt) {
        universe.setCurrentDeltaT(newDt);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        super.draw(canvas);
        canvas.drawColor(1);

        canvas.translate(dx, dy);
        canvas.translate(Universe.CONSTANTS.UNIVERSEWIDTH/2, Universe.CONSTANTS.UNIVERSEHEIGHT/2);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(-Universe.CONSTANTS.UNIVERSEWIDTH/2/scaleFactor, -Universe.CONSTANTS.UNIVERSEHEIGHT/2/scaleFactor);

//        canvas.translate(-Universe.CONSTANTS.UNIVERSEWIDTH/2, -Universe.CONSTANTS.UNIVERSEHEIGHT/2);

        if(canvas!=null){

            // draw boundary area
            canvas.drawRect(boundaryRect,boundaryPaint);
            // draw all objects
            universe.draw(canvas);
        }
        canvas.restore();

    }





    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            focusX = (detector.getFocusX()-dx)/scaleFactor;
            focusY = (detector.getFocusY()-dy)/scaleFactor;
            Log.d("FOCUS", Float.toString(focusX) + " " + Float.toString(focusY));
            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (isZoomMode) {
                scaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                scaleFactor = Math.max(0.2f, Math.min(scaleFactor, 5.0f));

                invalidate();

            }
            return true;

        }
    }

}
