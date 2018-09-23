package com.example.vaibhav.myfavmovie;

import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Movie_Add_Edit_Utility extends AppCompatActivity {

    protected EditText NAME;
    protected EditText DESCRIPTION;
    protected SeekBar RATING;
    protected Spinner GENRE;
    protected EditText YEAR;
    protected EditText IMDB;

    /**
     * SetVariable: get all ui controls and set variables
     */
    protected void SetVariable() {
        NAME = ((EditText) findViewById(R.id.movie_name_editText));
        DESCRIPTION = ((EditText) findViewById(R.id.description_editText));
        RATING = ((SeekBar) findViewById(R.id.rating_seekBar));
        GENRE = ((Spinner) findViewById(R.id.spinner));
        YEAR = ((EditText) findViewById(R.id.year_editText2));
        IMDB = ((EditText) findViewById(R.id.imdb_editText));
    }

    /**
     * SetupUi: For edit window set all controls by movie properties
     */
    protected  void SetupUi(Movie movie)
    {
        ((EditText) findViewById(R.id.movie_name_editText)).setText(movie.getName());
        ((EditText) findViewById(R.id.description_editText)).setText(movie.getDescription());
        ((SeekBar) findViewById(R.id.rating_seekBar)).setProgress(movie.getRatig());
        ((TextView)findViewById(R.id.progress_percentage)).setText((Integer.toString(movie.getRatig())));
        ((Spinner) findViewById(R.id.spinner)).setSelection(Arrays.asList((getResources().getStringArray(R.array.movie_genre))).indexOf(movie.getGenre()));
        ((EditText) findViewById(R.id.year_editText2)).setText(String.format("%04d", movie.getYear()));
        ((EditText) findViewById(R.id.imdb_editText)).setText(movie.getImdbRating());
    }
    /**
     * ValidateInputs: Check if all input Movie fields are valid
     */
    protected boolean ValidateInputs() {
        boolean isValid = true;
        SimpleDateFormat dateformat = new SimpleDateFormat("y");
        if (NAME.getText().toString().equals("") || NAME.getText().toString().equals(null) || NAME.getText().toString().trim().length() == 0 ) {
            NAME.setError(getString(R.string.moviename_error));
            isValid = false;
        }
        if (DESCRIPTION.getText().toString().equals("") || DESCRIPTION.getText().toString().equals(null)) {
            DESCRIPTION.setError(getString(R.string.description_error));
            isValid = false;
        }
        if (GENRE.getSelectedItem().toString().equals(getResources().getStringArray(R.array.movie_genre)[0])) {
            Toast.makeText(getApplicationContext(), getString(R.string.genre_error), Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (YEAR.getText().toString().equals("") || YEAR.getText().toString().equals(null)) {
            YEAR.setError(getString(R.string.year_error));
            isValid = false;
        } else if (Integer.parseInt(YEAR.getText().toString()) > Integer.parseInt(dateformat.format(new Date())) || Integer.parseInt(YEAR.getText().toString()) < 0001 || (YEAR.getText().toString().length() < 4)) {
            YEAR.setError(getString(R.string.year_Valid_error));
            isValid = false;
        }
        if (IMDB.getText().toString().equals("") || IMDB.getText().toString().equals(null)) {
            IMDB.setError(getString(R.string.imdb_error));
            isValid = false;
        } else if (!Patterns.WEB_URL.matcher(IMDB.getText().toString()).matches()) {
            IMDB.setError("Enter Valid Link");
            isValid = false;
        } else if (!IMDB.getText().toString().toLowerCase().contains("imdb")) {
            IMDB.setError(getString(R.string.imdb_valid_error));
            isValid = false;
        }

        return isValid;
    }
}
