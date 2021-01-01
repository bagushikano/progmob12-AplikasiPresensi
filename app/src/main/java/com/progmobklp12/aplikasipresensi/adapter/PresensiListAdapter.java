package com.progmobklp12.aplikasipresensi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.activity.dosen.DetailPresensiActivity;
import com.progmobklp12.aplikasipresensi.activity.dosen.EditPresensiActivity;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresensiListAdapter extends RecyclerView.Adapter<PresensiListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Presensi> presensiArrayList;
    private int position;

    private String presensiNameKey = "PRESENSINAME";
    private String presensiDescKey = "PRESENSIDESC";
    private String presensiDateOpenKey = "PRESENSIDATEOPEN";
    private String presensiDateCloseKey = "PRESENSIDATECLOSE";
    private String presensiIdKey = "PRESENSIID";
    private String presensiStatusKey = "PRESENSISTATUS";


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
        holder.presensiTitle.setText(presensiArrayList.get(position).getNamaPresensi());
        holder.presensiDesc.setText(presensiArrayList.get(position).getKeterangan());
        holder.presensiOpenDate.setText(String.format("Tanggal di buka: %s", presensiArrayList.get(position).getTanggalOpen()));
        holder.presensiCloseDate.setText(String.format("Tanggal di tutup: %s", presensiArrayList.get(position).getTanggalClose()));
        if (presensiArrayList.get(position).getIsOpen() == 1) {
            holder.openCloseButton.setText("Tutup");
        }
        else {
            holder.openCloseButton.setText("Buka");
        }
        //TODO di backendnya update juga tanggal buka sama tutup nya sesuai kapan buka/tutupnya
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
        private Button deletePresensiButton;
        private Button openCloseButton;
        private Button editPresensiButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiTitle = itemView.findViewById(R.id.presensi_title_view);
            presensiDesc = itemView.findViewById(R.id.presensi_desc_view);
            presensiOpenDate = itemView.findViewById(R.id.presensi_open_date_view);
            presensiCloseDate = itemView.findViewById(R.id.presensi_close_date_view);
            openCloseButton = itemView.findViewById(R.id.presensi_open_close_button);
            deletePresensiButton = itemView.findViewById(R.id.presensi_delete_button);
            editPresensiButton = itemView.findViewById(R.id.presensi_edit_button);

            deletePresensiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    BaseApi deletePresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
                    Call<MessageResponseModel> deletePresensiData = deletePresensi.deletePresensi(presensi.getIdPresensi());
                    deletePresensiData.enqueue(new Callback<MessageResponseModel>() {
                        @Override
                        public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                            if (response.body().getMessage().equals("Data berhasil di delete")) {
                                Toast.makeText(mContext, "Presensi berhasil di hapus!", Toast.LENGTH_SHORT).show();
                                //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                            }
                            else {
                                Toast.makeText(mContext, "Presensi gagal di hapus", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                            Toast.makeText(mContext, "Presensi gagal di hapus", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            openCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya

                    BaseApi openClose = RetrofitClient.buildRetrofit().create(BaseApi.class);
                    Call<MessageResponseModel> openClosePresensi;
                    if (presensi.getIsOpen() == 1) {
                        openClosePresensi = openClose.closePresensi(presensi.getIdPresensi());
                    }
                    else {
                        openClosePresensi = openClose.openPresensi(presensi.getIdPresensi());
                    }

                    openClosePresensi.enqueue(new Callback<MessageResponseModel>() {
                        @Override
                        public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                            if (response.body().getMessage().equals("Absensi berhasil di tutup")) {
                                Toast.makeText(mContext, "Presensi berhasil di tutup", Toast.LENGTH_SHORT).show();

                                openCloseButton.setText("Buka");
                                //TODO ada bug disini klo semisal koneksi ke server putus dia crash
                            }

                            else if (response.body().getMessage().equals("Absensi berhasil di buka")) {
                                Toast.makeText(mContext, "Presensi berhasil di buka", Toast.LENGTH_SHORT).show();
                                openCloseButton.setText("Tutup");
                            }

                            else {
                                Toast.makeText(mContext, "Status presensi gagal di update", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                            Toast.makeText(mContext, "Status presensi gagal di update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            editPresensiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    Intent editPresensi = new Intent(mContext, EditPresensiActivity.class);
                    editPresensi.putExtra(presensiIdKey, presensi.getIdPresensi());
                    editPresensi.putExtra(presensiNameKey, presensi.getNamaPresensi());
                    editPresensi.putExtra(presensiDescKey, presensi.getKeterangan());
                    editPresensi.putExtra(presensiDateOpenKey, presensi.getTanggalOpen());
                    editPresensi.putExtra(presensiDateCloseKey, presensi.getTanggalClose());
                    mContext.startActivity(editPresensi);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    Presensi presensi = presensiArrayList.get(position); //untuk dapetin posisi item dari arraylist yang di klik di cardnya
                    Intent detailPresensi = new Intent(mContext, DetailPresensiActivity.class);
                    detailPresensi.putExtra(presensiIdKey, presensi.getIdPresensi());
                    detailPresensi.putExtra(presensiNameKey, presensi.getNamaPresensi());
                    detailPresensi.putExtra(presensiDescKey, presensi.getKeterangan());
//                    detailPresensi.putExtra("PRESENSIDATEADDED", presensi.get);
                    detailPresensi.putExtra(presensiDateOpenKey, presensi.getTanggalOpen());
                    detailPresensi.putExtra(presensiDateCloseKey, presensi.getTanggalClose());
                    detailPresensi.putExtra(presensiStatusKey, presensi.getIsOpen());
                    mContext.startActivity(detailPresensi);
                }
            });
        }
    }
}
