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

import com.example.foodex.BILL.FurnitureBLL;
import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.Food;
import com.example.foodex.utils.Utils;

import java.util.List;

public class AdminFurnitureListAdapter extends RecyclerView.Adapter<AdminFurnitureListAdapter.Holder> {

    private Context context;
    private List<Food> foodList;

    public AdminFurnitureListAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_furniture_admin, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvPrice.setText("Rs." + food.getPrice());
        holder.btnDelete.setOnClickListener(v -> {
            FurnitureBLL bll = new FurnitureBLL(food.getId(), food.getName(), food.getCategory(), food.getPrice(), food.getImage());
            if (bll.deleteFood()) {
                Toast.makeText(context, "Food deleted success. Shake your device to refresh.", Toast.LENGTH_SHORT).show();
            }
        });
        Utils.displayImage(holder.imgFood, Url.baseUrl + food.getImage());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView tvName, tvPrice;
        private Button btnUpdate, btnDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
