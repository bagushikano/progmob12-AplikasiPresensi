package com.progmobklp12.aplikasipresensi.fragment.dosen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.PresensiListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiDosenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    SharedPreferences loginPreferences;
    private TextView welcomeText;
    private String namaUser;
    private String username;
    View v;
    private FloatingActionButton refreshData;

    private ArrayList<Presensi> presensiArrayList;

    private PresensiListAdapter presensiListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        refreshData = v.findViewById(R.id.presensi_open_refresh);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        username = loginPreferences.getString("username", "kosong");
        welcomeText = v.findViewById(R.id.home_welcome);
        welcomeText.setText(String.format("Selamat datang %1$s!", namaUser));

        presensiArrayList = new ArrayList<>();
        getDosenPresensiOpen();
        recyclerView = v.findViewById(R.id.presensi_open_list);
        presensiListAdapter = new PresensiListAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListAdapter);

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presensiArrayList.clear();
                getDosenPresensiOpen();
            }
        });

        return v;
    }


    public void getDosenPresensiOpen() {
        BaseApi getPresensiOpen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiDosenResponse> presensiDosenOpenResponseCall = getPresensiOpen.listPresensiOpenDosen(username);
        presensiDosenOpenResponseCall.enqueue(new Callback<PresensiDosenResponse>() {
            @Override
            public void onResponse(Call<PresensiDosenResponse> call, Response<PresensiDosenResponse> response) {
                if (response.body().getMessage().equals("Presensi berhasil di tampilkan")) {
                    presensiArrayList.addAll(response.body().getData());
                    presensiListAdapter.notifyDataSetChanged();
                    Snackbar.make(v, "Data presensi terbuka berhasil di refresh!", Snackbar.LENGTH_SHORT).show();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "Data presensi terbuka gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresensiDosenResponse> call, Throwable t) {
                Snackbar.make(v, "Data presensi terbuka gagal di refresh!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}