package com.leungjch.orbitalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.leungjch.orbitalapp.universe.Universe;

import java.util.Random;

import static com.leungjch.orbitalapp.GameView.RESET_TYPE;
import static com.leungjch.orbitalapp.GameView.ADD_TYPE;
import static com.leungjch.orbitalapp.GameView.PLACEMENT_TYPE;
import static com.leungjch.orbitalapp.GameView.SIZE_TYPE;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity {

    public RESET_TYPE loadTypeState;
    public ADD_TYPE addTypeState;
    public PLACEMENT_TYPE placementState;
    public SIZE_TYPE sizeState;

    GameView gameView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("MainActivity","Creating MainActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(new GameView(this));    }
        setContentView(R.layout.activity_main);

        gameView = (GameView) findViewById(R.id.gameView);

        // Handle buttons
        // Reset button
        Button resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.reset(gameView.currentLoadType);
            }
        });

        // Set default preset type
        loadTypeState = RESET_TYPE.BLANK;

        // Set default selected addType
        addTypeState = ADD_TYPE.PLANET;

        // Set default selected placementType
        placementState = PLACEMENT_TYPE.SCATTER;

        // Set default selected size
        sizeState = SIZE_TYPE.MEDIUM;

        // Load buttons
        // Set load preset button
        Button loadTypeButton = (Button)findViewById(R.id.loadButton);
        loadTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogLoadType();
            }
        });

        // Add type button
        Button addTypeButton = (Button)findViewById(R.id.addType);
        addTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddType();
            }
        });
        // Placement Type button
        Button placementTypeButton = (Button)findViewById(R.id.placementType);
        placementTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogPlacementType();
            }
        });
        // Size Type button
        Button sizeTypeButton = (Button)findViewById(R.id.objectSizeType);
        sizeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogSizeType();
            }
        });

        // Zoom mode toggle
        final Button zoomButton = (Button)findViewById(R.id.zoomMode);
        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameView.isZoomMode)
                {
                    zoomButton.setAlpha(0.45f);
                    Toast.makeText(MainActivity.this, "Use drag move, pinch to zoom", Toast.LENGTH_LONG).show();
                }
                else
                {
                    zoomButton.setAlpha(1.f);
                    Toast.makeText(MainActivity.this, "Move mode disabled", Toast.LENGTH_LONG).show();

                }
                gameView.toggleZoomMode();
            }
        });
        // Zoom mode toggle
        final Button traceButton = (Button)findViewById(R.id.traceMode);
        traceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameView.isTraceMode)
                {
                    traceButton.setAlpha(0.45f);
                    Toast.makeText(MainActivity.this, "Trace paths ON", Toast.LENGTH_LONG).show();
                }
                else
                {
                    traceButton.setAlpha(1.0f);
                    Toast.makeText(MainActivity.this, "Trace paths OFF", Toast.LENGTH_LONG).show();
                }
                gameView.toggleTraceMode();
            }
        });

        // Speed button #0 (pause)
        final ImageButton pauseButton = (ImageButton)findViewById(R.id.pauseplay);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setCurrentDeltaT(Universe.CONSTANTS.dT_0);
            }
        });
        // Speed button #1 (slow)
        final ImageButton speedButton1 = (ImageButton)findViewById(R.id.speed1);
        speedButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setCurrentDeltaT(Universe.CONSTANTS.dT_1);
            }
        });
        // Speed button #2 (medium)
        final ImageButton speedButton2 = (ImageButton)findViewById(R.id.speed2);
        speedButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setCurrentDeltaT(Universe.CONSTANTS.dT_2);
            }
        });
        // Speed button #3 (fast)
        final ImageButton speedButton3 = (ImageButton)findViewById(R.id.speed3);
        speedButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setCurrentDeltaT(Universe.CONSTANTS.dT_3);
            }
        });
    }


    // Choose load preset type
    private void showAlertDialogLoadType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Load preset");
        String[] list = RESET_TYPE.getString();
        final Button loadTypeButton = (Button)findViewById(R.id.loadButton);

        alertDialog.setSingleChoiceItems(list, loadTypeState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Clear all", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        loadTypeState = RESET_TYPE.BLANK;
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Single star system", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        loadTypeState = RESET_TYPE.SINGLE_STAR_SYSTEM;
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Binary star system", Toast.LENGTH_LONG).show();
                        loadTypeState = RESET_TYPE.BINARY_STAR_SYSTEM;
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Random Planets", Toast.LENGTH_LONG).show();
                        loadTypeState = RESET_TYPE.RANDOM_PLANETS;
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "Random satellites", Toast.LENGTH_LONG).show();
                        loadTypeState = RESET_TYPE.RANDOM_SATELLITES;
                        break;
               }
                dialog.dismiss();
                gameView.setCurrentResetType(loadTypeState);
                gameView.reset(loadTypeState);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }


    // Choose type
    private void showAlertDialogAddType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Celestial Body");
        String[] list = ADD_TYPE.getString();
        final Button addTypeButton = (Button)findViewById(R.id.addType);

        alertDialog.setSingleChoiceItems(list, addTypeState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Asteroid", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        addTypeState = ADD_TYPE.ASTEROID;
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Planet", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        addTypeState = ADD_TYPE.PLANET;
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Star", Toast.LENGTH_LONG).show();
                        addTypeState = ADD_TYPE.STAR;
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Black Hole", Toast.LENGTH_LONG).show();
                        addTypeState = ADD_TYPE.BLACK_HOLE;
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "White Hole", Toast.LENGTH_LONG).show();
                        addTypeState = ADD_TYPE.WHITE_HOLE;
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, "Satellite", Toast.LENGTH_LONG).show();
                        addTypeState = ADD_TYPE.SATELLITE;
                        break;
                    case 6:
                        Toast.makeText(MainActivity.this, "Player ship: Drag to control", Toast.LENGTH_LONG).show();
                        addTypeState = ADD_TYPE.PLAYER_SHIP;
                        gameView.isPlayerShipMode = true; // Enable player ship mode
                        break;

                }
                dialog.dismiss();
                addTypeButton.setText(ADD_TYPE.getString()[addTypeState.ordinal()]);
                gameView.setCurrentAddType(addTypeState);
            }
        });
        AlertDialog alert = alertDialog.create();

        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
    // Choose creation mode
    private void showAlertDialogPlacementType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Placement Mode");
        String[] list = PLACEMENT_TYPE.getString();
        alertDialog.setSingleChoiceItems(list, placementState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button placementButton = (Button)findViewById(R.id.placementType);

                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Scatter", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        placementState = PLACEMENT_TYPE.SCATTER;
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Flick", Toast.LENGTH_LONG).show();
                        placementState = PLACEMENT_TYPE.FLICK;
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Target", Toast.LENGTH_LONG).show();
                        placementState = PLACEMENT_TYPE.TARGET;
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Idle", Toast.LENGTH_LONG).show();
                        placementState = PLACEMENT_TYPE.IDLE;
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "Fixed", Toast.LENGTH_LONG).show();
                        placementState = PLACEMENT_TYPE.FIXED;
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, "Orbit", Toast.LENGTH_LONG).show();
                        placementState = PLACEMENT_TYPE.ORBIT;
                        break;

                }
                dialog.dismiss();
                placementButton.setText(placementState.name());
                gameView.setCurrentPlacementType(placementState);

            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
    // Choose creation size
    private void showAlertDialogSizeType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Object Size");
        String[] list = SIZE_TYPE.getString();
        alertDialog.setSingleChoiceItems(list, sizeState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button sizeButton = (Button)findViewById(R.id.objectSizeType);

                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Small", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        sizeState = SIZE_TYPE.SMALL;
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Medium", Toast.LENGTH_LONG).show();
                        sizeState = SIZE_TYPE.MEDIUM;
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Large", Toast.LENGTH_LONG).show();
                        sizeState = SIZE_TYPE.LARGE;
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Random", Toast.LENGTH_LONG).show();
                        sizeState = SIZE_TYPE.RANDOM;
                        break;
                }
                dialog.dismiss();
                sizeButton.setText(SIZE_TYPE.getAbbreviation(sizeState));
                gameView.setCurrentSizeType(sizeState);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}