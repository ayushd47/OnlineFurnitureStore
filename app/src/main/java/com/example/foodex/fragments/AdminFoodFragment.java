package com.example.foodex.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.activities.admin.AddFurnitureActivity;
import com.example.foodex.adapters.AdminFurnitureListAdapter;
import com.example.foodex.api.Url;
import com.example.foodex.response.FoodResponse;
import com.example.foodex.utils.ShakeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.SENSOR_SERVICE;

public class AdminFoodFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddFood;
    private SensorManager manager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_furniture, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        fabAddFood = v.findViewById(R.id.fabAddFood);
        fabAddFood.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AddFurnitureActivity.class));
        });

        getFoodList();

        manager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> {
            getFoodList();
        });

        return v;
    }

    private void getFoodList() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Refreshing...");
        dialog.setCancelable(false);
        dialog.show();
        Url.getEndPoints().getFood().enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                if (response.body().isStatus()) {
                    AdminFurnitureListAdapter adapter = new AdminFurnitureListAdapter(getActivity(), response.body().getData());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        manager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}
