package com.progmobklp1.aplikasipresensi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.model.DetailPresensi;

import java.util.ArrayList;

public class DetailPresensiListAdapter extends RecyclerView.Adapter<DetailPresensiListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DetailPresensi> detailPresensiArrayList;
    private int position;

    public DetailPresensiListAdapter(Context context, ArrayList<DetailPresensi> detailPresensiArrayList) {
        mContext = context;
        this.detailPresensiArrayList = detailPresensiArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.presensi_detail_card_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presensiMahasiswaName.setText(String.valueOf(detailPresensiArrayList.get(position).getMahasiswaId()));
        holder.presensiDateMahasiswa.setText(detailPresensiArrayList.get(position).getPresensiFilled().toString());
    }

    @Override
    public int getItemCount() {
        return detailPresensiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView presensiMahasiswaName;
        private AppCompatTextView presensiDateMahasiswa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiMahasiswaName = itemView.findViewById(R.id.presensi_detail_mahasiswa_name_view);
            presensiDateMahasiswa = itemView.findViewById(R.id.presensi_detail_mahasiswa_date_view);
        }
    }
}
