package com.progmobklp12.aplikasipresensi.model.dosen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListDosenResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Dosen> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Dosen> getData() {
        return data;
    }

    public void setData(List<Dosen> data) {
        this.data = data;
    }
}
