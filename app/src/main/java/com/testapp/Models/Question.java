package com.testapp.Models;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String questionTitle, questionID, testID;

    private ArrayList<Answer> ANSWERS;

    public Question() {}

    public Question(String questionTitle, String questionID, String testID, ArrayList<Answer> ANSWERS) {
        this.questionTitle = questionTitle;
        this.questionID = questionID;
        this.testID = testID;
        this.ANSWERS = ANSWERS;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public ArrayList<Answer> getANSWERS() {
        return ANSWERS;
    }

    public void setANSWERS(ArrayList<Answer> ANSWERS) {
        this.ANSWERS = ANSWERS;
    }
}






