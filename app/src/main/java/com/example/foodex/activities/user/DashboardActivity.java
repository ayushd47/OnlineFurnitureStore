package com.example.foodex.activities.user;

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
import com.example.foodex.fragments.HomeFragment;
import com.example.foodex.fragments.OrderFragment;
import com.example.foodex.fragments.ProfileFragment;
import com.example.foodex.utils.MySharedPreference;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private View navHome, navProfile, navOrder, navLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvTitle = findViewById(R.id.tvTitle);
        drawerLayout = findViewById(R.id.drawerLayout);
        navHome = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
        navOrder = findViewById(R.id.navOrder);
        navLogout = findViewById(R.id.navLogout);

        navHome.setOnClickListener(this);
        navProfile.setOnClickListener(this);
        navOrder.setOnClickListener(this);
        navLogout.setOnClickListener(this);

        showFragment(new HomeFragment(), "Dashboard");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

        tvTitle.setText(title);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navHome:
                showFragment(new HomeFragment(), "Dashboard");
                break;
            case R.id.navProfile:
                showFragment(new ProfileFragment(), "My Profile");
                break;
            case R.id.navOrder:
                showFragment(new OrderFragment(), "My Order");
                break;
            case R.id.navLogout:
                MySharedPreference.setInt(this, "userID", 0);
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
