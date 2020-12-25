package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Mahasiswa {
    @SerializedName("id_mahasiswa")
    @Expose
    private Integer idMahasiswa;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nim")
    @Expose
    private String nim;
    @SerializedName("username")
    @Expose
    private String username;

    public Integer getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(Integer idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
