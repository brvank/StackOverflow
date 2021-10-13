package com.example.stackoverflowunsolved;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.stackoverflowunsolved.Models.Questions;
import com.example.stackoverflowunsolved.Models.QuestionsAdapter;
import com.example.stackoverflowunsolved.Models.QuestionsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    public static Context context;

    private QuestionsViewModel questionsViewModel;

    //recycler view stuff
    private ArrayList<Questions> questions;
    private RecyclerView rvQuestions;
    private QuestionsAdapter questionsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Floating action button
    FloatingActionButton fbReloadQuestions;

    //swipe refresh layout
    SwipeRefreshLayout swipeRefreshLayout;

    //loading dialog
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //initialising the context
        context = this;

        //initialising the objects
        questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);

        //recycler view
        rvQuestions = findViewById(R.id.rv_questions);
        layoutManager = new LinearLayoutManager(this);
        rvQuestions.setLayoutManager(layoutManager);

        //floating action button
        fbReloadQuestions = findViewById(R.id.fb_reload_questions);
        fbReloadQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        questions = new ArrayList<>();
        questionsAdapter = new QuestionsAdapter(questions,getApplicationContext());
        rvQuestions.setAdapter(questionsAdapter);

        //loading dialog
        loadingDialog = new LoadingDialog(QuestionsActivity.this);

        questionsViewModel.getQuestionsMutableLiveData().observe(this, new Observer<ArrayList<Questions>>() {
            @Override
            public void onChanged(ArrayList<Questions> questions) {
                refreshQuestions(questions);
            }
        });

        //for swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.srl_questions);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

    }

    private void loadData() {
        if(!connected()){

            //making swipe refreshing off
            swipeRefreshLayout.setRefreshing(false);

            //showing snackbar for five seconds to the user to reconnect to the internet and load again
            Snackbar.make(findViewById(R.id.parent_questions), "No Internet Connection! ", 5000)
                    .setAction("Try Again!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadData();
                        }
                    })
                    .show();
            return ;
        }
        //enabling loading dialog
        loadingDialog.startLoadingDialog();

        questionsViewModel.refreshData();
    }

    private void refreshQuestions(ArrayList<Questions> tempList){

        //making swipe refreshing off
        swipeRefreshLayout.setRefreshing(false);

        //disabling the loading dialog
        loadingDialog.dismissDialog();

        questions.clear();
        for(int i=0;i<tempList.size();i++){
            questions.add(tempList.get(i));
        }

        //notifying the data changes
        questionsAdapter.notifyDataSetChanged();

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