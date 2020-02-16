package com.example.foodex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.Order;
import com.example.foodex.utils.Utils;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.Holder> {

    private Context context;
    private List<Order> orderList;

    public AdminOrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order_admin, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Order order = orderList.get(position);
        holder.tvName.setText(order.getFoodName());
        holder.tvQuantity.setText("Quantity: " + order.getQuantity());
        Utils.displayImage(holder.imgFood, Url.baseUrl + order.getFoodImage());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView tvName, tvQuantity;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
