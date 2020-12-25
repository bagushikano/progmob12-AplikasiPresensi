package com.progmobklp12.aplikasipresensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;

public class MahasiswaListAdapter extends RecyclerView.Adapter<MahasiswaListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Mahasiswa> mahasiswaArrayList;
    private int position;

    public MahasiswaListAdapter(Context context, ArrayList<Mahasiswa> mahasiswaArrayList) {
        mContext = context;
        this.mahasiswaArrayList = mahasiswaArrayList;
    }

    @NonNull
    @Override
    public MahasiswaListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mahasiswa_card_list, parent, false);
        return new MahasiswaListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaListAdapter.ViewHolder holder, int position) {
        holder.mahasiswaListName.setText(mahasiswaArrayList.get(position).getNama());
        holder.mahasiswaListNim.setText(mahasiswaArrayList.get(position).getNim());
    }

    @Override
    public int getItemCount() {
        return mahasiswaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView mahasiswaListName;
        private AppCompatTextView mahasiswaListNim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mahasiswaListName = itemView.findViewById(R.id.mahasiswa_list_name_view);
            mahasiswaListNim = itemView.findViewById(R.id.mahasiswa_list_nim_view);
        }
    }
}
