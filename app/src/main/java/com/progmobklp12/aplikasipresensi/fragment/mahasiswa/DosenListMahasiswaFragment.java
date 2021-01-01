package com.progmobklp12.aplikasipresensi.fragment.mahasiswa;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.adapter.DosenListAdapter;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
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

    public DosenListMahasiswaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dosenArrayList = new ArrayList<>();
        getAllDosen();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dosen_list_mahasiswa, container, false);

        refreshData = v.findViewById(R.id.dosen_list_refresh_mahasiswa_fab);

        recyclerView = v.findViewById(R.id.dosen_list_recycler_mahasiswa_view);
        dosenListAdapter = new DosenListAdapter(this.getActivity(), dosenArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dosenListAdapter);

        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosenArrayList.clear();
                getAllDosen();
            }
        });

        return v;
    }

    public void getAllDosen() {
        BaseApi getDosen = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<ListDosenResponse> listDosenResponseCall = getDosen.listDosenAll();
        listDosenResponseCall.enqueue(new Callback<ListDosenResponse>() {
            @Override
            public void onResponse(Call<ListDosenResponse> call, Response<ListDosenResponse> response) {
                if (response.body().getMessage().equals("List dosen berhasil di tampilkan")) {
                    dosenArrayList.addAll(response.body().getData());
                    dosenListAdapter.notifyDataSetChanged();
                    Snackbar.make(v, "List data dosen berhasil di refresh!", Snackbar.LENGTH_SHORT).show();
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    Snackbar.make(v, "List data dosen gagal di refresh!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListDosenResponse> call, Throwable t) {
                Snackbar.make(v, "List data dosen gagal di refresh!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}