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
import android.widget.Toast;

import java.util.Random;

import static com.leungjch.orbitalapp.GameView.ADD_TYPE;
import static com.leungjch.orbitalapp.GameView.PLACEMENT_TYPE;
import static com.leungjch.orbitalapp.GameView.SIZE_TYPE;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity {

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
                gameView.reset();
            }
        });

        // Set default selected addType
        addTypeState = ADD_TYPE.PLANET;

        // Set default selected placementType
        placementState = PLACEMENT_TYPE.SCATTER;

        // Set default selectted size
        sizeState = SIZE_TYPE.MEDIUM;
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

    }

    // Choose type
    private void showAlertDialogAddType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Celestial Body");
        String[] list = ADD_TYPE.getString();
        alertDialog.setSingleChoiceItems(list, addTypeState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button addTypeButton = (Button)findViewById(R.id.addType);

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
                }
                dialog.dismiss();
                addTypeButton.setText(addTypeState.name());
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
                sizeButton.setText(sizeState.name());
                gameView.setCurrentSizeType(sizeState);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

}