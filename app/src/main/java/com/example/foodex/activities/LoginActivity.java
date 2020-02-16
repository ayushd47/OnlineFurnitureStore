package com.example.foodex.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodex.R;
import com.example.foodex.activities.admin.AdminDashboardActivity;
import com.example.foodex.activities.user.DashboardActivity;
import com.example.foodex.api.Url;
import com.example.foodex.response.LoginResponse;
import com.example.foodex.utils.MySharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> loginUser());

        if (MySharedPreference.getInt(this, "userID") > 0) {
            if (MySharedPreference.getString(this, "userType").equals("admin")) {
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            }
            finish();
        }
    }

    private void loginUser() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Url.getEndPoints().login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().isStatus()) {
                    MySharedPreference.setInt(LoginActivity.this, "userID", response.body().getUserId());
                    MySharedPreference.setString(LoginActivity.this, "userType", response.body().getUserType());
                    if (response.body().getUserType().equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void openRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
