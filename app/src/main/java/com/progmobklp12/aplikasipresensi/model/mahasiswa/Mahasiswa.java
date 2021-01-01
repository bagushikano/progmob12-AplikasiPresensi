package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Mahasiswa {

    @SerializedName("id_mahasiswa")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private Integer idMahasiswa;

    @SerializedName("nama")
    @Expose
    @ColumnInfo(name = "nama")
    private String nama;

    @SerializedName("nim")
    @Expose
    @ColumnInfo(name = "nim")
    private String nim;

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username")
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
