package com.example.testperiodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat SDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yy");
        String giorno = SDF.format(calendar.getTime());
        Log.i("OGGI", giorno);

        Log.i("PRIMO SETTIMANA", PrimoGiornoSettimana());
        Log.i("ULTIMO SETTIMANA", UltimoGiornoSettimana());

        Log.i("PRIMO MESE", PrimoGiornoMese());
        Log.i("ULTIMO MESE", UltimoGiornoMese());
        Log.i("PRIMO ANNO", PrimoGiornoAnno());
        Log.i("ULTIMO ANNO", UltimoGiornoAnno());


    }

    public String PrimoGiornoSettimana (){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String UltimoGiornoSettimana(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DATE, 6);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String PrimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String UltimoGiornoMese(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String PrimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }

    public String UltimoGiornoAnno(){
        Calendar calendar = Calendar.getInstance();
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, ultimo);
        String giorno = SDF.format(calendar.getTime());
        return giorno;
    }



}

