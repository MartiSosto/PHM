package com.example.personalhealthmonitor.Utilities;

import com.example.personalhealthmonitor.Database.Notification;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.Database.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.personalhealthmonitor.MainActivity.notificationViewModel;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;
import static com.example.personalhealthmonitor.MainActivity.settingsViewModel;

public class Utility {

    private static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public static Date PrimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date PrimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date PrimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static Date UltimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, ultimo);
        return Converters.StringToDate(SDF.format(calendar.getTime()));
    }

    public static void randomData(int numReport){

        for(int i = 0; i<numReport; i++){
            Date randomDate = randomDate(Converters.StringToDate("01/01/2019"), Converters.StringToDate("31/12/2020"));

            double battito = randomDouble(60, 100);
            double pressioneSistolica = randomDouble(60, 80);
            double pressioneDiastolica = randomDouble(60, 80);
            double temperatura = randomDouble(35, 38);
            double glicemiamax = randomDouble(60, 125);
            double glicemiamin = randomDouble(60, 125);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(randomDate);
            int giorno = calendar.getTime().getDate();
            int mese = calendar.getTime().getMonth()+1;
            int anno = calendar.getTime().getYear()-100;
            String ora = String.valueOf(randomDate.getHours())+":"+ String.valueOf(randomDate.getMinutes());

            Report report = new Report(0, giorno, mese, anno, ora, battito, temperatura, pressioneSistolica, pressioneDiastolica, glicemiamax, glicemiamin, String.valueOf(calendar.getTime()));
            reportViewModel.setReport(report);
        }

        Settings tmpSettings = new Settings(0, "Battito", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "PressioneDiastolica", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "PressioneSistolica", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "Temperatura", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);
        tmpSettings = new Settings(0, "Glicemia", 2, null, null, 0);
        settingsViewModel.setSettings(tmpSettings);

        Notification tmpNotification = new Notification(0, true, Calendar.getInstance().getTime());
        notificationViewModel.setNotification(tmpNotification);
    }

    private static double randomDouble(double inizio, double fine){
        boolean bool = ThreadLocalRandom.current().nextBoolean();
        if(bool) {
            double number = ThreadLocalRandom.current().nextDouble(inizio, fine);
            int aux = (int) (number * 100);
            double result = aux / 100d;//12.43
            return result;
        }
        else return  0;
    }

    private static Date randomDate (Date d1, Date d2) {
        return new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
    }
}
