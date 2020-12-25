package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.UpdateProfileDosenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileDosenActivity extends AppCompatActivity {

    SharedPreferences loginPreferences;
    private Button editProfile;

    private TextView changePassword;
    private TextInputEditText namaUserTextField;
    private TextInputEditText nipUserTextField;
    private TextInputEditText usernameTextField;
    private TextInputEditText passwordTextField;

    private String namaUser;
    private String nipUser;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_dosen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaUserTextField = findViewById(R.id.edit_nama_text_field);
        nipUserTextField = findViewById(R.id.edit_noinduk_text_field);
        usernameTextField = findViewById(R.id.edit_dosen_username_text_field);
        passwordTextField = findViewById(R.id.edit_password_text_field);
        changePassword = findViewById(R.id.change_password_text);
        editProfile = findViewById(R.id.update_profile_dosen_button);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordActivity = new Intent(getApplicationContext(), ChangePasswordDosenActivity.class);
                startActivity(changePasswordActivity);
                // TODO tambahin validasi untuk form nya
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDosen();
            }
        });


        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        nipUser = loginPreferences.getString("nip", "kosong");
        username = loginPreferences.getString("username", "kosong");

        namaUserTextField.setText(namaUser);
        nipUserTextField.setText(nipUser);
        usernameTextField.setText(username);
    }

    public void editProfileDosen() {
        BaseApi editDosenProfile = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<UpdateProfileDosenResponse> updateProfileDosenResponseCall = editDosenProfile.editProfileDosen(username, namaUserTextField.getText().toString(),
                nipUserTextField.getText().toString(), usernameTextField.getText().toString(), passwordTextField.getText().toString());
        updateProfileDosenResponseCall.enqueue(new Callback<UpdateProfileDosenResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileDosenResponse> call, Response<UpdateProfileDosenResponse> response) {
                if(response.body().getMessage() != null ) {
                    if (response.body().getMessage().equals("Data berhasil di Update")) {
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        String nama = response.body().getData().get(0).getNama();
                        String username = response.body().getData().get(0).getUsername();
                        String nip = response.body().getData().get(0).getNip();
                        editor.putString("nama", nama);
                        editor.putString("username", username);
                        editor.putString("nip", nip);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), String.format("Profile berhasil di update!"), Toast.LENGTH_SHORT).show();
                        setResult(2);
                        finish();

                        //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                    } else {
                        Toast.makeText(getApplicationContext(), "Profile gagal di update silahkan cek password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileDosenResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Profile gagal di update", Toast.LENGTH_SHORT).show();
            }
        });
    }


}