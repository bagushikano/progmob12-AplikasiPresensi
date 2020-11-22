package com.progmobklp1.aplikasipresensi.model;

import java.util.Date;

public class DetailPresensi {

    int id;
    int idPresensi;
    int mahasiswaId;
    Date presensiFilled;

    public DetailPresensi(int id, int idPresensi, int mahasiswaId, Date presensiFilled) {
        this.id = id;
        this.idPresensi = idPresensi;
        this.mahasiswaId = mahasiswaId;
        this.presensiFilled = presensiFilled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPresensi() {
        return idPresensi;
    }

    public void setIdPresensi(int idPresensi) {
        this.idPresensi = idPresensi;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public Date getPresensiFilled() {
        return presensiFilled;
    }

    public void setPresensiFilled(Date presensiFilled) {
        this.presensiFilled = presensiFilled;
    }
}
