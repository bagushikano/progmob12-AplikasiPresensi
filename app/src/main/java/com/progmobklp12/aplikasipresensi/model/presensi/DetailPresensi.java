package com.progmobklp12.aplikasipresensi.model.presensi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

public class DetailPresensi {
    @SerializedName("id_detail_presensi")
    @Expose
    private Integer idDetailPresensi;
    @SerializedName("id_presensi")
    @Expose
    private Integer idPresensi;
    @SerializedName("id_mahasiswa")
    @Expose
    private Integer idMahasiswa;
    @SerializedName("date_filled")
    @Expose
    private String dateFilled;
    @SerializedName("is_approved")
    @Expose
    private Integer isApproved;
    @SerializedName("mahasiswa")
    @Expose
    private Mahasiswa mahasiswa;

    public Integer getIdDetailPresensi() {
        return idDetailPresensi;
    }

    public void setIdDetailPresensi(Integer idDetailPresensi) {
        this.idDetailPresensi = idDetailPresensi;
    }

    public Integer getIdPresensi() {
        return idPresensi;
    }

    public void setIdPresensi(Integer idPresensi) {
        this.idPresensi = idPresensi;
    }

    public Integer getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(Integer idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getDateFilled() {
        return dateFilled;
    }

    public void setDateFilled(String dateFilled) {
        this.dateFilled = dateFilled;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }
}
