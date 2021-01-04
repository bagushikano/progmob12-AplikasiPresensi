package com.progmobklp12.aplikasipresensi;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.messaging.FirebaseMessaging;
import com.progmobklp12.aplikasipresensi.database.AppDatabase;

public class App extends Application {
    public AppDatabase appDatabase;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }
}
