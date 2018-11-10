package com.example.vaibhav.newsfeeds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements GetRequestAsyncCall.IGetRequestAsyncCall {
    private Source SOURCE = null;
    AlertDialog.Builder builder = null;
    AlertDialog dialog = null;
    Object SystemService = null;
    ListView LIST_VIEW = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        SystemService = getSystemService(Context.CONNECTIVITY_SERVICE);
        LIST_VIEW = (ListView) findViewById(R.id.news_list_view_dynamic);
        try {
            if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                if (getIntent() != null) {
                    Log.d("Log: NewsActivity At:", "getIntent");
                    if (getIntent().getExtras() != null) {
                        builder.setTitle("Loading Stories....").setView(inflater.inflate(R.layout.activity_progress_loader, null)).setCancelable(false);
                        dialog = builder.create();
                        dialog.show();
                        Source source = (Source) getIntent().getExtras().getSerializable(IntentKeyAndCodes.PROGRESS_TO_NEWS_KEY);
                        setTitle(source.source_Name);

                        RequestParams params = new RequestParams();
                        params.addParams("sources", source.source_id);
                        params.addParams("apiKey", IntentKeyAndCodes.API_KEY);
                        new GetRequestAsyncCall(NewsActivity.this, params).execute(getString(R.string.news_api_url), source);
                    }
                }
                LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            String url = SOURCE.NewsFeeds.get(position).url;
                            if (!(url.equalsIgnoreCase("null") && url.equalsIgnoreCase("null"))) {
                                    Intent intent = new Intent(getString(R.string.web));
                                    intent.putExtra(IntentKeyAndCodes.WEB_VIEW_URl_KEY,url);
                                    startActivity(intent);
                            } else {
                                showToast(6);
                            }
                        }
                        else{
                            showToast(0);
                        }

                    }
                });
            } else {
                showToast(0);
            }

        } catch (Exception ex) {
            Log.e("error", ex.getMessage() + ex.getStackTrace());
            ToastMessage.showToast(2, getApplicationContext());
        }
    }

    private void setListView() {
        NewsAdapter adapter = new NewsAdapter(this, R.layout.news_view, SOURCE.NewsFeeds);
        LIST_VIEW.setAdapter(adapter);
    }

    public void startSourceActivity(ArrayList<Source> sources, boolean isForSourceVeiw, boolean dismissLoader) {
        try {
            if (!dismissLoader) {
                if (!isForSourceVeiw) {
                    SOURCE = sources.get(0);
                    setListView();
                }
            } else {

                showToast(5);
            }
        } catch (Exception ex) {
            ToastMessage.showToast(2, getApplicationContext());
            Log.e("error", ex.getMessage() + ex.getStackTrace());
        }
    }

    public void dismissProgress() {
        dialog.dismiss();
    }

    public Object getSystemService() {
        return SystemService;
    }

    public void showToast(int id) {
        ToastMessage.showToast(id, getApplicationContext());
    }
}
