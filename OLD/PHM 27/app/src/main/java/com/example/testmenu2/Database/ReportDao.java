package com.example.testmenu2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface ReportDao {
    @Insert
    public void addReport(Report report);

    @Update
    void updateReport (Report report);

    @Delete
    void deleteReport (Report report);

    @Query("SELECT * FROM reports")
    LiveData<List<Report>> getAllReports();

    @Query("SELECT * FROM reports WHERE id=:reportId")
    LiveData<Report> getReport(int reportId);

    @Query("SELECT * FROM reports WHERE report_giorno=:date")
    LiveData<List<Report>> getAllReportInDate(Date date);

    @Query("SELECT * FROM reports WHERE report_giorno>= :date1 AND report_giorno<=:date2")
    LiveData<List<Report>> getAllReportsInPeriod(Date date1, Date date2);

    @Query("SELECT * FROM reports ORDER BY report_giorno DESC, report_ora DESC")
    LiveData<List<Report>> getAllReportsOrder();

    @Query("SELECT AVG(report_battito) FROM reports WHERE report_giorno=:date AND report_battito != 0")
    LiveData<Double> getAVGBattito(Date date);

    @Query("SELECT AVG(report_pressione) FROM reports WHERE report_giorno=:date AND report_pressione != 0")
    LiveData<Double> getAVGPressione(Date date);

    @Query("SELECT AVG(report_temperatura) FROM reports WHERE report_giorno=:date AND report_temperatura != 0")
    LiveData<Double> getAVGTemperatura(Date date);

    @Query("SELECT AVG(report_glicemia) FROM reports WHERE report_giorno=:date AND report_glicemia != 0")
    LiveData<Double> getAVGGlicemia(Date date);


}
