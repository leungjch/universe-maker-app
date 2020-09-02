package com.leungjch.orbitalapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
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
    private float scaleFactor = 1.f;

    // For pan control
    private float dx = 0.f;
    private float dy = 0.f;
    private boolean isPanning = false;
    private float panStartX = 0.f;
    private float panStartY = 0.f;
    private float panEndX = 0.f;
    private float panEndY = 0.f;

    // User settings
    // Zoom mode
    public boolean isZoomMode = true;




    // Control which type of celestial body to add
    public static enum ADD_TYPE{

        ASTEROID, PLANET, STAR, BLACK_HOLE, WHITE_HOLE;

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

        SCATTER, FLICK, TARGET, ORBIT;
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

    }
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);


        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
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
                mVelocityTracker.addMovement(event);
                universe.addCelestialBody(new Vector2D(Math.round((x-dx)/scaleFactor), Math.round((y-dy)/scaleFactor)),
                        new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                        action, currentAddType, currentSizeType, currentPlacementType);
                // Track original position
                // Scale after
                xOriginal = x;
                yOriginal = y;

                return true;
            case (MotionEvent.ACTION_MOVE) :
                if (isPanning)
                {
//                    panEndX = x;
//                    panEndY = y;
                    dx = panStartX - x;
                    dy = panStartY - y;
                }
                else
                {
                    mVelocityTracker.addMovement(event);
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    mVelocityTracker.computeCurrentVelocity(1000);

                    universe.addCelestialBody(new Vector2D(Math.round(((x-dx)/scaleFactor)), Math.round((y-dy)/scaleFactor)),
                            new Vector2D(mVelocityTracker.getXVelocity(pointerId)/scaleFactor,mVelocityTracker.getYVelocity(pointerId)/scaleFactor),
                            action, currentAddType, currentSizeType, currentPlacementType);


                }

                return true;

            case (MotionEvent.ACTION_UP) :
                universe.addCelestialBody(new Vector2D((xOriginal-dx)/scaleFactor, (yOriginal-dy)/scaleFactor),
                                          new Vector2D((xOriginal-x)/scaleFactor, (yOriginal-y)/scaleFactor),
                                          action, currentAddType, currentSizeType, currentPlacementType);

                return true;
            case (MotionEvent.ACTION_CANCEL) :
                mVelocityTracker.recycle();

                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                return true;
            // Both fingers down
            // Start pan
            case (MotionEvent.ACTION_POINTER_DOWN):
                panStartX = dx + x;
                panStartY = dy + y;
                isPanning = true;
                return true;
            case (MotionEvent.ACTION_POINTER_UP):
                panEndX = x;
                panEndY = y;
//                dx =  panEndX - panStartX;
//                dy = panEndY - panStartY;
                isPanning = false;
                return true;


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
    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        super.draw(canvas);
        canvas.translate(dx, dy);
        canvas.scale(scaleFactor, scaleFactor);

        if(canvas!=null){
            universe.draw(canvas);
        }
        canvas.restore();

    }





    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

}
