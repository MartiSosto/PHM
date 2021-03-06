package com.example.testmenu2.statistiche;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testmenu2.R;
import com.github.mikephil.charting.charts.PieChart;

public class StatisticheFragment extends Fragment {

    private StatisticheViewModel statisticheViewModel;
    Button BTNsettimana, BTNmese, BTNanno, BTNtutti;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticheViewModel =
                new ViewModelProvider(this).get(StatisticheViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistiche, container, false);
        BTNsettimana = root.findViewById(R.id.BTNsettimana);
        BTNmese = root.findViewById(R.id.BTNmese);
        BTNanno = root.findViewById(R.id.BTNanno);
        BTNtutti = root.findViewById(R.id.BTNtutti);

        BTNsettimana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticheViewModel.setPeriodo("settimana");
            }
        });

        BTNmese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticheViewModel.setPeriodo("mese");
            }
        });

        BTNanno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticheViewModel.setPeriodo("anno");
            }
        });

        BTNtutti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticheViewModel.setPeriodo("tutti");
            }
        });

        PieChart pieChart = root.findViewById(R.id.piechart);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();

        statisticheViewModel.getPeriodo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("Periodo", s);
                pieChart.setData(statisticheViewModel.getPieData(s));
                pieChart.setDescription(statisticheViewModel.getDescription(s));
            }
        });

        return root;
    }


}