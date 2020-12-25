package com.progmobklp12.aplikasipresensi.model.dosen;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileDosenResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UpdateProfileDosenData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UpdateProfileDosenData> getData() {
        return data;
    }

    public void setData(List<UpdateProfileDosenData> data) {
        this.data = data;
    }

}
