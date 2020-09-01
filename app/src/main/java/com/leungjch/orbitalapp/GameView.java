package com.leungjch.orbitalapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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


        public static String[] getString() {
        String[] strs = new String[ADD_TYPE.values().length];
        int i = 0;
        for (ADD_TYPE p: ADD_TYPE.values()) {
                strs[i++] = p.toString().substring(0,1).toUpperCase() + p.toString().substring(1).toLowerCase();
            }
        return strs;
        }
    }
    public ADD_TYPE currentAddType;

    // Control which placement method
    public static enum PLACEMENT_TYPE{

        SCATTER, TARGET, ORBIT;

        public static String[] getString() {
            String[] strs = new String[ADD_TYPE.values().length];
            int i = 0;
            for (ADD_TYPE p: PLACEMENT_TYPE.values()) {
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


    // This example shows an Activity, but you would use the same approach if
// you were subclassing a View.
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int x = (int)event.getX();
        int y = (int)event.getY();

        int action = MotionEventCompat.getActionMasked(event);
        String DEBUG_TAG = "Gesture";
        switch(action) {
            // Single tap
            case (MotionEvent.ACTION_DOWN) :
//                Log.d(DEBUG_TAG,"Action was DOWN");
                switch(currentAddType) {
                    case PLANET:
                        universe.addPlanet(new Vector2D(x,y));
                        return true;
                    case STAR:
                        universe.addStar(new Vector2D(x,y));
                        return true;
                }
            case (MotionEvent.ACTION_MOVE) :
//                Log.d(DEBUG_TAG,"Action was MOVE");
                switch(currentAddType) {
                    case PLANET:
                        universe.addPlanet(new Vector2D(x,y));
                        return true;
                    // Dont allow star spam
//                    case STAR:
//                        universe.addStar(new Vector2D(x,y));
//                        return true;
                }

            case (MotionEvent.ACTION_UP) :
//                Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
//                Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
//                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//                        "of current screen element");
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


}
