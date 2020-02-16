package com.example.foodex.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.User;
import com.example.foodex.response.BasicResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private EditText etFullName, etEmail, etPassword, etPhone, etAddress;
    private CircleImageView imgProfile;
    private Button btnRegister;
    private boolean isProfileImageSelected = false;
    private String imagePath;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        imgProfile = findViewById(R.id.imgProfile);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> uploadProfileImage());
    }

    private void uploadProfileImage() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        File file = new File(imagePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part profileImage = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile);

        Url.getEndPoints().uploadImage(profileImage).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        register((String) response.body().getData());
                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Profile Image Uploading Failed.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void register(String profileImage) {
        if (!validate()) return;
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();

        User user = new User(fullName, email, password, phone, address, profileImage, "user");

        Url.getEndPoints().register(user).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(RegisterActivity.this, "Register Success.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void openLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public boolean validate() {
        if (TextUtils.isEmpty(etFullName.getText().toString())) {
            etFullName.setError("Please enter full name.");
            etFullName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Please enter email.");
            etEmail.requestFocus();
            return false;
        } else if (!etEmail.getText().toString().contains("@")) {
            etEmail.setError("Please enter valid email.");
            etEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Please enter password.");
            etPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Please enter phone number.");
            etPhone.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etAddress.getText().toString())) {
            etAddress.setError("Please enter address.");
            etAddress.requestFocus();
            return false;
        } else if (!isProfileImageSelected) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please upload profile picture.");
            dialog.setPositiveButton("OK", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            dialog.show();
            return false;
        }
        return true;
    }

    public void openCamera(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                isProfileImageSelected = true;
                imgProfile.setImageBitmap(bitmap);
                imagePath = getPath(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projectile = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projectile, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
}