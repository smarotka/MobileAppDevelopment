
package com.example.vaibhav.newsfeeds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetRequestAsyncCall.IGetRequestAsyncCall {
    AlertDialog.Builder builder = null;
    AlertDialog dialog = null;
    Object SystemService = null;
    ListView LIST_VIEW = null;

    private ArrayList<Source> SOURCES = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name_main));
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        SystemService = getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if(TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))){
                LIST_VIEW = (ListView) findViewById(R.id.sources_list_view_dynamic);
                builder.setTitle("Loading Sources...").setView(inflater.inflate(R.layout.activity_progress_loader, null)).setCancelable(false);
                dialog = builder.create();
                dialog.show();

                RequestParams params = new RequestParams();
                params.addParams("apiKey", IntentKeyAndCodes.API_KEY);
                new GetRequestAsyncCall(MainActivity.this, params).execute(getString(R.string.source_api_url));

                LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        if (TestConnection.isConnected(SystemService)) {

                            Intent intent = new Intent(getString(R.string.news_view));
                            intent.putExtra(IntentKeyAndCodes.PROGRESS_TO_NEWS_KEY,SOURCES.get(position));
                            startActivity(intent);

                        } else {
                            ToastMessage.showToast(0, getApplicationContext());
                        }
                    }
                });
            }
            else{
                ToastMessage.showToast(0, getApplicationContext());
            }

        }
        catch(Exception ex)
        {
            dismissProgress();
            Log.e("error",ex.getMessage()+ex.getStackTrace());
            ToastMessage.showToast(2,getApplicationContext());
        }
    }
    private  void setListView(){
        ArrayAdapter<Source> adapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1,android.R.id.text1,SOURCES);
        LIST_VIEW.setAdapter(adapter);
    }
    public void startSourceActivity(ArrayList<Source> sources,boolean isForSourceVeiw,boolean dismissLoader) {
        try {
            if (!dismissLoader) {
                if (isForSourceVeiw) {
                    SOURCES = sources;
                    setListView();
                }
            } else {
                showToast(5);
            }
        }
        catch(Exception ex)
        {
            ToastMessage.showToast(2,getApplicationContext());
            Log.e("error",ex.getMessage()+ex.getStackTrace());
        }
    }

    public void dismissProgress(){
        Log.d("demo","I am dismiss");
        dialog.dismiss();
    }

    public Object getSystemService() {
        return SystemService;
    }

    public void showToast(int id) {
        ToastMessage.showToast(id, getApplicationContext());
    }
}
