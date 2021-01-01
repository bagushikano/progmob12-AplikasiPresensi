package com.progmobklp12.aplikasipresensi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface MahasiswaDao {
    @Query("SELECT * FROM mahasiswa")
    List<Mahasiswa> getAll();

    @Update
    void updateMahasiswa(Mahasiswa mahasiswa);

    @Insert
    void insertAll(ArrayList<Mahasiswa> mahasiswas);

    @Delete
    void delete(Mahasiswa mahasiswa);

    @Query("DELETE FROM mahasiswa")
    void nukeTable();
}
