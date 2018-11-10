package com.example.vaibhav.newsfeeds;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    webView = (WebView) findViewById(R.id.webView);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(getIntent().getExtras().getSerializable(IntentKeyAndCodes.WEB_VIEW_URl_KEY).toString());
                }
            }
        }
        else{
            ToastMessage.showToast(0,getApplicationContext());
        }
    }
}
