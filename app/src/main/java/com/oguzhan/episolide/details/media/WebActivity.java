package com.oguzhan.episolide.details.media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.oguzhan.episolide.R;

public class WebActivity extends AppCompatActivity
{

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_web_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.webview_progress_bar);

        webView.getSettings().setJavaScriptEnabled(false);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient()
        {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            public void onPageFinished(WebView view, String url)
            {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        String url = getIntent().getStringExtra(MediaDetailActivity.WEB_ACTIVITY_INTENT_TAG);
        webView.loadUrl(url);
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}