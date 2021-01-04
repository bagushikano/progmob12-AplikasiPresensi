package com.progmobklp12.aplikasipresensi.activity.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    private TextInputLayout namaUserLayout;
    private TextInputLayout nimUserLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;

    private String namaUser;
    private String nimUser;
    private String username;

    private ProgressDialog dialog;

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

        namaUserLayout = findViewById(R.id.nama_form);
        nimUserLayout = findViewById(R.id.noinduk_form);
        usernameLayout = findViewById(R.id.username_form);
        passwordLayout = findViewById(R.id.password_form);

        dialog = new ProgressDialog(this);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUserTextField.getText().toString().length() == 0) {
                    namaUserLayout.setError(getString(R.string.register_name_error));
                } else {
                    if (namaUserTextField.getText().toString().length() > 50) {
                        namaUserLayout.setError(getString(R.string.register_name_error_length));
                    } else {
                        namaUserLayout.setError(null);
                        if (nimUserTextField.getText().toString().length() == 0) {
                            nimUserLayout.setError(getString(R.string.register_noinduk_error));
                        } else {
                            if (nimUserTextField.getText().toString().length() > 15) {
                                nimUserLayout.setError(getString(R.string.register_noinduk_error_length));
                            } else {
                                nimUserLayout.setError(null);
                                if (usernameTextField.getText().toString().length() == 0) {
                                    usernameLayout.setError(getString(R.string.register_username_error));
                                } else {
                                    if (usernameTextField.getText().toString().length() > 25) {
                                        usernameLayout.setError(getString(R.string.register_username_error_length));
                                    } else {
                                        usernameLayout.setError(null);
                                        if (passwordTextField.getText().toString().length() == 0) {
                                            passwordLayout.setError(getString(R.string.register_password_error));
                                        } else {
                                            passwordLayout.setError(null);
                                            editProfileMahasiswa();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
        dialog.setMessage("Mohon tunggu...");
        dialog.show();
        BaseApi editMahasiswaProfile = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<UpdateProfileMahasiswaResponse> updateProfileMahasiswaResponseCall = editMahasiswaProfile.editProfileMahasiswa(username, namaUserTextField.getText().toString(),
                nimUserTextField.getText().toString(), usernameTextField.getText().toString(), passwordTextField.getText().toString());
        updateProfileMahasiswaResponseCall.enqueue(new Callback<UpdateProfileMahasiswaResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileMahasiswaResponse> call, Response<UpdateProfileMahasiswaResponse> response) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                if (response.code() == 200) {
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Profile gagal di update silahkan cek password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileMahasiswaResponse> call, Throwable t) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}