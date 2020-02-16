package com.example.foodex.activities.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.adapters.UserFurnitureListAdapter;
import com.example.foodex.api.Url;
import com.example.foodex.response.FoodResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFurinitureListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        category = getIntent().getStringExtra("category");

        getFoodList();
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    private void getFoodList() {
        Url.getEndPoints().getFoodByCategory(category).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.body().isStatus()) {
                    UserFurnitureListAdapter adapter = new UserFurnitureListAdapter(UserFurinitureListActivity.this, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(UserFurinitureListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(UserFurinitureListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
