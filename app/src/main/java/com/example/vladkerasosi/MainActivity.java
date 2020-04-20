package com.example.vladkerasosi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

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
    ArrayList<String> typesOfPurchases=new ArrayList<String>();
    ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
        typesOfPurchases.add("Авто");
        typesOfPurchases.add("Еда");
        typesOfPurchases.add("Медицина");
        typesOfPurchases.add("ЖКХ");
        typesOfPurchases.add("Связь");
        typesOfPurchases.add("Развлечения");
        typesOfPurchases.add("Прочее");


        setRecyclerView();

        mPieChart.addPieSlice(new PieModel("Еда", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Автомобиль", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Медицина", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Развлечения", 9, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();

    }



    public void setRecyclerView(){
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter(purchasesArrayList, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            purchasesArrayList = (ArrayList<Purchases>) getIntent().getSerializableExtra("purchasesArrayList");
        }
        setRecyclerView();
    }

    public void tap_add_purchases(View view) {
        Intent intent = new Intent(this, Add_new.class);
        intent.putExtra("typesOfPurchases",typesOfPurchases);
        intent.putExtra("purchasesArrayList",purchasesArrayList);
        startActivity(intent);

    }

    public void tap_add_money(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Cоси хуй еще не сделал, говорил же прототип",
                Toast.LENGTH_SHORT);

        toast.show();
    }
}
