package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> typeOfPurchases=new ArrayList<String>();
    ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
        Date currentDate = new Date();
// Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        purchasesArrayList.add(new Purchases(dateText,2500,"авто","заправка"));//fds
        purchasesArrayList.add(new Purchases(dateText,3500,"авто","заправка"));

        purchasesArrayList.add(new Purchases(dateText,2500,"авто","заправка"));

        purchasesArrayList.add(new Purchases(dateText,2500,"авто","заправка"));


        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter(purchasesArrayList, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        mPieChart.addPieSlice(new PieModel("Еда", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Автомобиль", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Медицина", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Развлечения", 9, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();
    }
}
