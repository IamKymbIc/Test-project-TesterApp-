package com.testapp.Models;

import java.util.ArrayList;

public class Tester {

    String questionTitle, answerTitle;
    Boolean success;
    ArrayList<Answer> answerList;

    public Tester(){}

    public Tester(String questionTitle, String answerTitle, Boolean success, ArrayList<Answer> answerList) {
        this.questionTitle = questionTitle;
        this.answerTitle = answerTitle;
        this.success = success;
        this.answerList = answerList;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<Answer> answerList) {
        this.answerList = answerList;
    }
}
