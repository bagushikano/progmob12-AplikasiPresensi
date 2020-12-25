package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginMahasiswaResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LoginMahasiswaData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginMahasiswaData getData() {
        return data;
    }

    public void setData(LoginMahasiswaData data) {
        this.data = data;
    }
}
