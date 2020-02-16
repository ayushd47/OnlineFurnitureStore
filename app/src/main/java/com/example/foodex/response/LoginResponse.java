package com.example.foodex.response;

public class LoginResponse {
    private boolean status;
    private String message;
    private int userId;
    private String userType;

    public LoginResponse(boolean status, String message, int userId, String userType) {
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.userType = userType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
