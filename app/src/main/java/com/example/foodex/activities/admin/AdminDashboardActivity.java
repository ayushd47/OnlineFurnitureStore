package com.example.foodex.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodex.R;
import com.example.foodex.activities.LoginActivity;
import com.example.foodex.fragments.AdminFoodFragment;
import com.example.foodex.fragments.AdminOrderFragment;
import com.example.foodex.utils.MySharedPreference;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private View navFood, navOrder, navLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvTitle = findViewById(R.id.tvTitle);
        drawerLayout = findViewById(R.id.drawerLayout);
        navFood = findViewById(R.id.navFood);
        navOrder = findViewById(R.id.navOrder);
        navLogout = findViewById(R.id.navLogout);

        navFood.setOnClickListener(this);
        navOrder.setOnClickListener(this);
        navLogout.setOnClickListener(this);

        showFragment(new AdminFoodFragment(), "Furniture List");
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void showFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

        tvTitle.setText(title);
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navFood:
                showFragment(new AdminFoodFragment(), "Furniture List");
                break;
            case R.id.navOrder:
                showFragment(new AdminOrderFragment(), "Order List");
                break;
            case R.id.navLogout:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                MySharedPreference.setInt(this, "userID", 0);
                break;
        }

    }
}
