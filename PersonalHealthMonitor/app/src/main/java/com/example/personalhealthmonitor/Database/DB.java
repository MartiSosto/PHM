package com.example.personalhealthmonitor.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.personalhealthmonitor.Utilities.Converters;


@Database(entities = {Report.class, Settings.class, Notification.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DB extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract SettingsDao settingsDao();
    public abstract NotificationDao notificationDao();

    public static volatile DB dbInstance;

    static DB getDatabase(final Context context){
        if(dbInstance == null){
            synchronized (DB.class){
                if(dbInstance == null){
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DB.class, "DB").build();

                }
            }
        }
        return dbInstance;
    }
}