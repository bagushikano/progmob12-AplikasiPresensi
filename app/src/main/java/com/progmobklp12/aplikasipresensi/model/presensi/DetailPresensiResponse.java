package com.progmobklp12.aplikasipresensi.model.presensi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailPresensiResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<DetailPresensi> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DetailPresensi> getData() {
        return data;
    }

    public void setData(List<DetailPresensi> data) {
        this.data = data;
    }
}
