package com.example.vaibhav.newsfeeds;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class GetRequestAsyncCall extends AsyncTask<Object, Void, ArrayList<Source>> {
    IGetRequestAsyncCall iGetRequestAsyncCall;
    RequestParams requestParams = null;

    public GetRequestAsyncCall(IGetRequestAsyncCall iGetRequestAsyncCall, RequestParams params) {
        this.iGetRequestAsyncCall = iGetRequestAsyncCall;
        this.requestParams = params;
    }

    @Override
    protected void onPostExecute(ArrayList<Source> source) {
        super.onPostExecute(source);
        if (source.size() > 0) {
            if (source.get(0).NewsFeeds != null) {
                iGetRequestAsyncCall.startSourceActivity(source, false, false);
                iGetRequestAsyncCall.dismissProgress();
            } else {
                iGetRequestAsyncCall.startSourceActivity(source, true,false);
                iGetRequestAsyncCall.dismissProgress();
            }
        } else {
            iGetRequestAsyncCall.dismissProgress();
        }
    }

    @Override
    protected ArrayList<Source> doInBackground(Object... params) {
        Log.d("Demo", "doInBackground");
        HttpURLConnection connection = null;
        ArrayList<Source> result = null;
        try {
            URL url = new URL(requestParams.getEncodedUrl(params[0].toString()));
            ;
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (TestConnection.isConnected(iGetRequestAsyncCall.getSystemService()) && connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                result = new ArrayList<>();
                result = params.length > 1 ? getObjectArrayFromJson(root, params[1]) : getObjectArrayFromJson(root);
            } else {
                iGetRequestAsyncCall.showToast(0);
            }
            Log.d("demo", "END: doInBackground ");
        } catch (MalformedURLException e) {
            iGetRequestAsyncCall.dismissProgress();
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
            iGetRequestAsyncCall.dismissProgress();
        } catch (Exception e) {
            iGetRequestAsyncCall.dismissProgress();
            Log.e("Error", e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }


    private ArrayList<Source> getObjectArrayFromJson(JSONObject jsonObject, Object... params) {
        ArrayList<Source> result = new ArrayList<>();
        try {
            JSONArray sourcesArray = jsonObject.getJSONArray("sources");

            if (sourcesArray.length() > 0) {
                for (int i = 0; i < sourcesArray.length(); i++) {
                    try {
                        JSONObject articleJson = sourcesArray.getJSONObject(i);
                        Source source = new Source();
                        source.source_id = articleJson.getString("id");
                        source.source_Name = articleJson.getString("name");
                        result.add(source);
                    } catch (Exception ex) {
                        iGetRequestAsyncCall.showToast(4);
                    }
                }
            }
            return result;
        } catch (JSONException ex) {
            JSONArray newsArray = null;
            try {
                newsArray = jsonObject.getJSONArray("articles");
                Source sourceDetails = new Source();
                sourceDetails.NewsFeeds = new ArrayList<News>();
                sourceDetails.source_id = ((Source) params[0]).source_id;
                sourceDetails.source_Name = ((Source) params[0]).source_Name;
                for (int i = 0; i < newsArray.length(); i++) {
                    try {
                        JSONObject newsJson = newsArray.getJSONObject(i);
                        final News news = new News();
                        news.author = newsJson.getString("author");
                        news.publishedAt = newsJson.getString("publishedAt");
                        news.title = newsJson.getString("title");
                        news.url = newsJson.getString("url");
                        news.urlToImage = newsJson.getString("urlToImage");
                        sourceDetails.NewsFeeds.add(news);
                    } catch (Exception exe) {
                        Log.e("error", exe.getMessage() + exe.getStackTrace());
                        iGetRequestAsyncCall.showToast(4);
                    }
                }
                result.add(sourceDetails);
            } catch (JSONException e) {

                e.printStackTrace();
                iGetRequestAsyncCall.startSourceActivity(new ArrayList<Source>(), true,true);
            }
            return result;
        }

    }

    public static interface IGetRequestAsyncCall {
        public Object getSystemService();

        public void showToast(int id);

        public void dismissProgress();

        public void startSourceActivity(ArrayList<Source> sources, boolean isForSourceView,boolean dismissLoader);
    }
}
