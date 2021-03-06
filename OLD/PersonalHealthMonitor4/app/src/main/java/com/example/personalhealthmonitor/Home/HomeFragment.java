package com.example.personalhealthmonitor.Home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.personalhealthmonitor.Database.ReportViewModel;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.OnSwipeTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.personalhealthmonitor.MainActivity.KEY_BATTITO;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMAX;
import static com.example.personalhealthmonitor.MainActivity.KEY_GLICEMIAMIN;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONEDIA;
import static com.example.personalhealthmonitor.MainActivity.KEY_PRESSIONESIS;
import static com.example.personalhealthmonitor.MainActivity.KEY_TEMPERATURA;
import static com.example.personalhealthmonitor.MainActivity.reportViewModel;

public class HomeFragment extends Fragment {

    private MutableLiveData<Date> dataSel;
    public static Calendar calendar;
    public static SimpleDateFormat SDF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setto la data di oggi

        calendar = Calendar.getInstance();
        SDF = new SimpleDateFormat("dd/MM/yyyy");
        dataSel = new MutableLiveData<>();
        dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView CardNoReport = root.findViewById(R.id.CardNoReport);
        SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

        //Recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        ReportListAdapter reportListAdapter = new ReportListAdapter(getContext());
        recyclerView.setAdapter(reportListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        dataSel.observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                TextView todayreportVal = root.findViewById(R.id.TXVTodayReport);
                todayreportVal.setText(getString(R.string.home_label1)+ SDF.format(date));

                TextView TXVBattiti = root.findViewById(R.id.TXVbattito);
                TextView TXVPressioneSistolica = root.findViewById(R.id.TXVpressioneSistolica);
                TextView TXVPressioneDiastolica = root.findViewById(R.id.TXVpressioneDiastolica);
                TextView TXVTemperatura = root.findViewById(R.id.TXVtemperatura);
                TextView TXVGlicemiaMax = root.findViewById(R.id.TXVglicemia_max);
                TextView TXVGlicemiaMin = root.findViewById(R.id.TXVglicemia_min);

                Log.i("DATA", Converters.DateToString(date));

                reportViewModel.getAvgVal(KEY_BATTITO, date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if(aDouble != null) TXVBattiti.setText(String.valueOf(tronca(aDouble)));
                    else TXVBattiti.setText("");
                });
                reportViewModel.getAvgVal(KEY_PRESSIONESIS,  date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if (aDouble != null) TXVPressioneSistolica.setText(String.valueOf(tronca(aDouble)));
                    else TXVPressioneSistolica.setText("");
                });
                reportViewModel.getAvgVal(KEY_PRESSIONEDIA, date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if (aDouble != null) TXVPressioneDiastolica.setText(String.valueOf(tronca(aDouble)));
                    else TXVPressioneDiastolica.setText("");
                });
                reportViewModel.getAvgVal(KEY_TEMPERATURA,  date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if (aDouble != null) TXVTemperatura.setText(String.valueOf(tronca(aDouble)));
                    else TXVTemperatura.setText("");
                });
                reportViewModel.getAvgVal(KEY_GLICEMIAMAX,  date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if (aDouble != null) TXVGlicemiaMax.setText(String.valueOf(tronca(aDouble)));
                    else TXVGlicemiaMax.setText("");
                });
                reportViewModel.getAvgVal(KEY_GLICEMIAMIN,  date, null).observe(getViewLifecycleOwner(), aDouble -> {
                    if (aDouble != null) TXVGlicemiaMin.setText(String.valueOf(tronca(aDouble)));
                    else TXVGlicemiaMin.setText("");
                });
                reportViewModel.getAllReports(date, null).observe(getViewLifecycleOwner(), reports -> {
                    Log.i("TIMESTAMP", String.valueOf(Converters.DateToLong(date)));
                    reportListAdapter.setReports(reports);
                    if(reports.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        CardNoReport.setVisibility(View.INVISIBLE);
                    }
                    else CardNoReport.setVisibility(View.VISIBLE);
                });
            }
        });


        //GESTISCE LO SWIPE TOP BOT LEFT E RIGHT
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                calendar.add(Calendar.DATE, -1);
                dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                calendar.add(Calendar.DATE, 1);
                dataSel.setValue(Converters.StringToDate(SDF.format(calendar.getTime())));
            }
        });

        //FAB
        FloatingActionButton fab = root.findViewById(R.id.FabAdd);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NewReportActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View,String>(fab,"activity_trans");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            startActivity(intent,options.toBundle());
        });

        return root;
    }

    //Tronca il valore double
    private double tronca(Double num){
        if(num == null ) return 0;
        else{
            num = num * 100;
            num = (double) Math.round(num);
            num = num / 100;
            return num;
        }

    }
}