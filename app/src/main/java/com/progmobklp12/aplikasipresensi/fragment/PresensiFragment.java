package com.progmobklp12.aplikasipresensi.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.progmobklp12.aplikasipresensi.activity.CreatePresensi;
import com.progmobklp12.aplikasipresensi.adapter.PresensiListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiDosenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresensiFragment extends Fragment {

    private ArrayList<Presensi> presensiArrayList;

    SharedPreferences loginPreferences;
    private String username;

    private PresensiListAdapter presensiListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FloatingActionButton fab;
    FloatingActionButton fabRefresh;



    private TextView listKosong;
    View v;

    public PresensiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_presensi, container, false);


        fabRefresh = v.findViewById(R.id.presensi_refresh_fab);

        presensiArrayList = new ArrayList<>();

        recyclerView = v.findViewById(R.id.presensi_recycler_view);
        presensiListAdapter = new PresensiListAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListAdapter);

        FloatingActionButton fab = v.findViewById(R.id.presensi_detail_fab);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");

        getDosenPresensi();

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presensiArrayList.clear();
                getDosenPresensi();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPresensi = new Intent(getActivity(), CreatePresensi.class);
                startActivity(createPresensi);
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getDosenPresensi() {
        BaseApi getPresensiDosen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiDosenResponse> presensiDosenResponseCall = getPresensiDosen.listPresensiDosen(username);
        presensiDosenResponseCall.enqueue(new Callback<PresensiDosenResponse>() {
            @Override
            public void onResponse(Call<PresensiDosenResponse> call, Response<PresensiDosenResponse> response) {
                if (response.body().getMessage().equals("Presensi berhasil di tampilkan")) {
                    presensiArrayList.addAll(response.body().getData());
                    presensiListAdapter.notifyDataSetChanged();
                    Snackbar.make(v, "Data presensi berhasil di refresh!", Snackbar.LENGTH_SHORT).show();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresensiDosenResponse> call, Throwable t) {
                Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



}