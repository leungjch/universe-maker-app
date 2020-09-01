package com.leungjch.orbitalapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.media.AudioPlaybackCaptureConfiguration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import com.leungjch.orbitalapp.helpers.Vector2D;
import com.leungjch.orbitalapp.universe.Universe;

public class GameView extends SurfaceView implements View.OnClickListener, SurfaceHolder.Callback{
    private MainThread thread;
    private Universe universe;

    // User settings
    // Control which type of celestial body to add
    public static enum ADD_TYPE{

        PLANET, STAR;

        // Return string enum with only first letter capitalized
        public static String[] getString() {
        String[] strs = new String[ADD_TYPE.values().length];
        int i = 0;
        for (ADD_TYPE p: ADD_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase();
            }
        return strs;
        }
    }
    public ADD_TYPE currentAddType = ADD_TYPE.PLANET;
    public PLACEMENT_TYPE currentPlacementType = PLACEMENT_TYPE.SCATTER;
    // Control which placement method
    public static enum PLACEMENT_TYPE{

        SCATTER, TARGET, ORBIT;
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


    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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


    private VelocityTracker mVelocityTracker = null;
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int x = (int)event.getX();
        int y = (int)event.getY();

        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        String DEBUG_TAG = "Gesture";
        switch(action) {
            // Single tap
            case (MotionEvent.ACTION_DOWN) :
                switch(currentAddType) {
                    case PLANET:
                        universe.addPlanet(new Vector2D(x,y), currentPlacementType);
                        return true;
                    case STAR:
                        universe.addStar(new Vector2D(x,y));
                        return true;
                }
            case (MotionEvent.ACTION_MOVE) :
                switch(currentAddType) {
                    case PLANET:
                        universe.addPlanet(new Vector2D(x,y), currentPlacementType);
                        return true;
                }

            case (MotionEvent.ACTION_UP) :
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
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
    public void setCurrentPlacementType(PLACEMENT_TYPE newPlacementType) {currentPlacementType = newPlacementType;}

}
