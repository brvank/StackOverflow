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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.stackoverflowunsolved.Models.Questions;
import com.example.stackoverflowunsolved.Models.QuestionsAdapter;
import com.example.stackoverflowunsolved.Models.QuestionsViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    public static Context context;

    private QuestionsViewModel questionsViewModel;
    private RequestQueue requestQueue;

    //recycler view stuff
    private ArrayList<Questions> questions;
    private RecyclerView rvQuestions;
    private QuestionsAdapter questionsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //swipe refresh layout
    SwipeRefreshLayout swipeRefreshLayout;

    //menu
    MenuInflater menuInflater;

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
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        //recycler view
        rvQuestions = findViewById(R.id.rv_questions);
        layoutManager = new LinearLayoutManager(this);
        rvQuestions.setLayoutManager(layoutManager);

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
                //refreshing enabled
                swipeRefreshLayout.setRefreshing(true);

                loadData();
            }
        });

        loadData();

    }

    private void loadData() {
        if(!connected()){
            Toast.makeText(QuestionsActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            return ;
        }
        //enabling loading dialog
        loadingDialog.startLoadingDialog();

        questionsViewModel.refreshData();
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
                loadData();
        }
        return super.onOptionsItemSelected(item);
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