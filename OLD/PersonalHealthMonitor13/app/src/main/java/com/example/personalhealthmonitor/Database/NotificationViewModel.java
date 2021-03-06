package com.example.personalhealthmonitor.Database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_NOTIFICATION;

public class NotificationViewModel extends AndroidViewModel {

    private NotificationDao notificationDao;
    private DB notificationDB;
    private LiveData<Notification> Notification;

    public NotificationViewModel(Application application){
        super(application);

        notificationDB = DB.getDatabase(application);
        notificationDao = notificationDB.notificationDao();
        Notification = notificationDao.getNotification();
    }

    public void insertNotification(Notification Notification){
        new InsertAsyncTask(notificationDao).execute(Notification);
    }

    public void updateNotification(Notification Notification){
        new UpdateAsyncTask(notificationDao).execute(Notification);
    }

    public void deleteNotification(Notification Notification){
        new DeleteAsyncTask(notificationDao).execute(Notification);
    }

    public LiveData<Notification> getNotification(){
        return Notification;
    }

    //OPERAZIONI ASYNC
    private class OperationAsyncTask extends AsyncTask<Notification, Void, Void> {

        NotificationDao AsyncTaskDao;

        public OperationAsyncTask(NotificationDao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Notification... Notifications) {
            return null;
        }
    }

    //OPERAZIONE DI INSERIMENTO
    private class InsertAsyncTask extends OperationAsyncTask{

        public InsertAsyncTask(NotificationDao NotificationDao) {
            super(NotificationDao);
        }

        @Override
        protected Void doInBackground(Notification... Notifications) {
            AsyncTaskDao.addNotification(Notifications[0]);
            return null;
        }
    }

    //OPERAZIONE DI MODIFICA
    private class UpdateAsyncTask extends AsyncTask<Notification, Void, Void> {

        NotificationDao mNotificationDao;

        public UpdateAsyncTask(NotificationDao NotificationDao) {
            this.mNotificationDao = NotificationDao;
        }

        @Override
        protected Void doInBackground(Notification... Notifications) {
            notificationDao.updateNotification(Notifications[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Notification, Void, Void> {
        NotificationDao mNotificationDao;

        public DeleteAsyncTask(NotificationDao NotificationDao) {
            this.mNotificationDao = NotificationDao;
        }


        @Override
        protected Void doInBackground(Notification... Notifications) {
            notificationDao.deleteNotification(Notifications[0]);
            return null;
        }
    }
}
