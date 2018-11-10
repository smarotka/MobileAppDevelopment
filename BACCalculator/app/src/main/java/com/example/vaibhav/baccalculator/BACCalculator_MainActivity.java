

package com.example.vaibhav.baccalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


public class BACCalculator_MainActivity extends EventHandlers {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        try{
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_baccalculator__main);

                firstLoad();

                ((Button) findViewById(R.id.saveButtonLabel)).setOnClickListener(this);
                ((Button) findViewById(R.id.addDrinkbuttonLabel)).setOnClickListener(this);
                ((Button) findViewById(R.id.resetButtonLabel)).setOnClickListener(this);
                ((SeekBar)findViewById(R.id.seekBarAlcoholPercentage)).setOnSeekBarChangeListener(this);

                }
                catch(Exception e){
                    Toast.makeText(this, "Error Occured: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
    }
}
