package com.example.foodex.response;

import com.example.foodex.models.Food;

import java.util.List;

public class FoodResponse {
    private boolean status;
    private String message;
    private List<Food> data;

    public FoodResponse(boolean status, String message, List<Food> data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public List<Food> getData() {
        return data;
    }

    public void setData(List<Food> data) {
        this.data = data;
    }
}
