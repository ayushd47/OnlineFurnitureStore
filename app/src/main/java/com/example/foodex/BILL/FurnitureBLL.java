package com.example.foodex.BILL;

import com.example.foodex.api.Url;
import com.example.foodex.models.Food;
import com.example.foodex.response.BasicResponse;

import java.io.IOException;

import retrofit2.Response;

public class FurnitureBLL {
    private int id;
    private String name;
    private String category;
    private int price;
    private String image;

    public FurnitureBLL(int id, String name, String category, int price, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
    }

    public boolean addFood() {
        boolean isSuccess = false;
        Food food = new Food(name, category, price, image);
        try {
            Response<BasicResponse> response = Url.getEndPoints().addFood(food).execute();
            if (response.body().isStatus()) {
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean updateFood() {
        boolean isSuccess = false;
        Food food = new Food(name, category, price, image);
        try {
            Response<BasicResponse> response = Url.getEndPoints().updateFood(id, food).execute();
            if (response.body().isStatus()) {
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteFood() {
        boolean isSuccess = false;
        try {
            Response<BasicResponse> response = Url.getEndPoints().deleteFood(id).execute();
            if (response.body().isStatus()) {
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
