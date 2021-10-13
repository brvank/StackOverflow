package com.example.stackoverflowunsolved;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailsWebViewActivity extends AppCompatActivity {

    private WebView wvQuestion;
    private ImageView ivError;
    private String questionURL;

    //menu
    MenuInflater menuInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_web_view);

        //setting the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //receiving the intent
        Intent intent = getIntent();
        questionURL = intent.getStringExtra("questionURL");

        //initialising the views
        wvQuestion = findViewById(R.id.wv_question_web);
        ivError = findViewById(R.id.iv_error_web);

        //loading the url for webview
        wvQuestion.getSettings().setJavaScriptEnabled(true);
        wvQuestion.setWebViewClient(new WebClient());
        wvQuestion.loadUrl(questionURL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                wvQuestion.loadUrl(wvQuestion.getUrl());
        }
        return super.onOptionsItemSelected(item);
    }

    public class WebClient extends android.webkit.WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wvQuestion.canGoBack()) {
                        wvQuestion.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}