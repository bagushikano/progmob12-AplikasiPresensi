package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterMahasiswaResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private RegisterMahasiswaData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegisterMahasiswaData getData() {
        return data;
    }

    public void setData(RegisterMahasiswaData data) {
        this.data = data;
    }
}
