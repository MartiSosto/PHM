package com.example.personalhealthmonitor.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.personalhealthmonitor.Database.Settings;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.Utility;
import java.util.Calendar;
import static com.example.personalhealthmonitor.Utilities.Utility.*;

public class Alarm extends Service {
    private static AlarmManager alarmManagerReportDaily;
    private static PendingIntent pendingIntentReportDaily;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NOTIFICATION ALARM", "CREATED");
        //Creo il canale delle notifiche
        createNotificationChannel();

        //Setto le notifiche giornaliere
        alarmManagerReportDaily = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentDaily = new Intent(getApplicationContext(), Notification_receiver.class);
        intentDaily.setAction(KEY_REPORT_DAILY);
        pendingIntentReportDaily = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intentDaily, PendingIntent.FLAG_UPDATE_CURRENT);

        reportViewModel.getAllReports(Converters.StringToDate(SDF.format(Calendar.getInstance().getTime())), null).observeForever(reports -> {
            setDailyNotification();
            setWarningNotification();
        });
    }

    //CONTROLLA CHE LE NOTIFICHE GIORNALIERE SIANO ON O OFF
    public void setDailyNotification(){
        notificationViewModel.getNotification().observeForever(notification -> {
            if(notification.isStatus()){
                Log.i("NOTIFICATION ALARM", "ON");
                new checkTodayReports().execute(notification.getOra(), notification.getMinuti());
            }
            else{
                Log.i("NOTIFICATION ALARM", "OFF");
                alarmManagerReportDaily.cancel(pendingIntentReportDaily);
            }
        });
    }

    //CONTROLLA SE OGGI SONO STATI AGGIUNTI REPORT: SE NON SONO STATI AGGIUNTI OGGI ALLORA INVIA LA NOTIFICA OGGI, ALTRIMENTI LA INVIA DOMANI
    private static class checkTodayReports extends AsyncTask <Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... integers) {
            Calendar alarmClock = Calendar.getInstance();
            alarmClock.set(Calendar.HOUR_OF_DAY, integers[0]);
            alarmClock.set(Calendar.MINUTE, integers[1]);
            alarmClock.set(Calendar.SECOND, 0);
            //se oggi ho gia' inserito report oppure se non ho ancora inserito nulla ma l'orario prefissato per inviare le notifiche è passato allora la invia domani
            if(reportViewModel.getCOUNTVal(null, null, Converters.StringToDate(SDF.format(Calendar.getInstance().getTime())), null) != 0 || Converters.DateToLong(alarmClock.getTime()) < Converters.DateToLong(Calendar.getInstance().getTime()))
                alarmClock.add(Calendar.DATE, 1); //se l'orario è passato allora lo invio domani

            Log.i("NOTIFICATION ALARM", Converters.DateToString(alarmClock.getTime())+ " alle "+ alarmClock.getTime().getHours()+":"+ alarmClock.getTime().getMinutes());
            alarmManagerReportDaily.setRepeating(android.app.AlarmManager.RTC_WAKEUP, alarmClock.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReportDaily);
            return null;
        }
    }

    //CONTROLLA SE HO DEI VALORI CON PRIORITA' MAGGIORE O UGUALE A 3 , SE I LORO VALORI SONO SOPRA IL LIMITE ALLORA INVIA LA NOTIFICA
    private void setWarningNotification(){
        settingsViewModel.getmAllSettingsFilter(3).observeForever(list -> {
            Settings[] set = new Settings[list.size()];
            for(int i = 0; i < list.size(); i++){
                set[i] = list.get(i);
            }
            new AVGSettings().execute(set);
        });
    }

    //PRENDO I VALORI MEDI NEL PERIODO INDICATO E, SE SOPRA IL LIMITE, AGGIUNGO LE STRIGHE DI ALLARME A WARNING STRING
    public class AVGSettings extends AsyncTask<Settings, Void, String>{
        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);
            Log.i("WARNING STRING FINAL", aString);
            if(!aString.matches("")){
                //Log.i("WARNING STRING NOTIFICA", WarningString);
                Calendar alarmClock = Calendar.getInstance();
                alarmClock.set(Calendar.HOUR_OF_DAY, 9);
                alarmClock.set(Calendar.MINUTE, 0);
                alarmClock.set(Calendar.SECOND, 0);
                alarmClock.add(Calendar.DATE, 1);

                //Setto le notifiche di warning
                AlarmManager alarmManagerReportWarning = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intentWarning = new Intent(getApplicationContext(), Notification_receiver.class);
                intentWarning.setAction(KEY_WARNING);
                intentWarning.putExtra("WarningString", aString);
                PendingIntent pendingIntentReportWarning = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intentWarning, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.i("WARNING ALARM", Converters.DateToString(alarmClock.getTime())+ " alle "+ alarmClock.getTime().getHours()+":"+ alarmClock.getTime().getMinutes());
                alarmManagerReportWarning.setRepeating(android.app.AlarmManager.RTC_WAKEUP, alarmClock.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReportWarning);
            }
        }
//
        @Override
        protected String doInBackground(Settings... settings) {
            String warning ="";
            for (Settings mSettings : settings) {
                Double avg = reportViewModel.getAvgVal(mSettings.getValore(), mSettings.getInizio(), mSettings.getFine());
                if (avg != null && avg > mSettings.getLimite()) {
                    warning += Utility.KeyToPrompt(mSettings.getValore()) + ": " + tronca(avg) + "/" + mSettings.getLimite() + " tra il " + Converters.DateToString(mSettings.getInizio()) + " e il " + Converters.DateToString(mSettings.getFine()) + "\n";
                    //Log.i("WARNING STRING", WarningString);
                }
            }
            return warning;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //CREO IL CANALE DELLE NOTIFICHE
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Personal Notification";
            String description = "Descrizione";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
