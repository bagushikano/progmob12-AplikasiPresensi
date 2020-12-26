package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginMahasiswaResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Mahasiswa data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Mahasiswa getData() {
        return data;
    }

    public void setData(Mahasiswa data) {
        this.data = data;
    }
}
