package com.testapp.Models;

import java.util.List;

public class Answer {

    String answerTitle, answerID, questionID;
    List list;
    Boolean success;

    public Answer (){}

    public Answer(String answerTitle, String answerID, String questionID, List list, Boolean success) {
        this.answerTitle = answerTitle;
        this.answerID = answerID;
        this.questionID = questionID;
        this.list = list;
        this.success = success;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

