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
import static java.sql.DriverManager.println;

public class MainActivity extends Activity {

    public ADD_TYPE addTypeState;
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

        // Add type radio button dialog
        Button addTypeButton = (Button)findViewById(R.id.addType);
        addTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddType();
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
                        Toast.makeText(MainActivity.this, "Planet Mode", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        addTypeState = ADD_TYPE.PLANET;

                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Star Mode", Toast.LENGTH_LONG).show();
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
        alertDialog.setTitle("Celestial Body");
        String[] list = ADD_TYPE.getString();
        alertDialog.setSingleChoiceItems(list, addTypeState.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button addTypeButton = (Button)findViewById(R.id.addType);

                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Scatter", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        addTypeState = ADD_TYPE.PLANET;

                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Target", Toast.LENGTH_LONG).show();
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


}