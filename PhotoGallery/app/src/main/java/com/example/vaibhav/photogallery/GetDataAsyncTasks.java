package com.example.vaibhav.photogallery;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class GetDataAsyncTasks extends AsyncTask<Object, Void, AsynInBackgroudResultParams> {
    RequestParams mParams = null;
    IGetDataAsyncTasks iGetDataAsyncTasks;
    public GetDataAsyncTasks(IGetDataAsyncTasks iGetDataAsyncTasks,RequestParams params) {
        mParams = params;
        this.iGetDataAsyncTasks = iGetDataAsyncTasks;
    }

    public GetDataAsyncTasks(IGetDataAsyncTasks iGetDataAsyncTasks) {
        this.iGetDataAsyncTasks = iGetDataAsyncTasks;
        mParams = null;
    }

    @Override
    protected void onPostExecute(final AsynInBackgroudResultParams result) {
        Log.d("demo", "START: onPostExecute " + result);
        if (result != null && !result.result.equals("")) {
            if (!result.isKeywordRequest) {
                iGetDataAsyncTasks.initBuilder(result.result);
            }
            if (result.result != null && result.isKeywordRequest) {
                iGetDataAsyncTasks.handleImageView(result.result);
            }
        } else {
            if (result.result.equals("")) {
                iGetDataAsyncTasks.handleImageView(result.result);
                iGetDataAsyncTasks.showToast(1);
            } else iGetDataAsyncTasks.showToast(2);
        }
    }


    @Override
    protected AsynInBackgroudResultParams doInBackground(final Object... params) {

        Log.d("demo", "END: doInBackground ");
        HttpURLConnection connection = null;
        AsynInBackgroudResultParams asynInputParams = null;
        try {
            URL url = new URL(mParams == null ? params[0].toString() : mParams.getEncodedUrl(params[0].toString()));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (iGetDataAsyncTasks.isConnected() && connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                asynInputParams = new AsynInBackgroudResultParams();
                asynInputParams.isKeywordRequest = Boolean.parseBoolean(params[1].toString());
                asynInputParams.result = IOUtils.toString(connection.getInputStream(), "UTF8");
            }
            else{
                iGetDataAsyncTasks.showToast(3);
            }
            Log.d("demo", "END: doInBackground ");
        } catch (MalformedURLException e) {
            iGetDataAsyncTasks.showToast(3);
            e.printStackTrace();
        } catch (IOException e) {
            iGetDataAsyncTasks.showToast(3);
            e.printStackTrace();
        } catch (Exception e) {
            iGetDataAsyncTasks.showToast(3);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return asynInputParams;
    }
    public static interface IGetDataAsyncTasks
    {
        public void initBuilder(String result);
        public void triggerUrlImagerequest();
        public void handleImageView(String imageUrl);
        public void showToast(int id);
        public boolean isConnected();
    }
}
