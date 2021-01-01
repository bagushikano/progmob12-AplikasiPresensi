package com.progmobklp12.aplikasipresensi.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.progmobklp12.aplikasipresensi.dao.DosenDao;
import com.progmobklp12.aplikasipresensi.dao.MahasiswaDao;
import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

@Database(entities = {Mahasiswa.class, Dosen.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MahasiswaDao mahasiswaDao();
    public abstract DosenDao dosenDao();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "db-app").build();
                }
            }
        }
        return instance;
    }
}
