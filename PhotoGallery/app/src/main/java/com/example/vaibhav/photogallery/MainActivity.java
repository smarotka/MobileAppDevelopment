
package com.example.vaibhav.photogallery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import android.app.Dialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetDataAsyncTasks.IGetDataAsyncTasks, GetDataImageAsyncTasks.IGetDataImageAsyncTasks {
    AlertDialog.Builder BUILDER_DROPDOWN = null;
    ProgressDialog BUILDER_PROGRESS_BAR = null;
    ProgressDialog BUILDER_PHOTO_SPINNER = null;
    public HashMap<Integer, String> IMAGE_URLS = null;
    public int SELECTED_IMAGE_URL = 0;
    public TextView keyword_textView = null;
    // public ProgressBar image_loader = null;
    public Button go_button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disableImageView(false, false, true, 0, 0);
        keyword_textView = ((TextView) findViewById(R.id.keyword_textView));
        go_button = ((Button) findViewById(R.id.go_button));
        initializeProgreeLoader();


        if (isConnected()) {
            new GetDataAsyncTasks(MainActivity.this).execute(getString(R.string.keyword_url), false);


            ((ImageView) findViewById(R.id.previous_imageView)).setOnClickListener(this);
            ((ImageView) findViewById(R.id.next_imageView)).setOnClickListener(this);
            ((Button) findViewById(R.id.go_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo", "START: go_button  setOnClickListener ");
                    if (BUILDER_DROPDOWN != null) {
                        AlertDialog dialog = BUILDER_DROPDOWN.create();
                        dialog.show();
                    } else {
                        showToast(1);
                    }
                    if (isConnected()) {

                    } else {
                        showToast(0);
                    }
                    Log.d("demo", "END: go_button  setOnClickListener ");
                }
            });
        } else {
            showToast(0);
        }

    }

    public void showProgress(boolean showIt) {
        if (showIt)
            BUILDER_PROGRESS_BAR.show();
        else
            BUILDER_PROGRESS_BAR.dismiss();
    }

    private void initializeProgreeLoader() {
        BUILDER_PROGRESS_BAR = new ProgressDialog(this);
        BUILDER_PROGRESS_BAR.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        BUILDER_PROGRESS_BAR.setTitle(AsynInBackgroudResultParams.DictionaryLoaderMessage);
        BUILDER_PROGRESS_BAR.setCancelable(false);

    }

    /*Dialog builder for keyword list*/
    public void initBuilder(final String result) {
        BUILDER_DROPDOWN = new AlertDialog.Builder(MainActivity.this);
        BUILDER_DROPDOWN.setTitle("Choose A keyword").setItems(result.split(";"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(isConnected()) {
                    keyword_textView.setText(result.split(";")[which]);
                    BUILDER_PROGRESS_BAR.setTitle(AsynInBackgroudResultParams.DictionaryLoaderMessage);
                    BUILDER_PROGRESS_BAR.show();
                    triggerUrlImagerequest();
                }
                else{
                    showToast(0);
                }
            }
        });
        go_button.setEnabled(true);
    }

    /*Utility to Show Toast*/
    public void showToast(int id) {
        if (id == 3)
            showProgress(false);

        switch (id) {
            case 0:
                Toast.makeText(getApplicationContext(), getString(R.string.no_Connection), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getApplicationContext(), "No Images/Data Found", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Invalid Operation Check logs", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "No Internet or Connection Interrupted", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    /*getter for Hashmap of url lists*/
    public HashMap<Integer, String> getIMAGE_URLS() {
        return IMAGE_URLS;
    }

    /*getter for selected index of image*/
    public int getSELECTEDIMAGEURL() {
        return SELECTED_IMAGE_URL;
    }

    /*Trigger URL fetching request based on selected keyword*/
    public void triggerUrlImagerequest() {
        SELECTED_IMAGE_URL = 0;
        String keyword = ((TextView) findViewById(R.id.keyword_textView)).getText().toString().trim();
        Log.d("demo", "START: triggerUrlImagerequest");
        RequestParams params = new RequestParams();
        params.addParams("keyword", keyword);
        new GetDataAsyncTasks(MainActivity.this, params).execute(getString(R.string.image_urls), true);
        Log.d("demo", "END: triggerUrlImagerequest");
    }

    /*Connection check*/
    public boolean isConnected() {
        Log.d("demo", "START: isConnected");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Log.e("Error", getString(R.string.no_Connection));
            return false;
        }
        Log.d("demo", "END: isConnected is " + true);
        return true;
    }

    /*Set bitmap image received from http request*/
    public void setBitMap(Bitmap bitMap) {
        BUILDER_PROGRESS_BAR.dismiss();
        ((ImageView) findViewById(R.id.display_imageView)).setImageBitmap(bitMap);
    }

    /*handle next and previous buttons */
    public void handleImageView(String imageUrl) {
        if (imageUrl == null || imageUrl.equals("")) {
            disableImageView(false, false, false, 0, 0);
        } else {
            IMAGE_URLS = new HashMap<Integer, String>();
            String[] urls = imageUrl.split("\\r?\\n");
            for (int i = 0; i < urls.length; i++) {
                IMAGE_URLS.put(i, urls[i].trim());
                if (urls.length == 1)
                    disableImageView(false, false, true, (SELECTED_IMAGE_URL + 1), IMAGE_URLS.size());
                else
                    disableImageView(true, true, true, (SELECTED_IMAGE_URL + 1), IMAGE_URLS.size());
            }

            new GetDataImageAsyncTasks(MainActivity.this).execute(IMAGE_URLS.get(SELECTED_IMAGE_URL));
        }
    }

    /*Enabling and Disabling next and previous buttons */
    public void disableImageView(boolean next, boolean previous, boolean imageView, int currentImage, int totalImages) {
        ((ImageView) findViewById(R.id.next_imageView)).setEnabled(next);
        ((ImageView) findViewById(R.id.previous_imageView)).setEnabled(previous);
        if (!imageView) {
            BUILDER_PROGRESS_BAR.dismiss();
            ((ImageView) findViewById(R.id.display_imageView)).setImageDrawable(getDrawable(R.drawable.no_image_available));
        }
        ((TextView) findViewById(R.id.pagination_label)).setText(currentImage + " of " + totalImages);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_imageView:
                if (isConnected()) {
                    SELECTED_IMAGE_URL = SELECTED_IMAGE_URL == 0 ? IMAGE_URLS.size() - 1 : SELECTED_IMAGE_URL - 1;
                    disableImageView(true, true, true, (SELECTED_IMAGE_URL + 1), IMAGE_URLS.size());
                    BUILDER_PROGRESS_BAR.setTitle(AsynInBackgroudResultParams.PhotoLoaderMessage);
                    BUILDER_PROGRESS_BAR.show();
                    new GetDataImageAsyncTasks(MainActivity.this).execute(IMAGE_URLS.get(SELECTED_IMAGE_URL));
                } else {
                    showToast(0);
                }
                break;
            case R.id.next_imageView:
                if (isConnected()) {
                    SELECTED_IMAGE_URL = SELECTED_IMAGE_URL == IMAGE_URLS.size() - 1 ? 0 : SELECTED_IMAGE_URL + 1;
                    disableImageView(true, true, true, (SELECTED_IMAGE_URL + 1), IMAGE_URLS.size());
                    BUILDER_PROGRESS_BAR.setTitle(AsynInBackgroudResultParams.PhotoLoaderMessage);
                    BUILDER_PROGRESS_BAR.show();
                    new GetDataImageAsyncTasks(MainActivity.this).execute(IMAGE_URLS.get(SELECTED_IMAGE_URL));
                } else {
                    showToast(0);
                }
                break;
        }
    }
}

