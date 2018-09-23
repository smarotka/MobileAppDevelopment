//Assignment Name : Homework03
//Group no 23: Mohit Saini, Srishtee
package com.example.vaibhav.myfavmovie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movieWithIDS;
    String deletedName;
    Movie movieSelected;
    int editInedexSelected;


    /**
     * onActivityResult to get and set all received intents
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            Log.d("Log: ", "onActivityResult");
            if (data != null) {
                if (requestCode == CodeAndKey.MOVIE_TO_ADD_MOVIE_CODE) {
                    if (data.getExtras() != null) {
                        movieWithIDS.add((Movie) data.getExtras().getSerializable(CodeAndKey.MOVIE_TO_ADD_MOVIE_KEY));
                        Toast.makeText(getApplicationContext(), getString(R.string.Movie_Added), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.Movie_Not_Added), Toast.LENGTH_SHORT).show();
                    }
                }
                if (requestCode == CodeAndKey.MOVIE_TO_EDIT_CODE) {
                    if (data.getExtras().getSerializable(CodeAndKey.MOVIE_TO_EDIT_KEY) != null) {
                        movieWithIDS.set(editInedexSelected, (Movie) data.getExtras().getSerializable(CodeAndKey.MOVIE_TO_EDIT_KEY));
                        Toast.makeText(getApplicationContext(), getString(R.string.Movie_Edit), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.Movie_Not_Edit), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(getApplicationContext(), "Exception Occured: Check logs", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d("Log: ", "onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            movieWithIDS = new ArrayList<Movie>();

            /**
             * Set App bar Icon
             */
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_movie);

            /**
             * On Moview by Year click event
             */
            ((Button) findViewById(R.id.byyear)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Log: ", "byyear click event");
                    if (movieWithIDS.size() > 0) {
                        Intent intent = new Intent(getString(R.string.BY_YEAR_VIEW));
                        intent.putExtra(CodeAndKey.MOVIE_TO_BY_YEAR_KEY, movieWithIDS);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.empty_array), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            /**
             * On Moview by Rating click event
             */
            ((Button) findViewById(R.id.byrating)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Log: ", "byrating click event");
                    if (movieWithIDS.size() > 0) {
                        Intent intent = new Intent(getString(R.string.BY_RATING_VIEW));
                        intent.putExtra(CodeAndKey.MOVIE_TO_BY_RATING_KEY, movieWithIDS);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.empty_array), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            /**
             * On Delete Movie click event
             */
            ((Button) findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                CharSequence[] charSequenceItems;

                @Override
                public void onClick(View v) {
                    Log.d("Log: ", "delete click event");
                    List<String> listItems = new ArrayList<String>();
                    if (movieWithIDS.size() > 0) {
                        Iterator<Movie> iter = movieWithIDS.iterator();
                        while (iter.hasNext()) {
                            Movie movie = iter.next();
                            listItems.add(movie.getName());
                            charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Pick a Movie")
                                .setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        deletedName = (String) charSequenceItems[which];
                                        movieWithIDS.remove(which);
                                        Toast.makeText(MainActivity.this, deletedName + "  is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        ;
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.nothing_to_delete), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            /**
             * On Edit Movie click event
             */
            ((Button) findViewById(R.id.editmovie)).setOnClickListener(new View.OnClickListener() {
                CharSequence[] charSequenceItems;

                @Override
                public void onClick(View v) {
                    Log.d("Log: ", "editmovie");
                    List<String> listItems = new ArrayList<String>();
                    if (movieWithIDS.size() > 0) {
                        Iterator<Movie> iter = movieWithIDS.iterator();
                        while (iter.hasNext()) {
                            Movie movie = iter.next();
                            listItems.add(movie.getName());
                            charSequenceItems = listItems.toArray(new CharSequence[listItems.size()]);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Pick a Movie")
                                .setItems(charSequenceItems, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editInedexSelected = which;
                                        movieSelected = movieWithIDS.get(which);
                                        Log.d("last", movieSelected.toString());
                                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                        intent.putExtra(CodeAndKey.MOVIE_TO_EDIT_KEY, movieSelected);
                                        startActivityForResult(intent, CodeAndKey.MOVIE_TO_EDIT_CODE);


                                    }
                                })
                        ;
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.nothing_to_edit), Toast.LENGTH_SHORT).show();
                    }


                }
            });

            /**
             * On Add Movie click event
             */
            ((Button) findViewById(R.id.addMovie)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Log: ", "addMovie");
                    Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                    startActivityForResult(intent, CodeAndKey.MOVIE_TO_ADD_MOVIE_CODE);
                }
            });
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            Toast.makeText(getApplicationContext(), "Exception Occured: Check logs", Toast.LENGTH_SHORT).show();
        }

    }
}
