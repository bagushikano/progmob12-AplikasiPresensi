package com.progmobklp12.aplikasipresensi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.progmobklp12.aplikasipresensi.model.dosen.Dosen;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.Mahasiswa;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DosenDao {
    @Query("SELECT * FROM dosen")
    List<Dosen> getAll();

    @Update
    void updateDosen(Dosen dosen);

    @Insert
    void insertAll(ArrayList<Dosen> dosens);

    @Delete
    void delete(Dosen dosen);

    @Query("DELETE FROM dosen")
    void nukeTable();
}
