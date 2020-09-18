package com.testapp.Models;

import java.util.List;

public class Test {

    private String testTitle, testID, userID, testPass, amountQuestions;
    private Boolean visible;

    public Test(){}

    public Test(String testTitle, String testID, String userID, String testPass, String amountQuestions, Boolean visible) {
        this.testTitle = testTitle;
        this.testID = testID;
        this.userID = userID;
        this.testPass = testPass;
        this.amountQuestions = amountQuestions;
        this.visible = visible;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTestPass() {
        return testPass;
    }

    public void setTestPass(String testPass) {
        this.testPass = testPass;
    }

    public String getAmountQuestions() {
        return amountQuestions;
    }

    public void setAmountQuestions(String amountQuestions) {
        this.amountQuestions = amountQuestions;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
