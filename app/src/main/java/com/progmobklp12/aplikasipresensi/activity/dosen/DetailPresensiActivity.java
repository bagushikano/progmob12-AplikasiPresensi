package com.progmobklp12.aplikasipresensi.activity.dosen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.DetailPresensiListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensi;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensiResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiDosenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresensiActivity extends AppCompatActivity {

    private ArrayList<DetailPresensi> detailPresensiArrayList;
    private ArrayList<Mahasiswa> mahasiswaArrayList;

    private DetailPresensiListAdapter detailPresensiListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    int id;

    private TextView listKosong;

    private TextView presensiNameView;
    private TextView presensiKeteranganView;
    private TextView presensiDateOpenView;
    private TextView presensiDateCloseView;
    private TextView presensiStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_presensi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras(); //ini di pake buat nangkep data yang di dapet dari activity yang manggil activity ini

        id = extras.getInt("PRESENSIID");

        listKosong = findViewById(R.id.empty_view);

        presensiNameView = findViewById(R.id.presensi_detail_name_view);
        presensiKeteranganView = findViewById(R.id.presensi_detail_keterangan_view);
        presensiDateOpenView = findViewById(R.id.presensi_detail_date_open_view);
        presensiDateCloseView = findViewById(R.id.presensi_detail_date_close_view);
        presensiStatusView = findViewById(R.id.presensi_detail_status_view);


        presensiNameView.setText(extras.getString("PRESENSINAME"));
        presensiKeteranganView.setText(String.format("Keterangan: %s", extras.getString("PRESENSIDESC")));
        presensiDateOpenView.setText(String.format("Tanggal buka: %s", extras.getString("PRESENSIDATEOPEN")));
        presensiDateCloseView.setText(String.format("Tanggal tutup: %s",extras.getString("PRESENSIDATECLOSE")));
        presensiStatusView.setText(extras.getString("PRESENSINAME"));

        if (extras.getInt("PRESENSISTATUS") == 1) {
            presensiStatusView.setText("Status presensi: terbuka");
        }
        else {
            presensiStatusView.setText("Status presensi: Tutup");
        }
        detailPresensiArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.presensi_detail_recycler_view);
        detailPresensiListAdapter = new DetailPresensiListAdapter(this, detailPresensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detailPresensiListAdapter);

        detailPresensiListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (detailPresensiListAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    listKosong.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    listKosong.setVisibility(View.GONE);
                }
            }
        });

        getDetailPresensi();
    }

    public void getDetailPresensi() {
        findViewById(R.id.muter_muter).setVisibility(View.VISIBLE);
        findViewById(R.id.presensi_detail_recycler_view).setVisibility(View.GONE);
        findViewById(R.id.empty_view).setVisibility(View.GONE);
        BaseApi detailPresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<DetailPresensiResponse> detailPresensiResponseCall = detailPresensi.detailPresensiDosen(id);
        detailPresensiResponseCall.enqueue(new Callback<DetailPresensiResponse>() {
            @Override
            public void onResponse(Call<DetailPresensiResponse> call, Response<DetailPresensiResponse> response) {
                findViewById(R.id.muter_muter).setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("Presensi Berhasil di tampilkan")) {
                        detailPresensiArrayList.addAll(response.body().getData());
                        detailPresensiListAdapter.notifyDataSetChanged();
                        Snackbar.make(findViewById(android.R.id.content), "Daftar detail presensi berhasil di refresh!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailPresensiResponse> call, Throwable t) {
                findViewById(R.id.muter_muter).setVisibility(View.GONE);
                detailPresensiListAdapter.notifyDataSetChanged();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), getString(R.string.server_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}