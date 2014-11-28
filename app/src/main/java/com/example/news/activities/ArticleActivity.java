package com.example.news.activities;

import com.example.news.R;
import com.pkmmte.pkrss.Article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class ArticleActivity extends BaseActivity {
    private static final String EXTRA = "ArticleActivity:item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Article item = getIntent().getParcelableExtra(EXTRA);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        ViewCompat.setTransitionName(relativeLayout, EXTRA);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(item.getSource().toString());

        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_article;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(BaseActivity activity, View transitionView, Article url) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA);
        Intent intent = new Intent(activity, ArticleActivity.class);
        intent.putExtra(EXTRA, url);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
