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
import com.progmobklp12.aplikasipresensi.adapter.PresensiListAdapter;
import com.progmobklp12.aplikasipresensi.adapter.PresensiListMahasiswaAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiDosenResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiMahasiswaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresensiMahasiswaFragment extends Fragment {

    private ArrayList<Presensi> presensiArrayList;

    private PresensiListMahasiswaAdapter presensiListMahasiswaAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabRefresh;



    private TextView listKosong;
    View v;


    public PresensiMahasiswaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_presensi_mahasiswa, container, false);
        listKosong = v.findViewById(R.id.empty_view);
        fabRefresh = v.findViewById(R.id.presensi_refresh_fab);
        presensiArrayList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.presensi_recycler_view);
        presensiListMahasiswaAdapter = new PresensiListMahasiswaAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListMahasiswaAdapter);

        presensiListMahasiswaAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (presensiListMahasiswaAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    listKosong.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    listKosong.setVisibility(View.GONE);
                }
            }
        });

        getMahasiswaPresensi();

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "List presensi sedang di refresh, harap tunggu", Snackbar.LENGTH_SHORT).show();
                presensiArrayList.clear();
                getMahasiswaPresensi();
            }
        });

        return v;
    }

    public void getMahasiswaPresensi() {
        v.findViewById(R.id.muter_muter).setVisibility(View.VISIBLE);
        v.findViewById(R.id.presensi_recycler_view).setVisibility(View.GONE);
        v.findViewById(R.id.empty_view).setVisibility(View.GONE);
        BaseApi getPresensiMahasiswa = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<PresensiMahasiswaResponse> presensiMahasiswaResponseCall = getPresensiMahasiswa.listPresensiMahasiswa();
        presensiMahasiswaResponseCall.enqueue(new Callback<PresensiMahasiswaResponse>() {
            @Override
            public void onResponse(Call<PresensiMahasiswaResponse> call, Response<PresensiMahasiswaResponse> response) {
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("List Absensi berhasil ditampilkan")) {
                        presensiArrayList.addAll(response.body().getData());
                    }
                    else {
                        Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
                presensiListMahasiswaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PresensiMahasiswaResponse> call, Throwable t) {
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                Snackbar.make(v, "Data presensi gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                presensiListMahasiswaAdapter.notifyDataSetChanged();
            }
        });
    }

}