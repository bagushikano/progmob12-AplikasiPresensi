package com.progmobklp12.aplikasipresensi.fragment.mahasiswa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.App;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.DosenListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.fragment.dosen.DosenListFragment;
import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;
import com.progmobklp12.aplikasipresensi.model.dosen.ListDosenResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DosenListMahasiswaFragment extends Fragment {

    private String namaUser;
    private String nipUser;
    private String username;


    private ArrayList<Dosen> dosenArrayList;

    private DosenListAdapter dosenListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private FloatingActionButton refreshData;
    private TextView listKosong;
    View v;

    private Activity activity;

    public DosenListMahasiswaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dosen_list_mahasiswa, container, false);

        dosenArrayList = new ArrayList<>();
        listKosong = v.findViewById(R.id.empty_view);
        refreshData = v.findViewById(R.id.dosen_list_refresh_mahasiswa_fab);
        recyclerView = v.findViewById(R.id.dosen_list_recycler_mahasiswa_view);
        dosenListAdapter = new DosenListAdapter(this.getActivity(), dosenArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dosenListAdapter);

        dosenListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (dosenListAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    listKosong.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    listKosong.setVisibility(View.GONE);
                }
            }
        });

        getAllDosen();

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "List dosen sedang di refresh, harap tunggu", Snackbar.LENGTH_SHORT).show();
                dosenArrayList.clear();
                getAllDosen();
            }
        });

        return v;
    }

    public void getAllDosen() {
        v.findViewById(R.id.muter_muter).setVisibility(View.VISIBLE);
        v.findViewById(R.id.dosen_list_recycler_mahasiswa_view).setVisibility(View.GONE);
        v.findViewById(R.id.empty_view).setVisibility(View.GONE);
        BaseApi getDosen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<ListDosenResponse> listDosenResponseCall = getDosen.listDosenAll();
        listDosenResponseCall.enqueue(new Callback<ListDosenResponse>() {
            @Override
            public void onResponse(Call<ListDosenResponse> call, Response<ListDosenResponse> response) {
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getMessage().equals("List dosen berhasil di tampilkan")) {
                        dosenArrayList.addAll(response.body().getData());
                        dosenListAdapter.notifyDataSetChanged();
                        new insertDataDosen().execute();
                    }
                    else {
                        Snackbar.make(v, "List dosen gagal di refresh, menggunakan data dari database", Snackbar.LENGTH_SHORT).show();
                        new getDosenData().execute();
                        dosenListAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Snackbar.make(v, "List dosen gagal di refresh, menggunakan data dari database", Snackbar.LENGTH_SHORT).show();
                    new getDosenData().execute();
                    dosenListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ListDosenResponse> call, Throwable t) {
                v.findViewById(R.id.muter_muter).setVisibility(View.GONE);
                Snackbar.make(v, "List dosen gagal di refresh, menggunakan data dari database", Snackbar.LENGTH_SHORT).show();
                new getDosenData().execute();
                dosenListAdapter.notifyDataSetChanged();
            }
        });
    }

    public class insertDataDosen extends AsyncTask<Void, Void, Dosen> {
        @Override
        protected Dosen doInBackground(Void... voids) {
            ((App) getActivity().getApplication()).appDatabase.dosenDao().nukeTable();
            ((App) getActivity().getApplication()).appDatabase.dosenDao().insertAll(dosenArrayList);
            return null;
        }
    }

    public class getDosenData extends AsyncTask<Void, Void, Dosen> {
        @Override
        protected Dosen doInBackground(Void... voids) {
            dosenArrayList.addAll(((App) getActivity().getApplication()).appDatabase.dosenDao().getAll());
            updateDataView();
            return null;
        }
    }

    public void updateDataView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dosenListAdapter.notifyDataSetChanged();
            }
        });
    }

}