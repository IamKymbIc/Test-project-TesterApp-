package com.testapp.Models;

public class CheckedAnswer {

    Boolean checkAns;

    public CheckedAnswer(){}

    public CheckedAnswer(Boolean checkAns) {
        this.checkAns = checkAns;
    }

    public Boolean getCheckAns() {
        return checkAns;
    }

    public void setCheckAns(Boolean checkAns) {
        this.checkAns = checkAns;
    }
}
