package com.progmobklp12.aplikasipresensi.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.DetailPresensiActivity;
import com.progmobklp12.aplikasipresensi.activity.dosen.EditPresensiActivity;
import com.progmobklp12.aplikasipresensi.activity.mahasiswa.DetailFilledPresensiActivity;
import com.progmobklp12.aplikasipresensi.activity.mahasiswa.FillPresensiActivity;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensiResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiMahasiswaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresensiListMahasiswaAdapter extends RecyclerView.Adapter<PresensiListMahasiswaAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Presensi> presensiArrayList;
    private int position;

    private String presensiNameKey = "PRESENSINAME";
    private String presensiDescKey = "PRESENSIDESC";
    private String presensiDateOpenKey = "PRESENSIDATEOPEN";
    private String presensiDateCloseKey = "PRESENSIDATECLOSE";
    private String presensiIdKey = "PRESENSIID";
    private String presensiStatusKey = "PRESENSISTATUS";
    private String presensiOwnerKey = "PRESENSIOWNER";

    SharedPreferences loginPreferences;
    String username;

    public PresensiListMahasiswaAdapter(Context context, ArrayList<Presensi> presensiArrayList) {
        mContext = context;
        this.presensiArrayList = presensiArrayList;
    }

    @NonNull
    @Override
    public PresensiListMahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mahassiwa_presensi_card_list, parent, false);
        return new PresensiListMahasiswaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresensiListMahasiswaAdapter.ViewHolder holder, int position) {
        holder.presensiTitle.setText(presensiArrayList.get(position).getNamaPresensi());
        holder.presensiDesc.setText(presensiArrayList.get(position).getKeterangan());
        holder.presensiOwner.setText(String.format("Pemilik presensi: %s", presensiArrayList.get(position).getDosen().getNama()));
        holder.presensiOpenDate.setText(String.format("Tanggal di buka: %s", presensiArrayList.get(position).getTanggalOpen()));
        holder.presensiCloseDate.setText(String.format("Tanggal di tutup: %s", presensiArrayList.get(position).getTanggalClose()));
        loginPreferences = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = loginPreferences.getString("username", "kosong");
        BaseApi getDetailPresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
        Call<DetailPresensiResponse> detailPresensiResponseCall = getDetailPresensi.detailPresensiMahasiswa(username, presensiArrayList.get(position).getIdPresensi());
        detailPresensiResponseCall.enqueue(new Callback<DetailPresensiResponse>() {
            @Override
            public void onResponse(Call<DetailPresensiResponse> call, Response<DetailPresensiResponse> response) {
                if (response.body().getMessage().equals("Presensi telah di isi")) {
                    holder.fillPresensiButton.setText("Anda telah mengisi presensi ini");
                    holder.fillPresensiButton.setEnabled(false);
                    holder.itemView.setEnabled(true);
                    //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                }
                else {
                    if(presensiArrayList.get(position).getIsOpen() == 0) {
                        holder.fillPresensiButton.setText("Presensi di tutup");
                        holder.fillPresensiButton.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                    else {
                        holder.itemView.setEnabled(false);
                        holder.fillPresensiButton.setText("Isi presensi");
                    }
                }
            }
            @Override
            public void onFailure(Call<DetailPresensiResponse> call, Throwable t) {
            }
        });
    }
    @Override
    public int getItemCount() {
        return presensiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView presensiTitle;
        private AppCompatTextView presensiDesc;
        private AppCompatTextView presensiOpenDate;
        private AppCompatTextView presensiCloseDate;
        private AppCompatTextView presensiOwner;
        private Button fillPresensiButton;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiTitle = itemView.findViewById(R.id.presensi_title_view);
            presensiDesc = itemView.findViewById(R.id.presensi_desc_view);
            presensiOpenDate = itemView.findViewById(R.id.presensi_open_date_view);
            presensiCloseDate = itemView.findViewById(R.id.presensi_close_date_view);
            presensiOwner = itemView.findViewById(R.id.presensi_owner_view);
            fillPresensiButton = itemView.findViewById(R.id.presensi_fill_button);


            fillPresensiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    Intent fillPresensi = new Intent(mContext, FillPresensiActivity.class);
                    fillPresensi.putExtra(presensiIdKey, presensi.getIdPresensi());
                    fillPresensi.putExtra(presensiNameKey, presensi.getNamaPresensi());
                    fillPresensi.putExtra(presensiDescKey, presensi.getKeterangan());
//                    detailPresensi.putExtra("PRESENSIDATEADDED", presensi.get);
                    fillPresensi.putExtra(presensiOwnerKey, presensiArrayList.get(position).getDosen().getNama());
                    fillPresensi.putExtra(presensiDateOpenKey, presensi.getTanggalOpen());
                    fillPresensi.putExtra(presensiDateCloseKey, presensi.getTanggalClose());
                    fillPresensi.putExtra(presensiStatusKey, presensi.getIsOpen());
                    mContext.startActivity(fillPresensi);

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    Intent filledPresensi = new Intent(mContext, DetailFilledPresensiActivity.class);
                    filledPresensi.putExtra(presensiIdKey, presensi.getIdPresensi());
                    filledPresensi.putExtra(presensiNameKey, presensi.getNamaPresensi());
                    filledPresensi.putExtra(presensiDescKey, presensi.getKeterangan());
//                    detailPresensi.putExtra("PRESENSIDATEADDED", presensi.get);
                    filledPresensi.putExtra(presensiOwnerKey, presensiArrayList.get(position).getDosen().getNama());
                    filledPresensi.putExtra(presensiDateOpenKey, presensi.getTanggalOpen());
                    filledPresensi.putExtra(presensiDateCloseKey, presensi.getTanggalClose());
                    filledPresensi.putExtra(presensiStatusKey, presensi.getIsOpen());
                    mContext.startActivity(filledPresensi);
                }
            });
        }
    }

}
