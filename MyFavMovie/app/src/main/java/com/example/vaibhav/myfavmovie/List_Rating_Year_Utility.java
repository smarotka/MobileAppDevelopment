package com.example.vaibhav.myfavmovie;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class List_Rating_Year_Utility extends AppCompatActivity {

        protected int CURENT_INDEX;
        protected ArrayList<Movie> MOVIES;

        /**
         * DisplayMovies: Set Display by Year/Rating UI fields
         */
        protected void DisplayMovies ( int index, boolean byYear){
        Log.d("Rating_Year_Utility: ", "DisplayMovies : " + index + byYear);
            ((TextView) findViewById(R.id.pagination_label)).setText((index+1)+" of "+ MOVIES.size() );
        if (MOVIES.size() - 1 == 0) {

            SetPagingImagesClickable(false, false, false, false);
        } else if (index == 0) {
            if (MOVIES.size() - 1 == 0) {
                SetPagingImagesClickable(false, false, false, false);
            } else {
                SetPagingImagesClickable(false, false, true, true);
            }
        } else if (index == MOVIES.size() - 1) {
            SetPagingImagesClickable(true, true, false, false);
        } else {
            SetPagingImagesClickable(true, true, true, true);
        }
        if (byYear)
            Collections.sort(MOVIES);
        else
            Collections.sort(MOVIES, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return o2.getRatig() - o1.getRatig();
                }
            });
        CURENT_INDEX = index;
        Movie Movie = MOVIES.get(index);
        ((TextView) findViewById(R.id.title_name)).setText(Movie.name);
        ((TextView) findViewById(R.id.description_value_editText)).setText(Movie.description);
        ((TextView) findViewById(R.id.rating_value)).setText(Integer.toString(Movie.ratig) + getString(R.string.ratingOutOf));
        ((TextView) findViewById(R.id.genre_value)).setText(Movie.genre);
        ((TextView) findViewById(R.id.year_value)).setText(Integer.toString(Movie.year));
        ((TextView) findViewById(R.id.imdb_value)).setText(Movie.imdbRating);
    }

        /**
         * SetPagingImagesClickable: enable and disable buttons for pagination
         */
        private void SetPagingImagesClickable ( boolean first, boolean previous, boolean last, boolean next){
        Log.d("Rating_Year_Utility: ", "DisplayMovies : " + first + previous + last + next);
        ((ImageView) findViewById(R.id.first_button2)).setEnabled(first);
        ((ImageView) findViewById(R.id.previous_button)).setEnabled(previous);
        ((ImageView) findViewById(R.id.last_button)).setEnabled(last);
        ((ImageView) findViewById(R.id.next_button)).setEnabled(next);
    }

}
