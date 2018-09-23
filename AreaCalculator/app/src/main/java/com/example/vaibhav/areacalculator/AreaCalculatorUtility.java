package com.example.vaibhav.areacalculator;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

//Class is defined for area calculation utilities
public class AreaCalculatorUtility extends AppCompatActivity {

    Double lengthOne;
    Double lengthTwo;
    Double calculatedArea;
    String shape;
    String emptyString = "";

    public Double getCalculatedArea() {
        return calculatedArea;
    }

    public void setCalculatedArea(Double calculatedArea) {
        this.calculatedArea = calculatedArea;
    }


    public void setShape(String shape) {
        this.shape = shape;
    }


    public void setLengthOne(Double lengthOne) {
        this.lengthOne = lengthOne;
    }


    public void setLengthTwo(double lengthTwo) {
        this.lengthTwo = lengthTwo;
    }




    //calculate shape, length 1 and length 2 based area
    public void CalculateArea(Resources res) {
        if (shape == res.getString(R.string.Triangle)) {
            this.setCalculatedArea(0.5 * this.lengthOne * this.lengthTwo);
        }
        if (shape == res.getString(R.string.Circle)) {
            this.setCalculatedArea(3.1416 * this.lengthOne * this.lengthOne);
        }
        if (shape == res.getString(R.string.Square)) {
            this.setCalculatedArea(this.lengthOne * this.lengthOne);
        }
    }
}
