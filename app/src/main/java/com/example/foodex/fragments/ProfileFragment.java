package com.example.foodex.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.example.foodex.R;
import com.example.foodex.api.Url;
import com.example.foodex.models.User;
import com.example.foodex.response.BasicResponse;
import com.example.foodex.response.UserResponse;
import com.example.foodex.utils.MySharedPreference;
import com.example.foodex.utils.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView imgProfile;
    private TextView tvFullName, tvEmail, tvPhone, tvAddress;
    private ProgressDialog dialog;
    private int id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvFullName = v.findViewById(R.id.tvFullName);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvPhone = v.findViewById(R.id.tvPhone);
        tvAddress = v.findViewById(R.id.tvAddress);
        imgProfile = v.findViewById(R.id.imgProfile);

        imgProfile.setOnClickListener(view -> {
            changeProfileImage();
        });

        loadProfile();

        return v;
    }

    private void loadProfile() {
        id = MySharedPreference.getInt(getActivity(), "userID");
        Url.getEndPoints().getProfile(id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().isStatus()) {
                    User user = response.body().getData();
                    tvFullName.setText(user.getFullName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                    tvAddress.setText(user.getAddress());

                    Utils.displayImage(imgProfile, Url.baseUrl + user.getProfileImage());
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgProfile.setImageURI(uri);
            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Uploading Profile Image...");
            dialog.show();
            uploadImage(getPath(uri)); //Uploading profile image
        }
    }

    public String getPath(Uri uri) {
        String[] projectile = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), uri, projectile, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void uploadImage(String imagePath) {
        File file = new File(imagePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profileImage", file.getName(), mFile);
        Url.getEndPoints().uploadImage(fileToUpload).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.body().isStatus()) {
                    updateProfile((String) response.body().getData());
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(String profileImage) {
        Url.getEndPoints().updateProfilePicture(profileImage, id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.body().isStatus()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
