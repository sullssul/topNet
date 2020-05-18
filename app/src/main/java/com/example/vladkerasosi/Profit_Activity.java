package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import Model.Profit;

import Model.TypeOfProfit;



public class Profit_Activity extends AppCompatActivity {

    private Data_adapter_profit data_adapter_profit;

    private ArrayList<TypeOfProfit> typesOfProfit=new ArrayList<>();
    private ArrayList<Profit> profitArrayList= new ArrayList<>();
    private HashMap<String,Float> piechartItem= new HashMap<>();
    private float Balance=0;

    private ArrayList<String> types=new ArrayList<>();

    private AppDatabase appDatabase;

    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

        loadData();
        if(typesOfProfit.isEmpty()) firstStart();



    }
    private void loadData(){
        profitArrayList.clear();
        typesOfProfit.clear();
        profitArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllProfit());
        typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        LoadBalance();
        setRecyclerView();
        setPiechartItem();
        setTextView();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editProfit(final Profit profit, final int position)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Profit_Activity.this);
        alertDialogBuilderUserInput.setView(view);


        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);

        decriptionEditText.setVisibility(View.GONE);

        if(profit!=null) {
            nameEditText.setText(profit.getName());
            priceEditText.setText(String.valueOf(profit.getSum()));
            dateEditText.setText(profit.getDate());
            convertToString();

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).equals(profit.getTypeOfProfitName())) {
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
                            deleteProfit(profit, position);

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
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    Toast.makeText(Profit_Activity.this, "Введите название покупки!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())) {
                    Toast.makeText(Profit_Activity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dateEditText.getText().toString())) {
                    Toast.makeText(Profit_Activity.this, "Введите дату!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    alertDialog.dismiss();
                }


                if (profit!=null) {

                    Balance-=profit.getSum();
                    updateProfit(nameEditText.getText().toString(),
                            Float.parseFloat(priceEditText.getText().toString()),
                            dateEditText.getText().toString(),
                            new TypeOfProfit(spinner.getSelectedItemId(),spinner.getSelectedItem().toString()),
                            position);
                }
            }
        });

    }

    private void convertToString(){

        for (TypeOfProfit typeProfit : typesOfProfit) {
            types.add(typeProfit.getType_profit_name());

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProfit(String name,  float sum, String date, TypeOfProfit typeOfProfit, int position){

        Balance+=sum;
        Profit profit=profitArrayList.get(position);

        profit.setDate(date);
        profit.setName(name);
        profit.setSum(sum);
        profit.setTypeOfProfit(typeOfProfit);

        appDatabase.getPur_Pro_Dao().updateProfit(profit);
        profitArrayList.set(position,profit);
        data_adapter_profit.notifyDataSetChanged();
        setPiechartItem();
        setTextView();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteProfit(Profit profit, int position){

        Balance-=profit.getSum();
        profitArrayList.remove(position);
        appDatabase.getPur_Pro_Dao().deleteProfit(profit);
        data_adapter_profit.notifyDataSetChanged();
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
        RecyclerView recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        data_adapter_profit = new Data_adapter_profit(profitArrayList,Profit_Activity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(data_adapter_profit);
        recyclerView.setLayoutManager(layoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onRestart() {
        super.onRestart();
        setPiechartItem();
    }

    @SuppressLint("SetTextI18n")
    private void setTextView() {
        TextView textView=findViewById(R.id.Balance_Profit);
        textView.setText(Balance+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPiechartItem(){

        piechartItem.clear();
        PieChart mPieChart =  findViewById(R.id.piechart);
        mPieChart.clearChart();

        if(!profitArrayList.isEmpty()) {
            Random rand = new Random();

            for (int i = 0; i < typesOfProfit.size(); i++) {
                piechartItem.put(typesOfProfit.get(i).getType_profit_name(), (float) 0);
            }
            for (int i = 0; i < profitArrayList.size(); i++) {
                String type = profitArrayList.get(i).getTypeOfProfitName();
                try {
                    piechartItem.put(type, piechartItem.get(type) +  profitArrayList.get(i).getSum());
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