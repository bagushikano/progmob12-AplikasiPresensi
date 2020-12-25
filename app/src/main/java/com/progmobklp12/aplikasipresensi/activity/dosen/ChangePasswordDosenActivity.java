package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.UpdatePasswordDosenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordDosenActivity extends AppCompatActivity {

    private Button updatePassword;

    private TextInputEditText oldPassword;
    private TextInputEditText newPassword;

    private String username;

    SharedPreferences loginPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_dosen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");
        oldPassword = findViewById(R.id.dosen_old_password_text_field);
        newPassword = findViewById(R.id.dosen_new_password_text_field);
        updatePassword = findViewById(R.id.dosen_update_password);

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePasswordDosen();
                // TODO tambahin validasi untuk konfirmasi password sama field passwordnya
            }
        });
    }

    public void updatePasswordDosen() {
        BaseApi updatePassword = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<UpdatePasswordDosenResponse> updatePasswordDosenResponseCall = updatePassword.updatePasswordDosen(username,
                oldPassword.getText().toString(), newPassword.getText().toString());
        updatePasswordDosenResponseCall.enqueue(new Callback<UpdatePasswordDosenResponse>() {
            @Override
            public void onResponse(Call<UpdatePasswordDosenResponse> call, Response<UpdatePasswordDosenResponse> response) {
                if(response.body().getMessage() != null ) {
                    if (response.body().getMessage().equals("Password berhasil di Update")) {
                        Toast.makeText(getApplicationContext(), String.format("Password berhasil di update!"), Toast.LENGTH_SHORT).show();
                        finish();
                        //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                    } else {
                        Toast.makeText(getApplicationContext(), "Password gagal di update, silahkan cek password lama", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePasswordDosenResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Password gagal di update", Toast.LENGTH_SHORT).show();
            }
        });
    }

}