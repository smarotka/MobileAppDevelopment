package com.example.vaibhav.areacalculator;

import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Class is defined to handle all event listner
public class EventHandlers extends AreaCalculatorUtility implements View.OnClickListener {

    private static final String EMPTY_STRING = "";
    private static final String ENTER_STRING = "Enter ";
    private AreaCalculatorUtility AREA_UTILITY = new AreaCalculatorUtility();
    private EditText lengthOneEditText;
    private EditText lengthTwoEditText;
    private TextView lengthOneLabel;
    private TextView lengthTwoLabel;

    @Override
    public void onClick(View v) {
        try {

            SetTextAndEditView();
            // 'Clear' button click
            if (R.id.buttonClear == v.getId()) {
                SetDefault();
            }
            // 'Calculate' button click
            else if (R.id.buttonCalculate == v.getId()) {
                if (CheckIfLengthsAreEntered(lengthOneEditText.getText().toString(), lengthTwoEditText.getText().toString(), ((TextView) findViewById(R.id.textViewImageTitle)).getText().toString())) {
                    ((TextView) findViewById(R.id.editTextResult)).setText(String.format("%.2f", AREA_UTILITY.getCalculatedArea()));
                }
            }
            // 'Square' button click
            else if (R.id.imageViewSquare == v.getId()) {

                HideShowLengthTwoParam(View.GONE);
                lengthOneEditText.setHint(ENTER_STRING + getString(R.string.Side));
                ((TextView) findViewById(R.id.textViewImageTitle)).setText(getResources().getString(R.string.Square));
            }
            // 'Triangle' button click
            else if (R.id.imageViewTriangle == v.getId()) {
                HideShowLengthTwoParam(View.VISIBLE);
                lengthOneEditText.setHint(ENTER_STRING + getString(R.string.Height));
                lengthTwoEditText.setHint(ENTER_STRING + getString(R.string.Breadth));
                ((TextView) findViewById(R.id.textViewImageTitle)).setText(getResources().getString(R.string.Triangle));
            }
            // 'Circle' button click
            else if (R.id.imageViewCircle == v.getId()) {
                HideShowLengthTwoParam(View.GONE);
                lengthOneEditText.setHint(ENTER_STRING + getString(R.string.Radius));
                ((TextView) findViewById(R.id.textViewImageTitle)).setText(getResources().getString(R.string.Circle));
            }
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
            Toast.makeText(getApplicationContext(), "Exception Occured", Toast.LENGTH_SHORT).show();

        }
    }

    //hide length2 for square and circle
    private void HideShowLengthTwoParam(int viewStatus) {
        ((TextView) findViewById(R.id.editTextResult)).setText("");
        lengthTwoLabel.setVisibility(viewStatus);
        lengthTwoEditText.setVisibility(viewStatus);
    }

    //check if length 1 and length 2 has valid input
    public boolean CheckIfLengthsAreEntered(String lengthOne, String lengthTwo, String shape) {
        if (shape == getString(R.string.TextViewImageTitle)) {
            Toast.makeText(getApplicationContext(), getString(R.string.ShapeSelectMessage), Toast.LENGTH_SHORT).show();
            return false;
        } else if (shape == getString(R.string.Triangle) && (lengthOne.equals(EMPTY_STRING) || lengthTwo.equals(EMPTY_STRING))) {
            if (lengthOne.equals("")) {
                lengthOneEditText.setError(getString(R.string.LengthValidationMessage));
            }
            if (lengthTwo.equals("")) {
                lengthTwoEditText.setError(getString(R.string.LengthValidationMessage));
            }
            return false;
        } else if ((shape == getString(R.string.Circle) || shape == getString(R.string.Square)) && (lengthOne.equals(EMPTY_STRING))) {
            lengthOneEditText.setError(getString(R.string.LengthValidationMessage));
            return false;
        } else {
            if (Double.parseDouble(lengthOne) <= 0) {
                lengthOneEditText.setError(getString(R.string.LengthValidationMessage));
                return false;
            }
            if ((shape == getString(R.string.Triangle)) && (!lengthOne.equals("")) && (Double.parseDouble(lengthTwo) <= 0)) {
                lengthTwoEditText.setError(getString(R.string.LengthValidationMessage));
                return false;
            }
            AREA_UTILITY.setLengthOne(Double.parseDouble(lengthOne));
            if (!lengthTwo.equals(""))
                AREA_UTILITY.setLengthTwo(Double.parseDouble(lengthTwo));
            AREA_UTILITY.setShape(shape);
            AREA_UTILITY.CalculateArea(getResources());
            return true;
        }
    }

    private  void SetTextAndEditView()
    {
        lengthOneEditText = ((EditText) findViewById(R.id.LengthOneEditText));
        lengthTwoEditText = ((EditText) findViewById(R.id.LengthTwoEditText));
        lengthOneLabel = ((TextView) findViewById(R.id.LengthOneTextView));
        lengthTwoLabel = ((TextView) findViewById(R.id.LengthTwoTextView));
    }
    //set to default setting on clear button click
    private void SetDefault() {
        SpannableString content = new SpannableString(emptyString);
        content.setSpan(new UnderlineSpan(), 0, emptyString.length(), 0);
        AREA_UTILITY = new AreaCalculatorUtility();

        lengthOneEditText.setText(content);
        lengthTwoEditText.setText(content);
        ((TextView) findViewById(R.id.textViewImageTitle)).setText(getString(R.string.TextViewImageTitle));
        ((TextView) findViewById(R.id.editTextResult)).setText(emptyString);

        lengthOneEditText.setHint(R.string.LengthOneEditText);
        lengthOneLabel.setText(R.string.LengthOneTextView);
        lengthTwoEditText.setHint(R.string.LengthTwoEditText);
        lengthTwoLabel.setText(R.string.LengthTwoTextView);
        HideShowLengthTwoParam(View.VISIBLE);

    }
}
