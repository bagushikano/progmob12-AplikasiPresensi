package com.progmobklp1.aplikasipresensi.fragment;

import android.content.Intent;
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
import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.activity.CreatePresensi;
import com.progmobklp1.aplikasipresensi.activity.DetailPresensiActivity;
import com.progmobklp1.aplikasipresensi.adapter.PresensiListAdapter;
import com.progmobklp1.aplikasipresensi.model.Presensi;

import java.util.ArrayList;
import java.util.Date;


public class PresensiFragment extends Fragment {

    private ArrayList<Presensi> presensiArrayList;

    private PresensiListAdapter presensiListAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    FloatingActionButton fab;

    private TextView listKosong;
    View v;

    public PresensiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_presensi, container, false);
        recyclerView = v.findViewById(R.id.presensi_recycler_view);
        presensiListAdapter = new PresensiListAdapter(this.getActivity(), presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presensiListAdapter);
        FloatingActionButton fab = v.findViewById(R.id.presensi_detail_fab);

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

        presensiArrayList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            presensiArrayList.add(new Presensi(i, String.format("Coba list presensi gan %d", i), String.format("Coba deskripsi presensi gan %d", i),
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        }
    }

}