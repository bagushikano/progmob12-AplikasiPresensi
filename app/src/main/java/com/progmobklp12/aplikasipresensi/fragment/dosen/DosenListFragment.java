package com.progmobklp12.aplikasipresensi.fragment.dosen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.App;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.EditProfileDosenActivity;
import com.progmobklp12.aplikasipresensi.adapter.DosenListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;
import com.progmobklp12.aplikasipresensi.model.dosen.ListDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;

public class DosenListFragment extends Fragment {

    SharedPreferences loginPreferences;
    private Button editProfile;
    private TextView namaUserView;
    private TextView nipUserView;
    private TextView usernameView;

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


    public DosenListFragment() {
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

        v = inflater.inflate(R.layout.fragment_dosen_list, container, false);
        listKosong = v.findViewById(R.id.empty_view);
        editProfile = v.findViewById(R.id.dosen_edit_profile);
        namaUserView = v.findViewById(R.id.dosen_name_text);
        nipUserView = v.findViewById(R.id.dosen_nip);
        usernameView = v.findViewById(R.id.dosen_username);
        refreshData = v.findViewById(R.id.dosen_list_refresh);

        dosenArrayList = new ArrayList<>();

        recyclerView = v.findViewById(R.id.dosen_list_recycler_view);
        dosenListAdapter = new DosenListAdapter(this.getActivity(), dosenArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dosenListAdapter);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        nipUser = loginPreferences.getString("nip", "kosong");
        username = loginPreferences.getString("username", "kosong");

        namaUserView.setText(String.format("Nama: %1$s", namaUser));
        nipUserView.setText(String.format("NIP: %1$s", nipUser));
        usernameView.setText(String.format("Username: %1$s", username));

        getAllDosen();

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "List dosen sedang di refresh, harap tunggu", Snackbar.LENGTH_SHORT).show();
                dosenArrayList.clear();
                getAllDosen();
            }
        });
        
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile =  new Intent(getActivity(), EditProfileDosenActivity.class);
                startActivityForResult(editProfile, 1);
            }
        });

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


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            namaUser = loginPreferences.getString("nama", "kosong");
            nipUser = loginPreferences.getString("nip", "kosong");
            username = loginPreferences.getString("username", "kosong");
            namaUserView.setText(String.format("Nama: %1$s", namaUser));
            nipUserView.setText(String.format("NIP: %1$s", nipUser));
            usernameView.setText(String.format("Username: %1$s", username));
        }
    }

    public void getAllDosen() {
        v.findViewById(R.id.muter_muter).setVisibility(View.VISIBLE);
        v.findViewById(R.id.dosen_list_recycler_view).setVisibility(View.GONE);
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