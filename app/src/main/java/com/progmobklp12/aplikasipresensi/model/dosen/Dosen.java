package com.progmobklp12.aplikasipresensi.model.dosen;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Dosen {

    @SerializedName("id_dosen")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private Integer idDosen;

    @SerializedName("nama")
    @Expose
    @ColumnInfo(name = "nama")
    private String nama;

    @SerializedName("nip")
    @Expose
    @ColumnInfo(name = "nip")
    private String nip;

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username")
    private String username;

    public Integer getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(Integer idDosen) {
        this.idDosen = idDosen;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
