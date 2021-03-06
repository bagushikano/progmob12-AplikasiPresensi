package com.progmobklp12.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private TextInputEditText passwordConfirm;

    private TextInputLayout namaLayout;
    private TextInputLayout noIndukLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout passwordConfirmLayout;

    private ProgressDialog dialog;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        passwordConfirm = findViewById(R.id.password_confirm_text_field);
        registerButton = findViewById(R.id.register_button);

        namaLayout = findViewById(R.id.nama_form);
        noIndukLayout = findViewById(R.id.noinduk_form);
        usernameLayout = findViewById(R.id.username_form);
        passwordLayout = findViewById(R.id.password_form);
        passwordConfirmLayout = findViewById(R.id.password_confirm_form);

        dialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().length() == 0) {
                    namaLayout.setError(getString(R.string.register_name_error));
                } else{
                    if (nama.getText().toString().length() > 50) {
                        namaLayout.setError(getString(R.string.register_name_error_length));
                    } else {
                        namaLayout.setError(null);
                        if (noInduk.getText().toString().length() == 0) {
                            noIndukLayout.setError(getString(R.string.register_noinduk_error));
                        } else{
                            if (noInduk.getText().toString().length() > 15) {
                                noIndukLayout.setError(getString(R.string.register_noinduk_error_length));
                            } else {
                                noIndukLayout.setError(null);
                                if (username.getText().toString().length() == 0) {
                                    usernameLayout.setError(getString(R.string.register_username_error));
                                } else {
                                    if (username.getText().toString().length() > 25) {
                                        usernameLayout.setError(getString(R.string.register_username_error_length));
                                    } else {
                                        usernameLayout.setError(null);
                                        if (password.getText().toString().length() == 0) {
                                            passwordLayout.setError(getString(R.string.register_password_error));
                                        } else {
                                            passwordLayout.setError(null);
                                            if (passwordConfirm.getText().toString().length() == 0) {
                                                passwordConfirmLayout.setError(getString(R.string.register_password_confirm_error));
                                            } else {
                                                if (!(password.getText().toString().equals(passwordConfirm.getText().toString()))) {
                                                    passwordConfirmLayout.setError(getString(R.string.register_password_confirm_not_match));
                                                } else {
                                                    if (loginRoleDropdown.getText().toString().equals("Dosen")) {
                                                        registerDosen();
                                                    } else if (loginRoleDropdown.getText().toString().equals("Mahasiswa")) {
                                                        registerMahasiswa();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), getString(R.string.register_select_role_error), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void registerDosen() {
        dialog.setMessage("Mohon tunggu...");
        dialog.show();
        BaseApi registerDosenApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<RegisterDosenResponse> registerDosenResponseCall = registerDosenApi.registerDosen(nama.getText().toString(), noInduk.getText().toString(),
                username.getText().toString(), password.getText().toString());
        registerDosenResponseCall.enqueue(new Callback<RegisterDosenResponse>() {
            @Override
            public void onResponse(Call<RegisterDosenResponse> call, Response<RegisterDosenResponse> response) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("Berhasil Register")) {
                        Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_SHORT).show();
                        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginActivity);
                        finish();
                    }
                    else if (response.body().getMessage().equals("username sama")) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Username tidak tersedia", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Register gagal, silahkan coba lagi nanti", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDosenResponse> call, Throwable t) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void registerMahasiswa() {
        dialog.setMessage("Mohon tunggu...");
        dialog.show();
        BaseApi registerMahasiswaApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<RegisterMahasiswaResponse> registerMahasiswaResponseCall = registerMahasiswaApi.registerMahasiswa(nama.getText().toString(), noInduk.getText().toString(),
                username.getText().toString(), password.getText().toString());
        registerMahasiswaResponseCall.enqueue(new Callback<RegisterMahasiswaResponse>() {
            @Override
            public void onResponse(Call<RegisterMahasiswaResponse> call, Response<RegisterMahasiswaResponse> response) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("Berhasil Register")) {
                        Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_SHORT).show();
                        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginActivity);
                        finish();
                    }
                    else if (response.body().getMessage().equals("username sama")) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Username tidak tersedia", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Register gagal, silahkan coba lagi nanti", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterMahasiswaResponse> call, Throwable t) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}