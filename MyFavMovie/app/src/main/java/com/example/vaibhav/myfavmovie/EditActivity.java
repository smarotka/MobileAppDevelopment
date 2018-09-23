package com.example.vaibhav.myfavmovie;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class EditActivity extends Movie_Add_Edit_Utility {
    Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit);

            if (getIntent() != null) {
                Log.d("Log: EDIT At:", "getIntent()");
                if (getIntent().getExtras() != null) {
                    movie = (Movie) getIntent().getExtras().getSerializable(CodeAndKey.MOVIE_TO_EDIT_KEY);
                    SetupUi(movie);
                }
            }

            ((SeekBar) findViewById(R.id.rating_seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d("Log: EDIT At:", "rating_seekBar");
                    ((TextView) findViewById(R.id.progress_percentage)).setText(Integer.toString(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            findViewById(R.id.save_movie_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Log: EDIT At:", "save_movie_button");
                    SetVariable();

                    if (ValidateInputs()) {
                        Intent intent = new Intent();
                        intent.putExtra(CodeAndKey.MOVIE_TO_EDIT_KEY,
                                new Movie(NAME.getText().toString(), DESCRIPTION.getText().toString(),
                                        GENRE.getSelectedItem().toString(), RATING.getProgress(),
                                        Integer.parseInt(YEAR.getText().toString()), IMDB.getText().toString()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                }
            });
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(getApplicationContext(), "Exception Occured: Check logs", Toast.LENGTH_SHORT).show();
        }
    }


}

