package com.progmobklp12.aplikasipresensi.model.presensi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;

public class Presensi {
    @SerializedName("id_presensi")
    @Expose
    private Integer idPresensi;
    @SerializedName("id_dosen")
    @Expose
    private Integer idDosen;
    @SerializedName("nama_presensi")
    @Expose
    private String namaPresensi;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("tanggal_open")
    @Expose
    private String tanggalOpen;
    @SerializedName("tanggal_close")
    @Expose
    private String tanggalClose;
    @SerializedName("is_open")
    @Expose
    private Integer isOpen;
    @SerializedName("dosen")
    @Expose
    private Dosen dosen;

    public Integer getIdPresensi() {
        return idPresensi;
    }

    public void setIdPresensi(Integer idPresensi) {
        this.idPresensi = idPresensi;
    }

    public Integer getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(Integer idDosen) {
        this.idDosen = idDosen;
    }

    public String getNamaPresensi() {
        return namaPresensi;
    }

    public void setNamaPresensi(String namaPresensi) {
        this.namaPresensi = namaPresensi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggalOpen() {
        return tanggalOpen;
    }

    public void setTanggalOpen(String tanggalOpen) {
        this.tanggalOpen = tanggalOpen;
    }

    public String getTanggalClose() {
        return tanggalClose;
    }

    public void setTanggalClose(String tanggalClose) {
        this.tanggalClose = tanggalClose;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Dosen getDosen() {
        return dosen;
    }

    public void setDosen(Dosen dosen) {
        this.dosen = dosen;
    }
}
