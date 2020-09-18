package com.testapp.Models;

public class User {
     String userName, userEmail, userPass, userStatus, userID;
     Boolean userRole;

    public User(String userName, String userEmail, String userPass, String userStatus, String userID, Boolean userRole) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userStatus = userStatus;
        this.userID = userID;
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Boolean getUserRole() {
        return userRole;
    }

    public void setUserRole(Boolean userRole) {
        this.userRole = userRole;
    }

    public User(){}
}
