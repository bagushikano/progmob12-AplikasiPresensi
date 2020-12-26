package com.progmobklp12.aplikasipresensi.model.dosen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterDosenResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Dosen data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Dosen getData() {
        return data;
    }

    public void setData(Dosen data) {
        this.data = data;
    }
}
