package com.progmobklp12.aplikasipresensi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.progmobklp12.aplikasipresensi.R;
import com.progmobklp12.aplikasipresensi.api.BaseApi;
import com.progmobklp12.aplikasipresensi.api.RetrofitClient;
import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensi;
import com.progmobklp12.aplikasipresensi.model.presensi.Presensi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresensiListAdapter extends RecyclerView.Adapter<DetailPresensiListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DetailPresensi> detailPresensiArrayList;
    private int position;
    private ProgressDialog dialog;

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
        holder.presensiMahasiswaName.setText(detailPresensiArrayList.get(position).getMahasiswa().getNama() + " (" + detailPresensiArrayList.get(position).getMahasiswa().getNim() + ")");
        holder.presensiDateMahasiswa.setText(String.format("Tanggal terabsen: %s", detailPresensiArrayList.get(position).getDateFilled()));
        if (detailPresensiArrayList.get(position).getIsApproved() == 1) {
            holder.presensiStatusMahasiswa.setText("Status: Approved");
            holder.approveButton.setEnabled(false);
        }
        else {
            holder.presensiStatusMahasiswa.setText("Status: Not approved");
            holder.declineButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return detailPresensiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView presensiMahasiswaName;
        private AppCompatTextView presensiDateMahasiswa;
        private AppCompatTextView presensiStatusMahasiswa;
        private Button approveButton;
        private Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiMahasiswaName = itemView.findViewById(R.id.presensi_detail_mahasiswa_name_view);
            presensiDateMahasiswa = itemView.findViewById(R.id.presensi_detail_mahasiswa_date_view);
            presensiStatusMahasiswa = itemView.findViewById(R.id.presensi_detail_mahasiswa_status_view);
            approveButton = itemView.findViewById(R.id.approve_presensi_button);
            declineButton = itemView.findViewById(R.id.decline_presensi_button);


            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new ProgressDialog(mContext);
                    dialog.setMessage("Mohon tunggu...");
                    dialog.show();
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    DetailPresensi detailPresensi = detailPresensiArrayList.get(position);
                    BaseApi approvePresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
                    Call<MessageResponseModel> approvePresensiCall = approvePresensi.approvePresensi(detailPresensi.getIdDetailPresensi());
                    approvePresensiCall.enqueue(new Callback<MessageResponseModel>() {
                        @Override
                        public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                            if (response.code() == 200) {
                                if (response.body().getMessage().equals("Absensi approved")) {
                                    Toast.makeText(mContext, String.format("Presensi %s berhasil di approve!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                                    presensiStatusMahasiswa.setText("Status: Approved");
                                    approveButton.setEnabled(false);
                                    declineButton.setEnabled(true);
                                }
                                else {
                                    Toast.makeText(mContext, String.format("Presensi %s gagal di approve!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(mContext, String.format("Presensi %s gagal di approve!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                            Toast.makeText(mContext, String.format("Presensi %s gagal di approve!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new ProgressDialog(mContext);
                    dialog.setMessage("Mohon tunggu...");
                    dialog.show();
                    int position = getAdapterPosition(); // untuk mendapatkan posisi di adapter
                    DetailPresensi detailPresensi = detailPresensiArrayList.get(position);
                    BaseApi declinePresensi = RetrofitClient.buildRetrofit().create(BaseApi.class);
                    Call<MessageResponseModel> declinePresensiCall = declinePresensi.declinePresensi(detailPresensi.getIdDetailPresensi());
                    declinePresensiCall.enqueue(new Callback<MessageResponseModel>() {
                        @Override
                        public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                            if (response.code() == 200) {
                                if (response.body().getMessage().equals("Absensi decline")) {
                                    Toast.makeText(mContext, String.format("Presensi %s berhasil di decline!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                                    presensiStatusMahasiswa.setText("Status: Not approved");
                                    declineButton.setEnabled(false);
                                    approveButton.setEnabled(true);
                                }
                                else {
                                    Toast.makeText(mContext, String.format("Presensi %s gagal di decline!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(mContext, String.format("Presensi %s gagal di decline!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                            Toast.makeText(mContext, String.format("Presensi %s gagal di decline!", detailPresensi.getMahasiswa().getNama()), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
