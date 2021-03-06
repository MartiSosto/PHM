package com.example.personalhealthmonitor.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;


import com.example.personalhealthmonitor.Utilities.Converters;

import java.util.Date;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportDao reportDao;
    private DB reportDB;

    public ReportViewModel(Application application){
        super(application);
        reportDB = DB.getDatabase(application);
        reportDao = reportDB.reportDao();
    }

    public void setReport(Report report){
        new InsertAsyncTask(reportDao).execute(report);
    }

    public void updateReport(Report report){
        new UpdateAsyncTask(reportDao).execute(report);
    }

    public void deleteReport(Report report){
        new DeleteAsyncTask(reportDao).execute(report);
    }

    public Report getReport(int reportId){
        return reportDao.getReport(reportId);
    }

    public LiveData<List<Report>> getAllReports(int giorno, int mese, int anno){
        String query = "SELECT * FROM reports ";
        //prendo tutti i report
        if(giorno == 0 && mese == 0 && anno == 0) return reportDao.getAllReports(new SimpleSQLiteQuery(query));
        //prendo i report di un giorno specifico
        if(giorno != 0 && mese != 0 && anno != 0)
            return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno = "+ giorno+ " AND report_mese = "+ mese + " AND report_anno = "+ anno));
        return null;
    }

    public LiveData<Double> getAvgVal(String val, int giorno, int mese, int anno){
        String query = "SELECT AVG("+ val +") FROM reports WHERE report_giorno = "+ giorno+ " AND report_mese = "+ mese + " AND report_anno = "+ anno+ " AND "+ val+ " != 0";
        return reportDao.getAVGVal(new SimpleSQLiteQuery(query));
    }


    //OPERAZIONI ASYNC
    private class OperationAsyncTask extends AsyncTask<Report, Void, Void> {

        ReportDao AsyncTaskDao;

        public OperationAsyncTask(ReportDao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            return null;
        }
    }

    //OPERAZIONE DI INSERIMENTO
    private class InsertAsyncTask extends OperationAsyncTask{

        public InsertAsyncTask(ReportDao reportDao) {
            super(reportDao);
        }

        @Override
        protected Void doInBackground(Report... reports) {
            AsyncTaskDao.addReport(reports[0]);
            return null;
        }
    }

    //OPERAZIONE DI MODIFICA
    private class UpdateAsyncTask extends AsyncTask<Report, Void, Void> {

        ReportDao mreportDao;

        public UpdateAsyncTask(ReportDao reportDao) {
            this.mreportDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            mreportDao.updateReport(reports[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Report, Void, Void> {
        ReportDao mreportDao;

        public DeleteAsyncTask(ReportDao reportDao) {
            this.mreportDao = reportDao;
        }


        @Override
        protected Void doInBackground(Report... reports) {
            mreportDao.deleteReport(reports[0]);
            return null;
        }
    }
}
