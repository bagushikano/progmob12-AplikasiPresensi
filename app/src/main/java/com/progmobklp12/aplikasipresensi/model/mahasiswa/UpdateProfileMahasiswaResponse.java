package com.progmobklp12.aplikasipresensi.model.mahasiswa;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileMahasiswaResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Mahasiswa> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Mahasiswa> getData() {
        return data;
    }

    public void setData(List<Mahasiswa> data) {
        this.data = data;
    }

}
