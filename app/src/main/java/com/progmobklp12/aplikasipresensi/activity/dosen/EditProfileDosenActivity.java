package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    private TextInputLayout namaUserLayout;
    private TextInputLayout nipUserLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;

    private String namaUser;
    private String nipUser;
    private String username;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_dosen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);

        namaUserTextField = findViewById(R.id.edit_nama_text_field);
        nipUserTextField = findViewById(R.id.edit_noinduk_text_field);
        usernameTextField = findViewById(R.id.edit_dosen_username_text_field);
        passwordTextField = findViewById(R.id.edit_password_text_field);
        changePassword = findViewById(R.id.change_password_text);
        editProfile = findViewById(R.id.update_profile_dosen_button);

        namaUserLayout = findViewById(R.id.nama_form);
        nipUserLayout = findViewById(R.id.noinduk_form);
        usernameLayout = findViewById(R.id.username_form);
        passwordLayout = findViewById(R.id.password_form);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordActivity = new Intent(getApplicationContext(), ChangePasswordDosenActivity.class);
                startActivity(changePasswordActivity);
            }
        });
        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        nipUser = loginPreferences.getString("nip", "kosong");
        username = loginPreferences.getString("username", "kosong");

        namaUserTextField.setText(namaUser);
        nipUserTextField.setText(nipUser);
        usernameTextField.setText(username);

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
                        if (nipUserTextField.getText().toString().length() == 0) {
                            nipUserLayout.setError(getString(R.string.register_noinduk_error));
                        } else {
                            if (nipUserTextField.getText().toString().length() > 15) {
                                nipUserLayout.setError(getString(R.string.register_noinduk_error_length));
                            } else {
                                nipUserLayout.setError(null);
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
                                            editProfileDosen();
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

    public void editProfileDosen() {
        dialog.setMessage("Mohon tunggu...");
        dialog.show();
        BaseApi editDosenProfile = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<UpdateProfileDosenResponse> updateProfileDosenResponseCall = editDosenProfile.editProfileDosen(username, namaUserTextField.getText().toString(),
                nipUserTextField.getText().toString(), usernameTextField.getText().toString(), passwordTextField.getText().toString());
        updateProfileDosenResponseCall.enqueue(new Callback<UpdateProfileDosenResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileDosenResponse> call, Response<UpdateProfileDosenResponse> response) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                if (response.code() == 200) {
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
                        }
                        else if (response.body().getMessage().equals("username sama")) {
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Username tidak tersedia", Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Profile gagal di update, " +
                                    "silahkan cek password dan data yang di update, atau tunggu beberapa saat", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileDosenResponse> call, Throwable t) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}