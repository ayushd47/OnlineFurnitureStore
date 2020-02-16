package com.example.foodex.adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.Food;
import com.example.foodex.models.Order;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.utils.MySharedPreference;
import com.example.foodex.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFurnitureListAdapter extends RecyclerView.Adapter<UserFurnitureListAdapter.Holder> {

    private Context context;
    private List<Food> foodList;

    public UserFurnitureListAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvPrice.setText("Rs." + food.getPrice());
        Utils.displayImage(holder.imgFood, Url.baseUrl + food.getImage());
        holder.btnOrder.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_quantity);
            EditText etQuantity = dialog.findViewById(R.id.etQuantity);
            Button btnOrder = dialog.findViewById(R.id.btnAddQuantity);
            btnOrder.setText("Order");
            btnOrder.setOnClickListener(view1 -> {
                String quantity = etQuantity.getText().toString().trim();
                if (TextUtils.isEmpty(quantity)) {
                    etQuantity.setError("Please specify quantity.");
                    etQuantity.requestFocus();
                    return;
                }
                Order order = new Order(food.getId(), food.getName(), food.getImage(), MySharedPreference.getInt(context, "userID"), Integer.parseInt(quantity),"false");
                Url.getEndPoints().orderFood(order).enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView tvName, tvPrice;
        private Button btnOrder;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnOrder = itemView.findViewById(R.id.btnOrder);
        }
    }
}
