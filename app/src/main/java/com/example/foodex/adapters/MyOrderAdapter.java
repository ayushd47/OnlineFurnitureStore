package com.example.foodex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.Order;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Holder> {

    private Context context;
    private List<Order> orderList;

    public MyOrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Order order = orderList.get(position);
        holder.tvName.setText(order.getFoodName());
        holder.tvQuantity.setText("Quantity: " + order.getQuantity());
        Utils.displayImage(holder.imgFood, Url.baseUrl + order.getFoodImage());
        holder.btnCancelOrder.setOnClickListener(view -> {
            Url.getEndPoints().deleteOrder(order.getId()).enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView tvName, tvQuantity;
        private Button btnCancelOrder;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}
