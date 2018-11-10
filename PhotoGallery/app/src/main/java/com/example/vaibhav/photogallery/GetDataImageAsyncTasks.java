package com.example.vaibhav.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


class GetDataImageAsyncTasks extends AsyncTask<String, Void, Bitmap> {
    IGetDataImageAsyncTasks iGetDataImageAsyncTasks;
    public GetDataImageAsyncTasks(IGetDataImageAsyncTasks iGetDataImageAsyncTasks) {
        this.iGetDataImageAsyncTasks = iGetDataImageAsyncTasks;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        Log.d("demo", "START: onPostExecute ");
        if (result != null) {
            iGetDataImageAsyncTasks.setBitMap(result);
        } else {
             if(iGetDataImageAsyncTasks.getIMAGE_URLS().size() == 1)
                iGetDataImageAsyncTasks.disableImageView(false,false,false,(iGetDataImageAsyncTasks.getSELECTEDIMAGEURL()+1),iGetDataImageAsyncTasks.getIMAGE_URLS().size());
            else iGetDataImageAsyncTasks.disableImageView(true,true,false,(iGetDataImageAsyncTasks.getSELECTEDIMAGEURL()+1),iGetDataImageAsyncTasks.getIMAGE_URLS().size());
            iGetDataImageAsyncTasks.showToast(1);
        }
        iGetDataImageAsyncTasks.showProgress(false);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        iGetDataImageAsyncTasks.showProgress(true);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Log.d("demo", "END: doInBackground ");
        HttpURLConnection connection = null;
        Bitmap resultImage = null;
        try {
            URL url = new URL(params[0].toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (iGetDataImageAsyncTasks.isConnected() && connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                resultImage = BitmapFactory.decodeStream(connection.getInputStream());
            }
            else{
                iGetDataImageAsyncTasks.showToast(3);
            }
            publishProgress();
            Log.d("demo", "END: doInBackground ");
        } catch (MalformedURLException e) {
            iGetDataImageAsyncTasks.showToast(3);
            e.printStackTrace();
        } catch (IOException e) {
            iGetDataImageAsyncTasks.showToast(3);
            e.printStackTrace();
        } catch (Exception e) {
            iGetDataImageAsyncTasks.showToast(3);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultImage;
    }

    public static interface IGetDataImageAsyncTasks
    {
        public void showProgress(boolean show);
        public int getSELECTEDIMAGEURL();
        public HashMap<Integer,String> getIMAGE_URLS();
        public void showToast(int id);
        public boolean isConnected();
        public void setBitMap(Bitmap bitMap);
        public void disableImageView(boolean next, boolean previous, boolean imageView, int currentImage, int totalImages);
    }
}
