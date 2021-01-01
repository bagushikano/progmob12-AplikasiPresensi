package com.progmobklp12.aplikasipresensi;

import android.app.Application;

import com.progmobklp12.aplikasipresensi.database.AppDatabase;

public class App extends Application {
    public AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(this);
    }
}
