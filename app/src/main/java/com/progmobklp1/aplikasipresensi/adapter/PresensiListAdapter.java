package com.progmobklp1.aplikasipresensi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.progmobklp1.aplikasipresensi.R;
import com.progmobklp1.aplikasipresensi.activity.DetailPresensiActivity;
import com.progmobklp1.aplikasipresensi.fragment.PresensiFragment;
import com.progmobklp1.aplikasipresensi.model.Presensi;

import java.util.ArrayList;

public class PresensiListAdapter extends RecyclerView.Adapter<PresensiListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Presensi> presensiArrayList;
    private int position;

    public PresensiListAdapter(Context context, ArrayList<Presensi> presensiArrayList) {
        mContext = context;
        this.presensiArrayList = presensiArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.presensi_card_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presensiTitle.setText(presensiArrayList.get(position).getPresensiName());
        holder.presensiDesc.setText(presensiArrayList.get(position).getPresensiDesc());
        holder.presensiOpenDate.setText(presensiArrayList.get(position).getPresensiOpen().toString());
    }

    @Override
    public int getItemCount() {
        return presensiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView presensiTitle;
        private AppCompatTextView presensiDesc;
        private AppCompatTextView presensiOpenDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiTitle = itemView.findViewById(R.id.presensi_title_view);
            presensiDesc = itemView.findViewById(R.id.presensi_desc_view);
            presensiOpenDate = itemView.findViewById(R.id.presensi_open_date_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    Intent detailPresensi = new Intent(mContext, DetailPresensiActivity.class);
                    detailPresensi.putExtra("PRESENSIID", presensi.getId());
                    detailPresensi.putExtra("PRESENSINAME", presensi.getPresensiName());
                    detailPresensi.putExtra("PRESENSIDATEADDED", presensi.getPresensiAdded().toString());
                    detailPresensi.putExtra("PRESENSIDATEOPEN", presensi.getPresensiOpen().toString());
                    detailPresensi.putExtra("PRESENSIDATECLOSE", presensi.getPresensiClosed().toString());
                    mContext.startActivity(detailPresensi);
                }
            });
        }
    }
}
