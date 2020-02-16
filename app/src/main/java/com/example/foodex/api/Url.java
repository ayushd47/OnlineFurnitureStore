package com.example.foodex.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Url {
    private static Retrofit retrofit;
//    public static String baseUrl = "http://192.168.1.95:3000/";
    public static String baseUrl = "http://10.0.2.2:3000/";

    public static EndPoints getEndPoints() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(EndPoints.class);
    }
}