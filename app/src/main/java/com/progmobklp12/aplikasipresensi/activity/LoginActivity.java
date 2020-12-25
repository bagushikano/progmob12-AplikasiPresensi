package com.progmobklp12.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.LoginDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.LoginMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText username;
    private TextInputEditText password;
    private TextView register;
    private String namaUser;
    private int loginStatus;

    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        loginStatus = loginPreferences.getInt("login_status", 0);
        namaUser = loginPreferences.getString("nama", "kosong");

        /*
            login status = 0 -> nggak login
            login status = 1 -> dosen
            login status = 2 -> mahasiswa
         */
        if (loginStatus == 1) { //dosen
            Toast.makeText(getApplicationContext(), String.format("Login berhasil, selamat datang %1s", namaUser), Toast.LENGTH_SHORT).show();
            Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeActivity);
            finish();
        }

        else if (loginStatus == 2) { //mahasiswa

        }

        String[] loginRole = new String[] {getString(R.string.role_login_dropdown_mahasiswa), getString(R.string.login_role_dropdown_dosen)};
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.login_role_dropdown_menu_item, loginRole);
        AutoCompleteTextView loginRoleDropdown = findViewById(R.id.login_role_dropdown);
        loginRoleDropdown.setAdapter(adapter);
        loginRoleDropdown.setInputType(0);

        Button loginButton = findViewById(R.id.login_button);

        username = findViewById(R.id.username_text_field);
        password = findViewById(R.id.password_text_field);
        register = findViewById(R.id.reg_text);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("role", loginRoleDropdown.getText().toString());
                if (loginRoleDropdown.getText().toString().equals("Dosen")) {
                    loginDosen();
                }
                else if (loginRoleDropdown.getText().toString().equals("Mahasiswa")) {
                    loginMahasiswa();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Silahkan pilih role", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginMahasiswa() {
        BaseApi loginApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Log.d("username", username.toString());
        Log.d("password", password.toString());
        Call<LoginMahasiswaResponse> loginMahasiswaResponseCall = loginApi.loginMahasiswa(username.getText().toString(), password.getText().toString());
        loginMahasiswaResponseCall.enqueue(new Callback<LoginMahasiswaResponse>() {
            @Override
            public void onResponse(Call<LoginMahasiswaResponse> call, Response<LoginMahasiswaResponse> response) {
                if (response.body().getMessage().equals("login sukses")) {
                    SharedPreferences.Editor editor = loginPreferences.edit();
                    String nama = response.body().getData().getNama();
                    String username = response.body().getData().getUsername();
                    String nim = response.body().getData().getNim();
                    editor.putInt("login_status", 2);
                    editor.putString("nama", nama);
                    editor.putString("username", username);
                    editor.putString("nim", nim);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), String.format("Login berhasil, selamat datang %1s", response.body().getData().getNama()), Toast.LENGTH_SHORT).show();
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivity);
                    finish();
                    //TODO ganti activity untuk mahasiswa nya

                }
                else {
                    Toast.makeText(getApplicationContext(), "Login gagal silahkan cek password dan username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginMahasiswaResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loginDosen() {
        BaseApi loginApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Log.d("username", username.toString());
        Log.d("password", password.toString());
        Call<LoginDosenResponse> loginDosenResponseCall = loginApi.loginDosen(username.getText().toString(), password.getText().toString());
        loginDosenResponseCall.enqueue(new Callback<LoginDosenResponse>() {
            @Override
            public void onResponse(Call<LoginDosenResponse> call, Response<LoginDosenResponse> response) {
                if (response.body().getMessage().equals("login sukses")) {
                    SharedPreferences.Editor editor = loginPreferences.edit();
                    String nama = response.body().getData().getNama();
                    String username = response.body().getData().getUsername();
                    String nip = response.body().getData().getNip();
                    editor.putInt("login_status", 1);
                    editor.putString("nama", nama);
                    editor.putString("username", username);
                    editor.putString("nip", nip);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), String.format("Login berhasil, selamat datang %1s", response.body().getData().getNama()), Toast.LENGTH_SHORT).show();
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivity);
                    finish();

                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login gagal silahkan cek password dan username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDosenResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}