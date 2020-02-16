package com.example.foodex.BILL;

import com.example.foodex.api.Url;
import com.example.foodex.models.User;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.response.LoginResponse;

import java.io.IOException;

import retrofit2.Response;

public class UserBLL {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String profileImage;
    private String userType;

    public UserBLL(String fullName, String email, String password, String phone, String address, String profileImage, String userType) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
        this.userType = userType;
    }

    public UserBLL(String email, String password){
        this.email = email;
        this.password = password;
    }

    public boolean loginUser() {
        boolean isStatus = false;
        try {
            Response<LoginResponse> response = Url.getEndPoints().login(email, password).execute();
            if (response.body().isStatus()) {
                isStatus = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isStatus;
    }

    public boolean registerUser() {
        boolean isStatus = false;
        User user = new User(fullName, email, password, phone, address, profileImage, userType);
        try {
            Response<BasicResponse> response = Url.getEndPoints().register(user).execute();
            if (response.body().isStatus()) {
                isStatus = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isStatus;
    }
}
