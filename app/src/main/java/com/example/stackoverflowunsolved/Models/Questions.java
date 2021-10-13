package com.example.stackoverflowunsolved.Models;

import java.util.ArrayList;

public class Questions {

    String profileImageURL, title, questionURL, dateCreated;
    ArrayList<String> tags;

    public Questions(){}

    public Questions(String profileImageURL, String title, String questionURL, String dateCreated, ArrayList<String> tags) {
        this.profileImageURL = profileImageURL;
        this.title = title;
        this.questionURL = questionURL;
        this.dateCreated = dateCreated;
        this.tags = tags;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionURL() {
        return questionURL;
    }

    public void setQuestionURL(String questionURL) {
        this.questionURL = questionURL;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
