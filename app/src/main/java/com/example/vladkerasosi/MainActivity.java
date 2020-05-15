package com.example.vladkerasosi;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

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

    private ArrayList<TypesOfPurchases> typesOfPurchases= new ArrayList<>();
    private ArrayList<Purchases> purchasesArrayList= new ArrayList<>();
    private HashMap<String,Float> piechartItem= new HashMap<>();

    private AppDatabase appDatabase;
    private float Balance=0;

    private ArrayList<String> types=new ArrayList<>();

    private SharedPreferences sPref;
    private  SharedPreferences sPrefSettings;

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recylerView);
        sPrefSettings = PreferenceManager.getDefaultSharedPreferences(this);
        setDarkMode();

        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

        loadData();

        if(typesOfPurchases.isEmpty())  firstStart();


    }

    private void loadData(){
        purchasesArrayList.clear();
        typesOfPurchases.clear();
        purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
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
        loadData();
//        LoadLimit();
//        LoadNotifLimit();
//        if(NotifLimit)
//            CheckLimit();
        setRecyclerView();
        setPiechartItem();
        setTextView();
        checkLimit();
    }

    private void convertToString(){
        types.clear();
            for (TypesOfPurchases typePurchases : typesOfPurchases) {
                types.add(typePurchases.getType_name_purchases());
            }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPurchases(final Purchases purchases, final int position)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

         final EditText nameEditText = view.findViewById(R.id.nameEditText);
         final EditText priceEditText = view.findViewById(R.id.priceEditText);
         final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
         final EditText dateEditText=view.findViewById(R.id.dateEditText);
         final Spinner spinner=view.findViewById(R.id.spinerEdit);

        priceEditText.setVisibility(View.VISIBLE);
        decriptionEditText.setVisibility(View.VISIBLE);
        dateEditText.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);

        if(purchases!=null) {
            nameEditText.setText(purchases.getName());
            priceEditText.setText(String.valueOf(purchases.getSum()));
            decriptionEditText.setText(purchases.getDescription());
            dateEditText.setText(purchases.getData());

            convertToString();

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            for (int i = 0; i < typesOfPurchases.size(); i++) {

                if (purchases.getTypesOfPurchases().equals(typesOfPurchases.get(i))) {
                    spinner.setSelection(i);
                }
            }


            alertDialogBuilderUserInput.setCancelable(true)
                    .setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePurchases(purchases, position);

                        }
                    })
                    .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            dialog.cancel();
                        }
                    });
        }

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
       // Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.backgroundDialog);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите название покупки!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dateEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите дату!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(decriptionEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Введите заметку!", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    alertDialog.dismiss();
                }


                if (purchases!=null) {

                    Balance+=purchases.getSum();
                    updatePurchases(nameEditText.getText().toString(),
                            decriptionEditText.getText().toString() ,
                            Float.parseFloat(priceEditText.getText().toString()),
                            dateEditText.getText().toString(),
                            new TypesOfPurchases(spinner.getSelectedItemId(),spinner.getSelectedItem().toString()),
                            position);
                }
            }
        });







    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePurchases(String name, String description, float sum, String date, TypesOfPurchases typeOfPurchases, int position){

        Balance-=sum;

        Purchases purchases=purchasesArrayList.get(position);

        purchases.setName(name);
        purchases.setDescription(description);
        purchases.setData(date);
        purchases.setSum(sum);
        purchases.setTypesOfPurchases(typeOfPurchases);

        appDatabase.getPur_Pro_Dao().updatePurchases(purchases);
        purchasesArrayList.set(position,purchases);
        dataAdapter.notifyDataSetChanged();
        setPiechartItem();
        setTextView();
        checkLimit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deletePurchases(Purchases purchases, int position){

        Balance+=purchases.getSum();
        purchasesArrayList.remove(position);
        appDatabase.getPur_Pro_Dao().deletePurchases(purchases);
        dataAdapter.notifyDataSetChanged();
        setPiechartItem();
        setTextView();
        checkLimit();

    }

    @SuppressLint("ShowToast")
    public void showNotify(){
        // Идентификатор канала
        // Идентификатор канала
        String CHANNEL_ID = "lol_channel";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                        .setContentTitle("Превышен лимит")
                        .setContentText("Превышен ежемясяный бюджет, сократите расходы!");
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

        Toast toast = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            toast = Toast.makeText(MainActivity.this, "Превышен ежемесячный бюджет", Toast.LENGTH_LONG);
        }
        toast.show();
    }

    public  void setDarkMode(){
        if(sPrefSettings.getBoolean("DarkMode",false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


    public void checkLimit(){
        if(sPrefSettings.getBoolean("Notify",false)) {
            float limit = Float.parseFloat(sPrefSettings.getString("Limit", "50000"));
            float totalSum = 0;
            for (Purchases purchases : purchasesArrayList) {
                totalSum += purchases.getSum();
            }
            if (totalSum > limit) {
                showNotify();
            }
        }
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
        dataAdapter = new DataAdapter(purchasesArrayList, MainActivity.this);
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
    @Override
    protected void onRestart() {
        super.onRestart();
        setPiechartItem();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

        piechartItem.clear();
        PieChart mPieChart = findViewById(R.id.piechart);
        mPieChart.clearChart();

        if(!purchasesArrayList.isEmpty()) {

            Random rand = new Random();

            for (int i = 0; i < typesOfPurchases.size(); i++) {

                piechartItem.put(typesOfPurchases.get(i).getType_name_purchases(), (float) 0);
            }
            for (int i = 0; i < purchasesArrayList.size(); i++) {

                String type = purchasesArrayList.get(i).getTypesOfPurchasesName();
                try{
                piechartItem.put(type, piechartItem.get(type) + purchasesArrayList.get(i).getSum());
            } catch (NullPointerException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
            }
            for (HashMap.Entry<String, Float> item : piechartItem.entrySet()) {
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
               if( item.getValue()!=0)
                mPieChart.addPieSlice(new PieModel(item.getKey(), item.getValue(), Color.rgb(r, g, b)));

            }
        }

    }

    public void tap_add_purchases(View view) {
        Intent intent = new Intent(this, Add_new.class);
        intent.putExtra("typeAdd","purchases");
        startActivity(intent);
    }

    public void GotToProfit(View view) {
        Intent intent = new Intent(this, Profit_Activity.class);
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

}
