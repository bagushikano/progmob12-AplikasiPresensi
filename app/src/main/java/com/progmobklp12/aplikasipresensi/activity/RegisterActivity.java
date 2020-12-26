package com.progmobklp12.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.RegisterDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.RegisterMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nama;
    private TextInputEditText noInduk;
    private TextInputEditText username;
    private TextInputEditText password;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String[] loginRole = new String[] {getString(R.string.role_login_dropdown_mahasiswa), getString(R.string.login_role_dropdown_dosen)};
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.login_role_dropdown_menu_item, loginRole);
        AutoCompleteTextView loginRoleDropdown = findViewById(R.id.login_role_dropdown);
        loginRoleDropdown.setAdapter(adapter);
        loginRoleDropdown.setInputType(0);

        // TODO biar ga bisa ke tulis manual di role select nya
        loginRoleDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });

        nama = findViewById(R.id.nama_text_field);
        noInduk = findViewById(R.id.noinduk_text_field);
        username = findViewById(R.id.username_text_field);
        password = findViewById(R.id.password_text_field);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO tambahin validasi untuk konfirmasi password sama field yang lain
                if (loginRoleDropdown.getText().toString().equals("Dosen")) {
                    registerDosen();
                }
                else if (loginRoleDropdown.getText().toString().equals("Mahasiswa")) {
                    registerMahasiswa();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Silahkan pilih role", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void registerDosen() {
        BaseApi registerDosenApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<RegisterDosenResponse> registerDosenResponseCall = registerDosenApi.registerDosen(nama.getText().toString(), noInduk.getText().toString(),
                username.getText().toString(), password.getText().toString());
        registerDosenResponseCall.enqueue(new Callback<RegisterDosenResponse>() {
            @Override
            public void onResponse(Call<RegisterDosenResponse> call, Response<RegisterDosenResponse> response) {
                if (response.body().getMessage().equals("Berhasil Register")) {
                    Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_SHORT).show();
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login gagal silahkan cek password dan username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDosenResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerMahasiswa() {
        BaseApi registerMahasiswaApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<RegisterMahasiswaResponse> registerMahasiswaResponseCall = registerMahasiswaApi.registerMahasiswa(nama.getText().toString(), noInduk.getText().toString(),
                username.getText().toString(), password.getText().toString());
        registerMahasiswaResponseCall.enqueue(new Callback<RegisterMahasiswaResponse>() {
            @Override
            public void onResponse(Call<RegisterMahasiswaResponse> call, Response<RegisterMahasiswaResponse> response) {
                if (response.body().getMessage().equals("Berhasil Register")) {
                    Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_SHORT).show();
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login gagal silahkan cek password dan username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterMahasiswaResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}