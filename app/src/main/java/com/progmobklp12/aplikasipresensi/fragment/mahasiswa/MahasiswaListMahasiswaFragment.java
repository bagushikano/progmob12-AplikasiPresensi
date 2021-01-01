package com.progmobklp12.aplikasipresensi.fragment.mahasiswa;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.EditProfileDosenActivity;
import com.progmobklp12.aplikasipresensi.activity.mahasiswa.EditProfileMahasiswaActivity;
import com.progmobklp12.aplikasipresensi.adapter.MahasiswaListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.ListMahasiswaResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MahasiswaListMahasiswaFragment extends Fragment {
    private ArrayList<Mahasiswa> mahasiswaArrayList;

    SharedPreferences loginPreferences;
    private Button editProfile;
    private TextView namaUserView;
    private TextView nimUserView;
    private TextView usernameView;

    private String namaUser;
    private String nimUser;
    private String username;

    private MahasiswaListAdapter mahasiswaListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private FloatingActionButton refreshData;

    private TextView listKosong;

    View v;

    public MahasiswaListMahasiswaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mahasiswaArrayList = new ArrayList<>();
        getAllMahasiswa();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mahasiswa_list_mahasiswa, container, false);

        editProfile = v.findViewById(R.id.mahasiswa_edit_profile);
        namaUserView = v.findViewById(R.id.mahasiswa_name_text);
        nimUserView = v.findViewById(R.id.mahasiswa_nim_text);
        usernameView = v.findViewById(R.id.mahasiswa_username_text);
        recyclerView = v.findViewById(R.id.mahasiswa_list_recycler_mahasiswa_view);
        mahasiswaListAdapter = new MahasiswaListAdapter(this.getActivity(), mahasiswaArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mahasiswaListAdapter);

        refreshData = v.findViewById(R.id.mahasiswa_list_refresh_mahasiswa_fab);

        loginPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        namaUser = loginPreferences.getString("nama", "kosong");
        nimUser = loginPreferences.getString("nim", "kosong");
        username = loginPreferences.getString("username", "kosong");

        namaUserView.setText(String.format("Nama: %1$s", namaUser));
        nimUserView.setText(String.format("NIM: %1$s", nimUser));
        usernameView.setText(String.format("Username: %1$s", username));


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile =  new Intent(getActivity(), EditProfileMahasiswaActivity.class);
                startActivityForResult(editProfile, 1);
            }
        });

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mahasiswaArrayList.clear();
                getAllMahasiswa();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            namaUser = loginPreferences.getString("nama", "kosong");
            nimUser = loginPreferences.getString("nim", "kosong");
            username = loginPreferences.getString("username", "kosong");
            namaUserView.setText(String.format("Nama: %1$s", namaUser));
            nimUserView.setText(String.format("NIM: %1$s", nimUser));
            usernameView.setText(String.format("Username: %1$s", username));
        }
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
                    Snackbar.make(v, "List data mahasiswa berhasil di refresh!", Snackbar.LENGTH_SHORT).show();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "List mahasiswa gagal di tampilkan", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListMahasiswaResponse> call, Throwable t) {
                Snackbar.make(v, "List mahasiswa gagal di tampilkan", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}