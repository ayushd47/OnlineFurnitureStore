package com.example.foodex.fragments;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.adapters.AdminOrderAdapter;
import com.example.foodex.api.Url;
import com.example.foodex.response.OrderResponse;
import com.example.foodex.utils.ShakeDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.SENSOR_SERVICE;

public class AdminOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private SensorManager manager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_order, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllOrder();

        manager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> {
            getAllOrder();
        });

        return v;
    }

    private void getAllOrder() {
        Url.getEndPoints().getAllOrder().enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body().isStatus()) {
                    AdminOrderAdapter adapter = new AdminOrderAdapter(getActivity(), response.body().getData());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {

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
