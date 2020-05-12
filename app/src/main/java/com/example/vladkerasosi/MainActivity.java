package com.example.vladkerasosi;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import Model.Purchases;
import Model.TypesOfPurchases;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
   // TypesOfPurchases typesOfPurchases =new TypesOfPurchases(this);;
    ArrayList<TypesOfPurchases> typesOfPurchases=new ArrayList<TypesOfPurchases>();
    ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();
    HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    float Balance=0;
    SharedPreferences sPref;
    PieChart mPieChart;
    float totalSum=0;
    float Limit=0;
    boolean firstStart=true;
    boolean NotifLimit;
    private static final int NOTIFY_ID = 101;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recylerView);
        if(typesOfPurchases.size()==0) {
        typesOfPurchases.add(new TypesOfPurchases(0,"Авто"));
            typesOfPurchases.add(new TypesOfPurchases(1,"Еда"));
            typesOfPurchases.add(new TypesOfPurchases(2,"Медицина"));
            typesOfPurchases.add(new TypesOfPurchases(3,"Развлечения"));
            typesOfPurchases.add(new TypesOfPurchases(4,"Связь"));
            typesOfPurchases.add(new TypesOfPurchases(4,"Другое"));

            //typesOfPurchases.save();
        firstStart=false;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        LoadArrayList();
        LoadBalance();
        LoadLimit();
        LoadNotifLimit();
        //typesOfPurchases.load();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            if((Purchases) getIntent().getSerializableExtra("purchases")!=null) {
                purchasesArrayList.add((Purchases) getIntent().getSerializableExtra("purchases"));
                totalSum+=purchasesArrayList.get(purchasesArrayList.size()-1).getSum();
            }
//            if(arguments.getString("newTypeToMain")!=null) {
//                typesOfPurchases.add(arguments.getString("newTypeToMain"));
//            }
           // Balance=arguments.getFloat("Balance");
        }
//        if(NotifLimit)
//            CheckLimit();
        setRecyclerView();
        if(purchasesArrayList.size()!=0)
        setPiechartItem();
        setTextView();
    }

    public void showNotif(){
        // Идентификатор канала
        String CHANNEL_ID = "Kera lox";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                        .setContentTitle("Превышение лимита")
                        .setContentText("Вы превысели ежемесяный лимит в "+ Limit)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }




    public void CheckLimit(){
        if(totalSum>=Limit){
            showNotif();
        }
    }

    public void LoadNotifLimit(){
        sPref = getSharedPreferences("NotifLimit",MODE_PRIVATE);
        NotifLimit = sPref.getBoolean("NotifLimit", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveArrayList();
        SaveBalance();
        typesOfPurchases.save();
        //SaveTypesOfPurchases();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveArrayList();
        SaveBalance();
        typesOfPurchases.save();
//        SaveTypesOfPurchases();

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

    public void setRecyclerView(){
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        adapter = new DataAdapter(purchasesArrayList, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @SuppressLint("SetTextI18n")
    private void setTextView() {
        TextView textView=findViewById(R.id.balance_purchases);
        textView.setText(Balance+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.clearChart();
        Random rand = new Random();

        for(int i=0;i<typesOfPurchases.size();i++){
            piechartItem.put(typesOfPurchases.get(i), (float) 0);
        }
        for(int i=0;i<purchasesArrayList.size();i++){
            piechartItem.put(purchasesArrayList.get(i).getTypeOfPurchases(),piechartItem.get(purchasesArrayList.get(i).getTypeOfPurchases())+(float)purchasesArrayList.get(i).getSum());
        }
        for(HashMap.Entry<String, Float> item : piechartItem.entrySet()){
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r,g,b)));

        }

    }

    public void tap_add_purchases(View view) {
        Intent intent = new Intent(this, Add_new.class);
       // intent.putExtra("typesOfPurchases",typesOfPurchases);
     //   intent.putExtra("Balance",Balance);
        startActivity(intent);
    }

    public void GotToProfit(View view) {
        Intent intent = new Intent(this, Profit_Activity.class);
      // intent.putExtra("Balance",Balance);
        startActivity(intent);
        overridePendingTransition(R.anim.go_next_in, R.anim.go_next_out);
    }

//    public  void SaveTypesOfPurchases(){
//        sPref = getSharedPreferences("typesOfPurchases",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sPref.edit();
//        try {
//            editor.putString("typesOfPurchases", ObjectSerializer.serialize(typesOfPurchases));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        editor.apply();
//    }
//
//    public void LoadTypesOfPurchases(){
//        sPref = getSharedPreferences("typesOfPurchases",MODE_PRIVATE);
//        try {
//            typesOfPurchases = ( ArrayList<String>) ObjectSerializer.deserialize(sPref.getString("typesOfPurchases", ObjectSerializer.serialize(new ArrayList<String>())));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void SaveArrayList(){
        sPref = getSharedPreferences("purchasesArrayList",MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        try {
            editor.putString("purchasesArrayList", ObjectSerializer.serialize(purchasesArrayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public void LoadArrayList(){
        sPref = getSharedPreferences("purchasesArrayList",MODE_PRIVATE);

        try {
            purchasesArrayList = (ArrayList<Purchases>) ObjectSerializer.deserialize(sPref.getString("purchasesArrayList", ObjectSerializer.serialize(new ArrayList<Purchases>())));
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

    public void SaveLimit(){

        sPref = getSharedPreferences("Limit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat("Limit",Limit);
        editor.apply();
    }

    public void LoadLimit(){
        sPref = getSharedPreferences("Limit",MODE_PRIVATE);
        Limit = sPref.getFloat("Limit", 0);
    }
}
