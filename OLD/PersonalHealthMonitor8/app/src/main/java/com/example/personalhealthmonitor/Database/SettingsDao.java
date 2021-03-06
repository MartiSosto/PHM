package com.example.personalhealthmonitor.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SettingsDao {

    @Insert
     void addSettings(Settings settings);

    @Update
    void updateSettings(Settings settings);

    @Delete
    void deleteSettings (Settings settings);

    @Query("SELECT * FROM settings")
    LiveData<List<Settings>> getAllSetting();
}
