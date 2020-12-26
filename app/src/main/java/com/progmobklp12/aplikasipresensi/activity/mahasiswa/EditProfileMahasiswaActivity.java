package com.progmobklp12.aplikasipresensi.activity.mahasiswa;

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
import com.progmobklp12.aplikasipresensi.activity.dosen.ChangePasswordDosenActivity;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.UpdateProfileDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.UpdateProfileMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileMahasiswaActivity extends AppCompatActivity {

    private Button editProfile;
    SharedPreferences loginPreferences;

    private TextView changePassword;
    private TextInputEditText namaUserTextField;
    private TextInputEditText nimUserTextField;
    private TextInputEditText usernameTextField;
    private TextInputEditText passwordTextField;

    private String namaUser;
    private String nimUser;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_mahasiswa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Update profile");

        namaUserTextField = findViewById(R.id.edit_mahasiswa_nama_text_field);
        nimUserTextField = findViewById(R.id.edit_mahasiswa_noinduk_text_field);
        usernameTextField = findViewById(R.id.edit_mahasiswa_username_text_field);
        passwordTextField = findViewById(R.id.mahasiswa_password_text_field);
        changePassword = findViewById(R.id.update_password_mahasiswa_text);
        editProfile = findViewById(R.id.update_profile_mahasiwa_button);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileMahasiswa();
            }
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordActivity = new Intent(getApplicationContext(), ChangePasswordMahasiswaActivity.class);
                startActivity(changePasswordActivity);
            }
        });


        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        nimUser = loginPreferences.getString("nim", "kosong");
        username = loginPreferences.getString("username", "kosong");

        namaUserTextField.setText(namaUser);
        nimUserTextField.setText(nimUser);
        usernameTextField.setText(username);
    }

    public void editProfileMahasiswa() {
        BaseApi editMahasiswaProfile = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<UpdateProfileMahasiswaResponse> updateProfileMahasiswaResponseCall = editMahasiswaProfile.editProfileMahasiswa(username, namaUserTextField.getText().toString(),
                nimUserTextField.getText().toString(), usernameTextField.getText().toString(), passwordTextField.getText().toString());
        updateProfileMahasiswaResponseCall.enqueue(new Callback<UpdateProfileMahasiswaResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileMahasiswaResponse> call, Response<UpdateProfileMahasiswaResponse> response) {
                if(response.body().getMessage() != null ) {
                    if (response.body().getMessage().equals("Data berhasil di Update")) {
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        String nama = response.body().getData().get(0).getNama();
                        String username = response.body().getData().get(0).getUsername();
                        String nim = response.body().getData().get(0).getNim();
                        editor.putString("nama", nama);
                        editor.putString("username", username);
                        editor.putString("nim", nim);
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
            public void onFailure(Call<UpdateProfileMahasiswaResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Profile gagal di update", Toast.LENGTH_SHORT).show();
            }
        });
    }

}