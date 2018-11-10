
package com.example.vaibhav.areacalculator;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AreaCalculator_MainActivity extends EventHandlers {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_area_calculator__main);

            // Set action bar Icon
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher_round);
            //set all on click listner
            ((Button) findViewById(R.id.buttonClear)).setOnClickListener(this);
            ((Button) findViewById(R.id.buttonCalculate)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.imageViewSquare)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.imageViewCircle)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.imageViewTriangle)).setOnClickListener(this);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
