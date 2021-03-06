package com.example.personalhealthmonitor.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import com.example.personalhealthmonitor.Home.HomeFragment;
import com.example.personalhealthmonitor.Home.NewReportActivity;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Statistiche.StatisticheFragment;
import com.example.personalhealthmonitor.Utilities.Converters;

import java.util.Calendar;

import static com.example.personalhealthmonitor.MainActivity.KEY_REPORT_DAILY;
import static com.example.personalhealthmonitor.MainActivity.KEY_RIMANDA;
import static com.example.personalhealthmonitor.MainActivity.KEY_WARNING;
import static com.example.personalhealthmonitor.MainActivity.SDF;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.Notification.Alarm.CHANNEL_ID;
import static com.example.personalhealthmonitor.Notification.Alarm.NOTIFICATION_ID;

public class Notification_receiver extends BroadcastReceiver {
    public static final String KEY_REPORT_DAILY = "NOTIFY DAILY";
    public static final String KEY_WARNING = "NOTIFY WARNING";
    public static final String KEY_RIMANDA = "NOTIFY RIMANDA";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RECEIVER", "");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        if (intent.getAction().equals(KEY_REPORT_DAILY)) {
            notificationManagerCompat.notify(NOTIFICATION_ID, sendDailyNotification(context).build());
            Log.i("Notify", "Report Daily Alarm");
        }
       if (intent.getAction().equals(KEY_WARNING)) {
           notificationManagerCompat.notify(NOTIFICATION_ID, sendWarningNotification(context).build());
           Log.i("Notify", "Report Warning Alarm");
       }
       if(intent.getAction().equals(KEY_RIMANDA)){
           notificationManagerCompat.notify(NOTIFICATION_ID, sendDailyNotification(context).build());
           Log.i("Notify", "Report Rimanda");
           Alarm.RimandaOFF();
       }
    }

    private NotificationCompat.Builder sendWarningNotification(Context mContext) {
        //INTENT NOTIFICA WARNING
        Intent intent_notification = new Intent(mContext, StatisticheFragment.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        //NOTIFICA GIORNALIERA BUILDER
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle("Attenzione, alcuni valori hanno superato il limite!")
                .setContentText("")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Alarm.getWarningString()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
//
        return builder;
    }

    private NotificationCompat.Builder sendDailyNotification(Context mContext){
        //INTENT NOTIFICA GIORNALIERA
        Intent intent_notification = new Intent(mContext, HomeFragment.class);
        intent_notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_notification, PendingIntent.FLAG_ONE_SHOT);

        //INTENT BOTTONE1 NOTIFICA GIORNALIERA
        Intent IntentNuovoReport = new Intent(mContext, NewReportActivity.class);
        IntentNuovoReport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentNuovoReport = PendingIntent.getActivity(mContext, 0,IntentNuovoReport, PendingIntent.FLAG_ONE_SHOT);

        //INTENT BOTTONE2 NOTIFICA GIORNALIERA
        Intent IntentRimanda = new Intent(mContext, RimandaActivity.class);
        IntentRimanda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentRimanda = PendingIntent.getActivity(mContext, 0,IntentRimanda, PendingIntent.FLAG_ONE_SHOT);

        //NOTIFICA GIORNALIERA BUILDER
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                    .setContentTitle("Non hai ancora aggiunto report oggi")
                    .setContentText("")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Monitorare i tuoi parametri è importante, clicca qua per inserire un nuovo report"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_baseline_calendar_today_24, "Aggiungi report", pendingIntentNuovoReport)
                    .addAction(R.drawable.ic_baseline_calendar_today_24, "Rimanda", pendingIntentRimanda);

            return builder;
    }
}
