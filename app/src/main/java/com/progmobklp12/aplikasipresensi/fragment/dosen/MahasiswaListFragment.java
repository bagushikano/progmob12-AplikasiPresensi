package com.progmobklp12.aplikasipresensi.fragment.dosen;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.App;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.MahasiswaListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.ListMahasiswaResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MahasiswaListFragment extends Fragment {
    private ArrayList<Mahasiswa> mahasiswaArrayList;

    private MahasiswaListAdapter mahasiswaListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private FloatingActionButton refreshData;

    private TextView listKosong;
    View v;

    int flagData = 0;

    public MahasiswaListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_mahasiswa_list, container, false);
        mahasiswaArrayList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.mahasiswa_list_recycler_view);
        mahasiswaListAdapter = new MahasiswaListAdapter(this.getActivity(), mahasiswaArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mahasiswaListAdapter);

        refreshData = v.findViewById(R.id.mahasiswa_list_refresh);

        getAllMahasiswa();

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mahasiswaArrayList.clear();
                getAllMahasiswa();
                // TODO handle refresh data semisal koneksi rusak
            }
        });


        return v;
    }

    public void getAllMahasiswa() {
        BaseApi getMahasiswa = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<ListMahasiswaResponse> listMahasiswaResponseCall = getMahasiswa.listMahasiswaAll();
        listMahasiswaResponseCall.enqueue(new Callback<ListMahasiswaResponse>() {
            @Override
            public void onResponse(Call<ListMahasiswaResponse> call, Response<ListMahasiswaResponse> response) {
                if (response.body().getMessage().equals("List mahasiswa berhasil di tampilkan")) {
                    mahasiswaArrayList.addAll(response.body().getData());
                    mahasiswaListAdapter.notifyDataSetChanged();
                    new insertDataMahasiswa().execute();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "List mahasiswa gagal di refresh, menggunakan data dari database", Snackbar.LENGTH_SHORT).show();
                    new getMahasiswaData().execute();
                    mahasiswaListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ListMahasiswaResponse> call, Throwable t) {
                Snackbar.make(v, "List mahasiswa gagal di refresh, menggunakan data dari database", Snackbar.LENGTH_SHORT).show();
                new getMahasiswaData().execute();
                mahasiswaListAdapter.notifyDataSetChanged();
            }
        });
    }

    public class insertDataMahasiswa extends AsyncTask<Void, Void, Mahasiswa> {
        @Override
        protected Mahasiswa doInBackground(Void... voids) {
            ((App) getActivity().getApplication()).appDatabase.mahasiswaDao().nukeTable();
            ((App) getActivity().getApplication()).appDatabase.mahasiswaDao().insertAll(mahasiswaArrayList);
            return null;
        }
    }
    public class getMahasiswaData extends AsyncTask<Void, Void, Mahasiswa> {
        @Override
        protected Mahasiswa doInBackground(Void... voids) {
            mahasiswaArrayList.addAll(((App) getActivity().getApplication()).appDatabase.mahasiswaDao().getAll());
            return null;
        }
    }

}