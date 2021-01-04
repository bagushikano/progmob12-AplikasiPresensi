package com.progmobklp12.aplikasipresensi.activity.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFilledPresensiActivity extends AppCompatActivity {

    int id;
    private TextView presensiNameView;
    private TextView presensiKeteranganView;
    private TextView presensiDateOpenView;
    private TextView presensiDateCloseView;
    private TextView presensiStatusView;
    private TextView presensiOwner;

    private TextView presensiDateFilled;
    private TextView isApproved;

    private String presensiNameKey = "PRESENSINAME";
    private String presensiDescKey = "PRESENSIDESC";
    private String presensiDateOpenKey = "PRESENSIDATEOPEN";
    private String presensiDateCloseKey = "PRESENSIDATECLOSE";
    private String presensiIdKey = "PRESENSIID";
    private String presensiStatusKey = "PRESENSISTATUS";
    private String presensiOwnerKey = "PRESENSIOWNER";

    SharedPreferences loginPreferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_filled_presensi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");

        Bundle extras = getIntent().getExtras(); //ini di pake buat nangkep data yang di dapet dari activity yang manggil activity ini

        presensiNameView = findViewById(R.id.presensi_detail_name_view);
        presensiKeteranganView = findViewById(R.id.presensi_detail_keterangan_view);
        presensiDateOpenView = findViewById(R.id.presensi_detail_date_open_view);
        presensiDateCloseView = findViewById(R.id.presensi_detail_date_close_view);
        presensiStatusView = findViewById(R.id.presensi_detail_status_view);
        presensiOwner = findViewById(R.id.presensi_detail_owner_view);

        presensiDateFilled = findViewById(R.id.presensi_date_filled);
        isApproved = findViewById(R.id.presensi_is_approved);

        id = extras.getInt(presensiIdKey);
        presensiNameView.setText(extras.getString(presensiNameKey));
        presensiKeteranganView.setText(String.format("Keterangan: %s", extras.getString(presensiDescKey)));
        presensiDateOpenView.setText(String.format("Tanggal buka: %s", extras.getString(presensiDateOpenKey)));
        presensiDateCloseView.setText(String.format("Tanggal tutup: %s",extras.getString(presensiDateCloseKey)));
        presensiOwner.setText(String.format("Pemilik presensi: %s", extras.getString(presensiOwnerKey)));
        presensiDateFilled.setText("Tanggal di isi: mohon tunggu...");
        isApproved.setText("Status presensi: mohon tunggu...");
        if (extras.getInt(presensiStatusKey) == 1) {
            presensiStatusView.setText("Status presensi: terbuka");
        }
        else {
            presensiStatusView.setText("Status presensi: Tutup");
        }

        BaseApi getDetailPresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<DetailPresensiResponse> detailPresensiResponseCall = getDetailPresensi.detailPresensiMahasiswa(username, id);
        detailPresensiResponseCall.enqueue(new Callback<DetailPresensiResponse>() {
            @Override
            public void onResponse(Call<DetailPresensiResponse> call, Response<DetailPresensiResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("Presensi telah di isi")) {
                        presensiDateFilled.setText(String.format("Tanggal di isi: %s", response.body().getData().get(0).getDateFilled()));
                        if (response.body().getData().get(0).getIsApproved() == 1) {
                            isApproved.setText("Status presensi: Approved");
                        }
                        else {
                            isApproved.setText("Status presensi: Declined");
                        }
                    }
                }
                else {
                    presensiDateFilled.setText("Tanggal di isi: koneksi ke server gagal");
                    isApproved.setText("Status presensi: koneksi ke server gagal");
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<DetailPresensiResponse> call, Throwable t) {
                presensiDateFilled.setText("Tanggal di isi: koneksi ke server gagal");
                isApproved.setText("Status presensi: koneksi ke server gagal");
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}