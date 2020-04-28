package com.example.vladkerasosi;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> typesOfPurchases=new ArrayList<String>();
    ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();
    HashMap<String,Integer> piechartItem=new HashMap<String, Integer>();
    float Balance=50000;
    float startX;
    PieChart mPieChart;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recylerView);
        typesOfPurchases.add("Авто");
        typesOfPurchases.add("Еда");
        typesOfPurchases.add("Медицина");
        typesOfPurchases.add("ЖКХ");
        typesOfPurchases.add("Связь");
        typesOfPurchases.add("Развлечения");
        typesOfPurchases.add("Прочее");
        mPieChart=findViewById(R.id.piechart);
      //  mPieChart.startAnimation();

    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: //первое касание
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP: //отпускание
                float stopX = event.getX();
                if (stopX < startX) {
                    Intent intent = new Intent(this, Profit_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.go_next_out, R.anim.go_next_in);

                }
                break;
        }
        return true;
    }

    public void setRecyclerView(){
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter(purchasesArrayList, this);
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
            purchasesArrayList = (ArrayList<Purchases>) getIntent().getSerializableExtra("purchasesArrayList");
        }
        setRecyclerView();
        setPiechartItem();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

        mPieChart = (PieChart) findViewById(R.id.piechart);
        Random rand = new Random();

        for(int i=0;i<typesOfPurchases.size();i++){
            piechartItem.put(typesOfPurchases.get(i),0);
        }
        for(int i=0;i<purchasesArrayList.size();i++){
            piechartItem.put(purchasesArrayList.get(i).getTypeOfPurchases(),+purchasesArrayList.get(i).getSum());
        }
        for(HashMap.Entry<String, Integer> item : piechartItem.entrySet()){
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r,g,b)));

        }




    }


    public void tap_add_purchases(View view) {
        Intent intent = new Intent(this, Add_new.class);
        intent.putExtra("typesOfPurchases",typesOfPurchases);
        intent.putExtra("purchasesArrayList",purchasesArrayList);
        startActivity(intent);

    }


}
