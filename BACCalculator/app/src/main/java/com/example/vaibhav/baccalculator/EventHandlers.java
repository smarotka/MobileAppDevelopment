
package com.example.vaibhav.baccalculator;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EventHandlers extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener  {
    public double weightInLds;
    public String gender;
    private double calculatedBac = 0.00;
    private String weightEmptyErrorString = "Enter the weight in lbs";


    //Method resets all controls on resert button click
    private void resetTodefault()
    {
        weightInLds = 0.0;
        calculatedBac = 0.00;

        ((EditText) findViewById(R.id.weightPlainText)).setText("");
        ((ToggleButton) findViewById(R.id.genderToggle)).setChecked(false);
        ((RadioGroup) findViewById(R.id.drinkSize)).check(R.id.oneOzLabel);
        ((SeekBar) findViewById(R.id.seekBarAlcoholPercentage)).setProgress(getBaseContext().getResources().getInteger(R.integer.seekBarDefaultForDrinkSize));
        ((Button) findViewById(R.id.saveButtonLabel)).setEnabled(true);
        ((Button) findViewById(R.id.addDrinkbuttonLabel)).setEnabled(true);
    }

    //Calculate BAC level based on weight, alchohol percentage , gender, srink size
    private void calculateBac()
    {
        int ounces = 1;
        Double alchoholPercentage = new Double(((SeekBar)findViewById(R.id.seekBarAlcoholPercentage)).getProgress())/100 ;
        Double genderConstant= 0.68;

        if (getString(R.string.genderTextOnFemale) == gender) {
            genderConstant = 0.55;
        }
        if (((RadioGroup) findViewById(R.id.drinkSize)).getCheckedRadioButtonId() == R.id.fiveOzLabel) {
            ounces = 5;
        } else if (((RadioGroup) findViewById(R.id.drinkSize)).getCheckedRadioButtonId() == R.id.tweleveOzLabel) {
            ounces = 12;
        }
        calculatedBac = calculatedBac + (ounces * alchoholPercentage * 6.24 / (weightInLds * genderConstant));
        setBacComponents();

    }

    //check if weight has been entered and saved
    private boolean CheckIfWeightAndGenderSelected()
    {
        try{

            weightInLds = Double.parseDouble(((EditText)findViewById(R.id.weightPlainText)).getText().toString());
            gender = ((ToggleButton) findViewById(R.id.genderToggle)).getText().toString();
            Toast.makeText(getApplicationContext(),"Weight and gender saved successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (NumberFormatException e)
        {
            Log.e("Invalid Weight: ", e.getMessage());
            ((EditText)findViewById(R.id.weightPlainText)).setError(weightEmptyErrorString);
            return false;
        }catch(Exception e){
            Log.e("Error: ",e.getMessage());
            return false;
        }
    }

    //On first load and Add Drink button this button updates calculated BAC level
    private void setBacComponents()
    {
        try{

            if((Math.round(calculatedBac*100.0)/100.0) == 0)
            {  ((TextView)findViewById(R.id.bacLevelLabel)).setText(
                    (Math.round(calculatedBac*10000.0)/10000.0) == 0? "BAC Level: "+ String.format("%.2f",calculatedBac):"BAC Level: "+ String.format("%.3f",calculatedBac));}
            else{
                ((TextView)findViewById(R.id.bacLevelLabel)).setText("BAC Level: "+ (Math.round(calculatedBac*100.0)/100.0));;
            }

            ((ProgressBar) findViewById(R.id.progressBarForBacLevel)).setProgress((int) (400*calculatedBac));

            TextView bacStatusBar = ((TextView) findViewById(R.id.statusValue));

            if(calculatedBac <= 0.08)
            {

                bacStatusBar.setText(BacStatusColor.GetBacStatusColor().get(StatusColor.Green).getStatusMessage());
                bacStatusBar.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor(BacStatusColor.GetBacStatusColor().get(StatusColor.Green).getColorHexValue()), PorterDuff.Mode.SRC_IN));
            }
            else if(calculatedBac > 0.08 && calculatedBac <0.20)
            {
                bacStatusBar.setText(BacStatusColor.GetBacStatusColor().get(StatusColor.Amber).getStatusMessage());
                bacStatusBar.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor(BacStatusColor.GetBacStatusColor().get(StatusColor.Amber).getColorHexValue()), PorterDuff.Mode.SRC_IN));

            }
            else  if (calculatedBac >= 0.20)
            {
                bacStatusBar.setText(BacStatusColor.GetBacStatusColor().get(StatusColor.Red).getStatusMessage());
                bacStatusBar.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor(BacStatusColor.GetBacStatusColor().get(StatusColor.Red).getColorHexValue()), PorterDuff.Mode.SRC_IN));
            }
            if( calculatedBac >= 0.25)
            {
                ((Button) findViewById(R.id.saveButtonLabel)).setEnabled(false);
                ((Button) findViewById(R.id.addDrinkbuttonLabel)).setEnabled(false);
                Toast.makeText(getApplicationContext(),"No more drinks for You",Toast.LENGTH_SHORT).show();
            }


        }
        catch(Exception e) {
            Log.e("Exception Occured : ",e.getMessage());
        }


    }

    //Set Drink Size percentage on seek bar
    private void updatePercentValue(int progressValue) {
        ((TextView)findViewById(R.id.seekbarPercentLabel)).setText(progressValue+ " %");
    }

    //set all input fields to default
    public void firstLoad()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);
        updatePercentValue(((SeekBar)findViewById(R.id.seekBarAlcoholPercentage)).getProgress());
        setBacComponents();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId() == R.id.seekBarAlcoholPercentage)
        {
            int seekBarMinForDrinkSize = getBaseContext().getResources().getInteger(R.integer.seekBarDefaultForDrinkSize);
            if(progress < seekBarMinForDrinkSize)
            {
                seekBar.setProgress(seekBarMinForDrinkSize);
                updatePercentValue(seekBarMinForDrinkSize);
            }
            else{updatePercentValue(progress);}
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.saveButtonLabel)
        {
            calculatedBac = 0;
            CheckIfWeightAndGenderSelected();

        }
        else if(v.getId()== R.id.addDrinkbuttonLabel){
            //if(CheckIfWeightAndGenderSelected()){calculateBac();}
            if(weightInLds > 0){calculateBac();}
            else if(((EditText)findViewById(R.id.weightPlainText)).getText().toString().equals("")){
                ((EditText)findViewById(R.id.weightPlainText)).setError(weightEmptyErrorString);  }
                else{
                Toast.makeText(getApplicationContext(),"Click on save button to proceed",Toast.LENGTH_SHORT).show();}
        }
        else if(v.getId()== R.id.resetButtonLabel){
            resetTodefault();
            setBacComponents();}

    }
}

