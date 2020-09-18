package com.testapp.Models;

public class SettingsModel {

    private String  testPass, amountQuestions;
    private Boolean visible;

    public SettingsModel(){}

    public SettingsModel(String testPass, String amountQuestions, Boolean visible) {
        this.testPass = testPass;
        this.amountQuestions = amountQuestions;
        this.visible = visible;
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
