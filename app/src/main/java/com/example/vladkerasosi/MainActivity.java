package com.example.vladkerasosi;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

import Data.AppDatabase;
import Model.Purchases;
import Model.TypesOfPurchases;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DataAdapter dataAdapter;

    private ArrayList<TypesOfPurchases> typesOfPurchases=new ArrayList<TypesOfPurchases>();
    private ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();
    private HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    private PieChart mPieChart;

    private AppDatabase appDatabase;
    private float Balance=0;


    SharedPreferences sPref;
    private float totalSum=0;
    private float Limit=0;

    boolean NotifLimit;
    private static final int NOTIFY_ID = 101;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recylerView);

        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

        purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());

        if(typesOfPurchases.isEmpty())  firstStart();


    }

    private void firstStart(){

        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Авто"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Еда"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Медицина"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Развлечения"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Связь"));
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,"Другое"));
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        LoadBalance();
//        LoadLimit();
//        LoadNotifLimit();
//        if(NotifLimit)
//            CheckLimit();
        setRecyclerView();
        if(!purchasesArrayList.isEmpty()) setPiechartItem();
        setTextView();
    }

    public void editPurchases(final Purchases purchases,final int position)
    {


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
        SaveBalance();
    }

    @Override
    protected void onPause() {
        super.onPause();
       SaveBalance();
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
        dataAdapter = new DataAdapter(purchasesArrayList, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(dataAdapter);
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

            piechartItem.put(typesOfPurchases.get(i).getType_name_purchases(), (float) 0);
        }
        for(int i=0;i<purchasesArrayList.size();i++){

            String type=purchasesArrayList.get(i).getTypesOfPurchasesName();
            piechartItem.put(type,piechartItem.get(type)+(float)purchasesArrayList.get(i).getSum());
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
        intent.putExtra("typeAdd","purchases");
        startActivity(intent);
    }

    public void GotToProfit(View view) {
        Intent intent = new Intent(this, Profit_Activity.class);
      // intent.putExtra("Balance",Balance);
        startActivity(intent);
        overridePendingTransition(R.anim.go_next_in, R.anim.go_next_out);
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
