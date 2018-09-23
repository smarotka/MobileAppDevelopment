package com.example.vaibhav.myfavmovie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DisplayListYearActivity extends List_Rating_Year_Utility implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d("Log: ListYear At:", "onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_list_year);

            if (getIntent() != null) {
                Log.d("Log: ListYear At:", "getIntent");
                if (getIntent().getExtras() != null) {
                    MOVIES = (ArrayList<Movie>) getIntent().getExtras().getSerializable(CodeAndKey.MOVIE_TO_BY_YEAR_KEY);
                    DisplayMovies(0, true);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_array), Toast.LENGTH_SHORT).show();
                }
            }


            ((ImageView) findViewById(R.id.first_button2)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.previous_button)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.next_button)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.last_button)).setOnClickListener(this);
            ((Button) findViewById(R.id.finish_button)).setOnClickListener(this);

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(getApplicationContext(), "Exception Occured: Check logs", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("Log: ListYear At:", "onClick");
        if (v.getId() == R.id.first_button2)
            DisplayMovies(0, true);
        if (v.getId() == R.id.last_button)
            DisplayMovies(MOVIES.size() - 1, true);
        if (v.getId() == R.id.next_button)
            DisplayMovies(CURENT_INDEX + 1, true);
        if (v.getId() == R.id.previous_button)
            DisplayMovies(CURENT_INDEX - 1, true);
        if (v.getId() == R.id.finish_button)
            finish();
    }


}
