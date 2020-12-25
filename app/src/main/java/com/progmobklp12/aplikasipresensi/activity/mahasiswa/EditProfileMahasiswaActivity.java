package com.progmobklp12.aplikasipresensi.activity.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.ChangePasswordDosenActivity;

public class EditProfileMahasiswaActivity extends AppCompatActivity {

    private Button editProfile;
    SharedPreferences loginPreferences;

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
        setContentView(R.layout.activity_edit_profile_mahasiswa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaUserTextField = findViewById(R.id.edit_mahasiswa_nama_text_field);
        nipUserTextField = findViewById(R.id.edit_mahasiswa_noinduk_text_field);
        usernameTextField = findViewById(R.id.edit_mahasiswa_username_text_field);
        passwordTextField = findViewById(R.id.mahasiswa_password_text_field);
        changePassword = findViewById(R.id.update_password_mahasiswa_text);
        editProfile = findViewById(R.id.update_profile_mahasiwa_button);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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

    }
}