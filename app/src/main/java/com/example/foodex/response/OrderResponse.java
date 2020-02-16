package com.example.foodex.response;

import com.example.foodex.models.Order;

import java.util.List;

public class OrderResponse {
    private boolean status;
    private String message;
    private List<Order> data;

    public OrderResponse(boolean status, String message, List<Order> data) {
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

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
