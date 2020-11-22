package com.progmobklp1.aplikasipresensi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.adapter.DetailPresensiListAdapter;
import com.progmobklp1.aplikasipresensi.adapter.PresensiListAdapter;
import com.progmobklp1.aplikasipresensi.model.DetailPresensi;
import com.progmobklp1.aplikasipresensi.model.Mahasiswa;
import com.progmobklp1.aplikasipresensi.model.Presensi;

import java.util.ArrayList;
import java.util.Date;

public class DetailPresensiActivity extends AppCompatActivity {

    private ArrayList<DetailPresensi> detailPresensiArrayList;
    private ArrayList<Mahasiswa> mahasiswaArrayList;

    private DetailPresensiListAdapter detailPresensiListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    int id;
    TextView presensiDetailNameView, presensiDetailDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_presensi);

        Bundle extras = getIntent().getExtras(); //ini di pake buat nangkep data yang di dapet dari activity yang manggil activity ini

        id = extras.getInt("PRESENSIID");

        presensiDetailNameView = findViewById(R.id.presensi_detail_name_view);
        presensiDetailDateView = findViewById(R.id.presensi_detail_date_view);

        presensiDetailNameView.setText(extras.getString("PRESENSINAME"));
        presensiDetailDateView.setText(extras.getString("PRESENSIDATEADDED"));

        mahasiswaArrayList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            mahasiswaArrayList.add(new Mahasiswa(String.format("Test nama zyeenkk %d", i), String.format("Test nim %d", i), String.format("Test prodi %d", i), i));
        }

        detailPresensiArrayList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            detailPresensiArrayList.add(new DetailPresensi(i, id, mahasiswaArrayList.get(i).getMhsId(), new Date(System.currentTimeMillis())));
        }

        recyclerView = findViewById(R.id.presensi_detail_recycler_view);
        detailPresensiListAdapter = new DetailPresensiListAdapter(this, detailPresensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detailPresensiListAdapter);


    }
}