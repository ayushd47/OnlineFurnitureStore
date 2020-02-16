package com.example.foodex.api;

import com.example.foodex.models.Food;
import com.example.foodex.models.Order;
import com.example.foodex.models.User;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.response.FoodResponse;
import com.example.foodex.response.LoginResponse;
import com.example.foodex.response.OrderResponse;
import com.example.foodex.response.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EndPoints {

    @POST("user/register")
    Call<BasicResponse> register(@Body User user);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("user/{id}")
    Call<UserResponse> getProfile(@Path("id") int id);

    @Multipart
    @POST("user/upload")
    Call<BasicResponse> uploadImage(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @PUT("user/upload/change/{id}")
    Call<BasicResponse> updateProfilePicture(@Field("profileImage") String profileImage, @Path("id") int id);

    @Multipart
    @POST("food/upload")
    Call<BasicResponse> uploadFoodImage(@Part MultipartBody.Part file);

    @POST("food")
    Call<BasicResponse> addFood(@Body Food food);

    @GET("food")
    Call<FoodResponse> getFood();

    @PUT("food/{id}")
    Call<BasicResponse> updateFood(@Path("id")int id, @Body Food food);

    @DELETE("food/{id}")
    Call<BasicResponse> deleteFood(@Path("id")int id);

    @GET("food/{category}")
    Call<FoodResponse> getFoodByCategory(@Path("category") String category);

    @POST("order")
    Call<BasicResponse> orderFood(@Body Order order);

    @GET("order")
    Call<OrderResponse> getAllOrder();

    @GET("order/{id}")
    Call<OrderResponse> getMyOrder(@Path("id") int userID);

    @DELETE("order/{id}")
    Call<BasicResponse> deleteOrder(@Path("id") int id);
}
