package com.progmobklp1.aplikasipresensi.model;

import java.util.Date;

public class Presensi {

    int id;
    String presensiName;
    String presensiDesc;
    Date presensiAdded;
    Date presensiOpen;
    Date presensiClosed;

    public Presensi(int id, String presensiName, String presensiDesc, Date presensiAdded, Date presensiOpen, Date presensiClosed) {
        this.id = id;
        this.presensiName = presensiName;
        this.presensiDesc = presensiDesc;
        this.presensiAdded = presensiAdded;
        this.presensiOpen = presensiOpen;
        this.presensiClosed = presensiClosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPresensiDesc() {
        return presensiDesc;
    }

    public void setPresensiDesc(String presensiDesc) {
        this.presensiDesc = presensiDesc;
    }

    public String getPresensiName() {
        return presensiName;
    }

    public void setPresensiName(String presensiName) {
        this.presensiName = presensiName;
    }

    public Date getPresensiAdded() {
        return presensiAdded;
    }

    public void setPresensiAdded(Date presensiAdded) {
        this.presensiAdded = presensiAdded;
    }

    public Date getPresensiOpen() {
        return presensiOpen;
    }

    public void setPresensiOpen(Date presensiOpen) {
        this.presensiOpen = presensiOpen;
    }

    public Date getPresensiClosed() {
        return presensiClosed;
    }

    public void setPresensiClosed(Date presensiClosed) {
        this.presensiClosed = presensiClosed;
    }
}
