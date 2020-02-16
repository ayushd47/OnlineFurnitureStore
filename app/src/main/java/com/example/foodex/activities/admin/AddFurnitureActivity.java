package com.example.foodex.activities.admin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.Food;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.utils.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFurnitureActivity extends AppCompatActivity {

    private EditText etName, etPrice;
    private Spinner spnCategory;
    private ImageView imgFood;
    private Button btnAdd;
    private String imagePath = "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furiniture);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        spnCategory = findViewById(R.id.spnCategory);
        imgFood = findViewById(R.id.imgFood);
        btnAdd = findViewById(R.id.btnAdd);

        imgFood.setOnClickListener(view -> {
            selectFoodImage();
        });
        btnAdd.setOnClickListener(view -> {
            addFood();
        });
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    private void selectFoodImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void addFood() {
        if (!validate()) return;
        File file = new File(imagePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("foodImage", file.getName(), mFile);

        try {
            Utils.strictMode();
            String foodImage = (String) Url.getEndPoints().uploadFoodImage(fileToUpload).execute().body().getData();
            String name = etName.getText().toString();
            String category = spnCategory.getSelectedItem().toString();
            String price = etPrice.getText().toString();
            Food food = new Food(name, category, Integer.parseInt(price), foodImage);
            Url.getEndPoints().addFood(food).enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    if (response.body().isStatus()) {
                        finish();
                    } else {
                        Toast.makeText(AddFurnitureActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {
                    Toast.makeText(AddFurnitureActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgFood.setImageURI(uri);
            imagePath = getPath(uri);
        }
    }

    public String getPath(Uri uri) {
        String[] projectile = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, projectile, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private boolean validate() {
        if (imagePath.equals("")) {
            Toast.makeText(this, "Please select food image.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError("Name is required.");
            etName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPrice.getText().toString())) {
            etPrice.setError("Price is required.");
            etPrice.requestFocus();
            return false;
        }
        return true;
    }
}
