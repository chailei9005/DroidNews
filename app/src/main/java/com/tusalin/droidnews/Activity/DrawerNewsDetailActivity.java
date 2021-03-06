package com.tusalin.droidnews.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.tusalin.droidnews.Bean.NewsInfo;
import com.tusalin.innews.R;

/**
 * Created by tusalin on 9/7/2016.
 */

public class DrawerNewsDetailActivity extends AppCompatActivity {

    private NewsInfo newsInfo;
    private Toolbar detailToolbar;
    private CollapsingToolbarLayout collapsingToobar;;
    private ImageView detailImage;
    private ProgressBar progressBar;
    private WebView webview;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            newsInfo = (NewsInfo) bundle.getSerializable("newsInfo");
        }
        detailToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailImage = (ImageView) findViewById(R.id.detail_image);
        if (newsInfo!=null){
            Picasso.with(this)
                    .load(newsInfo.getNewsImageUrl())
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(detailImage);
        }
        collapsingToobar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToobar.setTitle("Jiemian");
        collapsingToobar.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        collapsingToobar.setExpandedTitleColor(getResources().getColor(R.color.purple));
        collapsingToobar.setCollapsedTitleTextColor(getResources().getColor(R.color.skyblue));
        progressBar = (ProgressBar) findViewById(R.id.detail_progressbar);
        webview = (WebView) findViewById(R.id.detail_webview);
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        if (newsInfo!=null){
            webview.loadUrl(newsInfo.getNewsUrl());
        }


        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                webview.setVisibility(View.GONE);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsInfo.getNewsUrl()));
                startActivity(intent);
            }
        });
    }

    public class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            webview.loadUrl(newsInfo.getNewsUrl());
            return true;
        }
    }

    @Override
    public void onBackPressed() {

        if (webview.canGoBack()){
            webview.goBack();
            return;
        }
        webview.setVisibility(View.GONE);
        super.onBackPressed();
    }

}
