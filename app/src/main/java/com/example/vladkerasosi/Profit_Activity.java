package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

public class Profit_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter<Data_adapter_profit.ViewHolder> adapter;
    PieChart mPieChart;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> typesOfProfit=new ArrayList<String>();
    ArrayList<Profit> profitArrayList=new ArrayList<Profit>();
    HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    float Balance=0;
    SharedPreferences sPref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        typesOfProfit.add("Зарплата");
        typesOfProfit.add("Пенсия");
        typesOfProfit.add("Стипендия");
        typesOfProfit.add("Вклад");
        typesOfProfit.add("Продажа вещей");
        typesOfProfit.add("Другое");
//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = sPref.edit();
//        editor.clear();
//        editor.apply();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveArrayList();
        SaveBalance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveArrayList();
        SaveBalance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        LoadBalance();
        LoadArrayList();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            if((Profit) getIntent().getSerializableExtra("profit")!=null)
                profitArrayList.add((Profit) getIntent().getSerializableExtra("profit"));
           Balance=arguments.getFloat("Balance");

        }
        setRecyclerView();
        setPiechartItem();
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
            piechartItem.put(typesOfProfit.get(i), (float) 0);
        }
        for(int i=0;i<profitArrayList.size();i++){
            piechartItem.put(profitArrayList.get(i).getTypeOfProfit(),piechartItem.get(profitArrayList.get(i).getTypeOfProfit()) +(float)profitArrayList.get(i).getSum());
        }
        for(HashMap.Entry<String, Float> item : piechartItem.entrySet()){
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r,g,b)));

        }

    }

    public void tap_add_profit(View view) {
        Intent intent = new Intent(this, Add_new_profit.class);
        intent.putExtra("typesOfProfit",typesOfProfit);
       intent.putExtra("Balance",Balance);
        startActivity(intent);
    }

    public void goToPurshases(View view) {
        Intent intent = new Intent(this, MainActivity.class);
      //  intent.putExtra("Balance",Balance);
        startActivity(intent);
        overridePendingTransition(R.anim.go_prev_in, R.anim.go_prev_out);
    }

    public void SaveArrayList(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        try {
            editor.putString("profitArrayList", ObjectSerializer.serialize(profitArrayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public void LoadArrayList(){
        sPref = getPreferences(MODE_PRIVATE);
        try {
            profitArrayList = (ArrayList<Profit>) ObjectSerializer.deserialize(sPref.getString("profitArrayList", ObjectSerializer.serialize(new ArrayList<Profit>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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