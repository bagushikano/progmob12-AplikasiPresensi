package com.progmobklp1.aplikasipresensi.model;

public class Mahasiswa {
    private String nama;
    private String nim;
    private String prodi;
    private int id;

    public Mahasiswa(String Nama, String Nim, String Prodi, int Id) {
        nama = Nama;
        nim = Nim;
        prodi = Prodi;
        id = Id;
    }

    public String getMhsName() {
        return nama;
    }

    public void setMhsName(String Nama) {
        nama = Nama;
    }

    public String getMhsNim() {
        return nim;
    }

    public void setMhsNim(String Nim) {
        nim = Nim;
    }

    public String getMhsProdi() {
        return prodi;
    }

    public void setMhsProdi(String Prodi) {
        prodi = Prodi;
    }

    public int getMhsId() {
        return id;
    }

    public void setMhsId(int Id) {
        id = Id;
    }
}
