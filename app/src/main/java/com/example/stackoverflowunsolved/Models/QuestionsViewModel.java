package com.example.stackoverflowunsolved.Models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stackoverflowunsolved.QuestionsActivity;
import com.example.stackoverflowunsolved.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionsViewModel extends ViewModel{

    private String mainURL = "https://api.stackexchange.com/2.2/questions/no-answers?order=desc&sort=activity&site=stackoverflow";

    private MutableLiveData<ArrayList<Questions>> questionsMutableLiveData;
    private ArrayList<Questions> questions;

    private RequestQueue requestQueue;

    public QuestionsViewModel(){
        questionsMutableLiveData = new MutableLiveData<>();
        requestQueue = VolleySingleton.getInstance(QuestionsActivity.context).getRequestQueue();
        //loadData(requestQueue);
    }

    public MutableLiveData<ArrayList<Questions>> getQuestionsMutableLiveData(){
        return questionsMutableLiveData;
    }

    //function to load data using the main url
    public void loadData(RequestQueue rq){
        //this.requestQueue = rq;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mainURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    createQuestionsArrayList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void createQuestionsArrayList(JSONObject response) throws JSONException {

        //temporary json object to get each json object from the response to create question object --> 1
        JSONObject tempQuestion;

        //getting the required json array from the response
        JSONArray questionsArray = response.getJSONArray("items");

        //setting the questions list array
        questions = new ArrayList<>();

        //creating and inserting question objects into questions array list
        for(int i=0;i<questionsArray.length();i++){
            //creating a question object to load each question returned from the API into the questions array list
            Questions question = new Questions();

            //go to comment 1
            tempQuestion = questionsArray.getJSONObject(i);

            //setting the values for the question
            question.setTitle(tempQuestion.getString("title"));
            question.setDateCreated(tempQuestion.getString("creation_date"));
            question.setProfileImageURL(tempQuestion.getJSONObject("owner").getString("profile_image"));
            question.setQuestionURL(tempQuestion.getString("link"));

            //temporary json array to set the tags
            JSONArray tempTags = tempQuestion.getJSONArray("tags");
            ArrayList<String> tags = new ArrayList<>();
            for(int j=0;j<tempTags.length();j++){
                tags.add(tempTags.getString(j));
            }
            question.setTags(tags);

            //adding the question into the questions array
            questions.add(question);
        }
        
        //setting the value of mutable live data
        questionsMutableLiveData.postValue(questions);
    }

    public void refreshData(){
        loadData(requestQueue);
    }

}
