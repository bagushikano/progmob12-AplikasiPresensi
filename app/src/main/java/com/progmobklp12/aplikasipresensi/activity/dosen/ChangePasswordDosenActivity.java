package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordDosenActivity extends AppCompatActivity {

    private Button updatePassword;

    private TextInputEditText oldPassword;
    private TextInputEditText newPassword;
    private TextInputEditText newPasswordConfirm;
    private TextInputLayout oldPasswordLayout;
    private TextInputLayout newPasswordLayout;
    private TextInputLayout newPasswordConfirmLayout;

    private ProgressDialog dialog;

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
        newPasswordConfirm = findViewById(R.id.dosen_confirm_new_password_text_field);

        oldPasswordLayout = findViewById(R.id.password_old_form);
        newPasswordLayout = findViewById(R.id.password_new_form);
        newPasswordConfirmLayout = findViewById(R.id.password_confirm_form);

        dialog = new ProgressDialog(this);

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().length() == 0) {
                    oldPasswordLayout.setError("Password lama tidak boleh kosong");
                }
                else{
                    oldPasswordLayout.setError(null);
                    if(newPassword.getText().toString().length() == 0){
                        newPasswordLayout.setError("Password baru tidak boleh kosong");
                    }
                    else{
                        newPasswordLayout.setError(null);
                        if(newPasswordConfirm.getText().toString().length() == 0){
                            newPasswordConfirmLayout.setError("Konfirmasi password baru tidak boleh kosong");
                        }
                        else {
                            if(!(newPasswordConfirm.getText().toString().equals(newPassword.getText().toString()))){
                                newPasswordConfirmLayout.setError("Password tidak cocok");
                            }
                            else{
                                newPasswordConfirmLayout.setError(null);
                                updatePasswordDosen();
                            }
                        }
                    }
                }
            }
        });
    }

    public void updatePasswordDosen() {
        dialog.setMessage("Mohon tunggu...");
        dialog.show();
        BaseApi updatePassword = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<MessageResponseModel> updatePasswordDosenResponseCall = updatePassword.updatePasswordDosen(username,
                oldPassword.getText().toString(), newPassword.getText().toString());
        updatePasswordDosenResponseCall.enqueue(new Callback<MessageResponseModel>() {
            @Override
            public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                if(response.body().getMessage() != null ) {
                    if (response.body().getMessage().equals("Password berhasil di Update")) {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
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
            public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Password gagal di update", Toast.LENGTH_SHORT).show();
            }
        });
    }

}