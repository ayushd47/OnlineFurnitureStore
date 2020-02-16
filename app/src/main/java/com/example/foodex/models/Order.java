package com.example.foodex.models;

public class Order {
    private int id;
    private int foodID;
    private String foodName;
    private String foodImage;
    private int userID;
    private int quantity;
    private String isDelivered;

    public Order(int foodID, String foodName, String foodImage, int userID, int quantity, String isDelivered) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.userID = userID;
        this.quantity = quantity;
        this.isDelivered = isDelivered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(String isDelivered) {
        this.isDelivered = isDelivered;
    }
}
