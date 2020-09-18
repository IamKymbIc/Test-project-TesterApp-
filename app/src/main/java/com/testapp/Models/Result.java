package com.testapp.Models;

public class Result {

    String resultID,userID, testID,score, percent;

    public Result (){}

    public Result(String resultID, String userID, String testID, String score, String percent) {
        this.resultID = resultID;
        this.userID = userID;
        this.testID = testID;
        this.score = score;
        this.percent = percent;
    }

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
