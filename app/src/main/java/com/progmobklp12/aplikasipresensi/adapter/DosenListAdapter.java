package com.progmobklp12.aplikasipresensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;

import java.util.ArrayList;

public class DosenListAdapter extends RecyclerView.Adapter<DosenListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Dosen> dosenArrayList;
    private int position;

    public DosenListAdapter(Context context, ArrayList<Dosen> dosenArrayList) {
        mContext = context;
        this.dosenArrayList = dosenArrayList;
    }

    @NonNull
    @Override
    public DosenListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dosen_card_list, parent, false);
        return new DosenListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DosenListAdapter.ViewHolder holder, int position) {
        holder.dosenListName.setText(dosenArrayList.get(position).getNama());
        holder.dosenListNip.setText(dosenArrayList.get(position).getNip());
    }

    @Override
    public int getItemCount() {
        return dosenArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView dosenListName;
        private AppCompatTextView dosenListNip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dosenListName = itemView.findViewById(R.id.dosen_list_name_view);
            dosenListNip = itemView.findViewById(R.id.dosen_list_nip_view);
        }
    }
}
