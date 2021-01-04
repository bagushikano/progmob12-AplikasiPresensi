package com.progmobklp12.aplikasipresensi.activity.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FillPresensiActivity extends AppCompatActivity {

    private String presensiNameKey = "PRESENSINAME";
    private String presensiDescKey = "PRESENSIDESC";
    private String presensiDateOpenKey = "PRESENSIDATEOPEN";
    private String presensiDateCloseKey = "PRESENSIDATECLOSE";
    private String presensiIdKey = "PRESENSIID";
    private String presensiOwnerKey = "PRESENSIOWNER";

    int id;
    private TextView presensiNameView;
    private TextView presensiKeteranganView;
    private TextView presensiDateOpenView;
    private TextView presensiDateCloseView;
    private TextView presensiOwner;

    private Button fillPresensi;
    private String username;
    SharedPreferences loginPreferences;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_presensi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");

        presensiNameView = findViewById(R.id.presensi_title_view);
        presensiKeteranganView = findViewById(R.id.presensi_desc_view);
        presensiDateOpenView = findViewById(R.id.presensi_open_date_view);
        presensiDateCloseView = findViewById(R.id.presensi_close_date_view);
        presensiOwner = findViewById(R.id.presensi_owner_view);
        fillPresensi = findViewById(R.id.fill_presensi);

        dialog = new ProgressDialog(this);

        id = extras.getInt(presensiIdKey);
        presensiNameView.setText(extras.getString(presensiNameKey));
        presensiKeteranganView.setText(String.format("Keterangan: %s", extras.getString(presensiDescKey)));
        presensiDateOpenView.setText(String.format("Tanggal buka: %s", extras.getString(presensiDateOpenKey)));
        presensiDateCloseView.setText(String.format("Tanggal tutup: %s",extras.getString(presensiDateCloseKey)));
        presensiOwner.setText(String.format("Pemilik presensi: %s", extras.getString(presensiOwnerKey)));

        fillPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Mohon tunggu...");
                dialog.show();
                BaseApi fillPresensiApi = RetrofitClient.buildRetrofit().create(BaseApi.class);
                Call<MessageResponseModel> fillPresensiCall = fillPresensiApi.signPresensiMahasiswa(username, id);
                fillPresensiCall.enqueue(new Callback<MessageResponseModel>() {
                    @Override
                    public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        if (response.code() == 200) {
                            if (response.body().getMessage().equals("Presensi Berhasil di Buat")) {
                                Toast.makeText(getApplicationContext(), "Presensi berhasil di isi!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Presensi gagal di isi!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}