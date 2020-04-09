package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> typeOfPurchases=new ArrayList<String>();
    ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Еда", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Автомобиль", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Медицина", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Развлечения", 9, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();
    }
}
