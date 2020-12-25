package com.progmobklp12.aplikasipresensi.model.presensi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PresensiEditResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Presensi data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Presensi getData() {
        return data;
    }

    public void setData(Presensi data) {
        this.data = data;
    }
}
