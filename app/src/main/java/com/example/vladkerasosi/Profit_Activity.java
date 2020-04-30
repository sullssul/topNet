package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class Profit_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    PieChart mPieChart;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> typesOfProfit=new ArrayList<String>();
    ArrayList<Profit> profitArrayList=new ArrayList<Profit>();
    HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    float Balance=0;




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

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            Balance=arguments.getFloat("Balance");
        }
        setTextView();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            if((ArrayList<Profit>) getIntent().getSerializableExtra("profitArrayList")!=null)
            profitArrayList = (ArrayList<Profit>) getIntent().getSerializableExtra("profitArrayList");
            Balance=arguments.getFloat("Balance");

        }
        setRecyclerView();
        setPiechartItem();
        setTextView();
    }


    private void setTextView() {
        TextView textView=findViewById(R.id.Balance_Profit);
        textView.setText(Balance+"");
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

         mPieChart = (PieChart) findViewById(R.id.piechart);
        Random rand = new Random();

        for(int i=0;i<typesOfProfit.size();i++){
            piechartItem.put(typesOfProfit.get(i), (float) 0);
        }
        for(int i=0;i<profitArrayList.size();i++){
            piechartItem.put(profitArrayList.get(i).getTypeOfProfit(), (float) +profitArrayList.get(i).getSum());
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
        intent.putExtra("profitArrayList",profitArrayList);
       intent.putExtra("Balance",Balance);
        startActivity(intent);


    }

    public void goToPurshases(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Balance",Balance);
        startActivity(intent);
        overridePendingTransition(R.anim.go_prev_in, R.anim.go_prev_out);
    }
}