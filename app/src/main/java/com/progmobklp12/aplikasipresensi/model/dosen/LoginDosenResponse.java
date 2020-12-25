package com.progmobklp12.aplikasipresensi.model.dosen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDosenResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LoginDosenData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginDosenData getData() {
        return data;
    }

    public void setData(LoginDosenData data) {
        this.data = data;
    }
}
