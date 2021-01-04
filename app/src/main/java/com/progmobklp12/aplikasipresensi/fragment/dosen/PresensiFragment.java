package com.progmobklp12.aplikasipresensi.fragment.dosen;

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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.CreatePresensi;
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
        listKosong = v.findViewById(R.id.empty_view);
        presensiArrayList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.presensi_recycler_view);
        presensiListAdapter = new PresensiListAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListAdapter);

        fab = v.findViewById(R.id.presensi_detail_fab);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");

        getDosenPresensi();

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "List presensi sedang di refresh, harap tunggu", Snackbar.LENGTH_SHORT).show();
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

        presensiListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (presensiListAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    listKosong.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    listKosong.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getDosenPresensi() {
        v.findViewById(R.id.muter_muter).setVisibility(View.VISIBLE);
        v.findViewById(R.id.presensi_recycler_view).setVisibility(View.GONE);
        v.findViewById(R.id.empty_view).setVisibility(View.GONE);
        BaseApi getPresensiDosen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiDosenResponse> presensiDosenResponseCall = getPresensiDosen.listPresensiDosen(username);
        presensiDosenResponseCall.enqueue(new Callback<PresensiDosenResponse>() {
            @Override
            public void onResponse(Call<PresensiDosenResponse> call, Response<PresensiDosenResponse> response) {
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("Presensi berhasil di tampilkan")) {
                        presensiArrayList.addAll(response.body().getData());
                        presensiListAdapter.notifyDataSetChanged();
                    }
                    else {
                        presensiListAdapter.notifyDataSetChanged();
                        Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    presensiListAdapter.notifyDataSetChanged();
                    Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresensiDosenResponse> call, Throwable t) {
                presensiListAdapter.notifyDataSetChanged();
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



}