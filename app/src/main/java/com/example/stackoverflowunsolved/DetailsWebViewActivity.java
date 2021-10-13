package com.example.stackoverflowunsolved;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.android.material.snackbar.Snackbar;

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
                if(connected()) Toast.makeText(DetailsWebViewActivity.this, "Reloading...", Toast.LENGTH_SHORT).show();
                else Toast.makeText(DetailsWebViewActivity.this, "Connect to Internet!", Toast.LENGTH_SHORT).show();
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

    //function for checking internet connection
    private boolean connected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            //not connected to the internet
            connected = false;
        }
        return connected;
    }
}