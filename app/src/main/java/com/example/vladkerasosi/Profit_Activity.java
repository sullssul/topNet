package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Data.AppDatabase;
import Model.Profit;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

public class Profit_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter<Data_adapter_profit.ViewHolder> adapter;
    PieChart mPieChart;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<TypeOfProfit> typesOfProfit=new ArrayList<>();
    private ArrayList<Profit> profitArrayList=new ArrayList<Profit>();
    private HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    private float Balance=0;

    private AppDatabase appDatabase;

    SharedPreferences sPref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

        profitArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllProfit());
        typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());
        if(typesOfProfit.isEmpty()) firstStart();

        for (TypeOfProfit type : typesOfProfit) {
            Log.d("profit", "type: "+type.getType_profit_name() );
        }



    }

    private void firstStart(){
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Зарплата"));
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Пенсия"));
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Стипендия"));
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Вклад"));
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Продажа вещей"));
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,"Другое"));
        typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveBalance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveBalance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        LoadBalance();
        setRecyclerView();
        if(!profitArrayList.isEmpty())  setPiechartItem();
        setTextView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_profit, menu);
        return true;
    }

    public void start_settings(MenuItem item) {
        Intent intent = new Intent(this, Settings_Activity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN: //первое касание
//                    startX = event.getX();
//                    break;
//                case MotionEvent.ACTION_UP: //отпускание
//                    float stopX = event.getX();
//                    if (stopX > startX) {
//                        Intent intent = new Intent(this, MainActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.go_prev_in, R.anim.go_prev_out);
//
//                    }
//                    break;
//            }
//            return true;
//
//
//        }

    public void setRecyclerView(){
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        adapter = new Data_adapter_profit(profitArrayList, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @SuppressLint("SetTextI18n")
    private void setTextView() {
        TextView textView=findViewById(R.id.Balance_Profit);
        textView.setText(Balance+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

         mPieChart = (PieChart) findViewById(R.id.piechart);
         mPieChart.clearChart();
        Random rand = new Random();

        for(int i=0;i<typesOfProfit.size();i++){
            piechartItem.put(typesOfProfit.get(i).getType_profit_name(), (float) 0);
        }
        for(int i=0;i<profitArrayList.size();i++){
            String type=profitArrayList.get(i).getTypeOfProfitName();
            piechartItem.put(type,piechartItem.get(type) +(float)profitArrayList.get(i).getSum());
        }
        for(HashMap.Entry<String, Float> item : piechartItem.entrySet()){
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r,g,b)));

        }

    }

    public void tap_add_profit(View view) {
        Intent intent = new Intent(this, Add_new.class);
        intent.putExtra("typeAdd","profit");
        startActivity(intent);
    }

    public void goToPurshases(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.go_prev_in, R.anim.go_prev_out);
    }

    public void SaveBalance(){
        sPref = getSharedPreferences("Balance",MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat("Balance", Balance);
        editor.apply();
    }

    public void LoadBalance(){
        sPref = getSharedPreferences("Balance",MODE_PRIVATE);
        Balance = sPref.getFloat("Balance", 0);

    }
}