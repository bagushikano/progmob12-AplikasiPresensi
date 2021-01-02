package com.progmobklp12.aplikasipresensi.fragment.mahasiswa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.PresensiListMahasiswaAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiMahasiswaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeMahasiswaFragment extends Fragment {

    SharedPreferences loginPreferences;
    private TextView welcomeText;
    private String namaUser;
    private String username;
    View v;
    private FloatingActionButton refreshData;

    private ArrayList<Presensi> presensiArrayList;

    private PresensiListMahasiswaAdapter presensiListMahasiswaAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    public HomeMahasiswaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home_mahasiswa, container, false);

        refreshData = v.findViewById(R.id.presensi_open_refresh);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        username = loginPreferences.getString("username", "kosong");
        welcomeText = v.findViewById(R.id.home_welcome);
        welcomeText.setText(String.format("Selamat datang %1$s!", namaUser));

        presensiArrayList = new ArrayList<>();
        getMahasiswaPresensiOpen();
        recyclerView = v.findViewById(R.id.presensi_open_list);
        presensiListMahasiswaAdapter = new PresensiListMahasiswaAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListMahasiswaAdapter);

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presensiArrayList.clear();
                getMahasiswaPresensiOpen();
            }
        });

        return v;
    }

    public void getMahasiswaPresensiOpen() {
        BaseApi getPresensiOpen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiMahasiswaResponse> presensiMahasiswaOpenResponseCall = getPresensiOpen.listPresensiOpenMahasiswa();
        presensiMahasiswaOpenResponseCall.enqueue(new Callback<PresensiMahasiswaResponse>() {
            @Override
            public void onResponse(Call<PresensiMahasiswaResponse> call, Response<PresensiMahasiswaResponse> response) {
                if (response.body().getMessage().equals("List Absensi berhasil ditampilkan")) {
                    presensiArrayList.addAll(response.body().getData());
                    presensiListMahasiswaAdapter.notifyDataSetChanged();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "Data presensi terbuka gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresensiMahasiswaResponse> call, Throwable t) {
                Snackbar.make(v, "Data presensi terbuka gagal di refresh!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}