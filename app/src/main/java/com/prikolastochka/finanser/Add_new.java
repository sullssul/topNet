package com.prikolastochka.finanser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Data.AppDatabase;
import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;



public class Add_new extends AppCompatActivity  {


    private Spinner spinner;
    private AppDatabase appDatabase;
    private ArrayList<TypesOfPurchases> typesOfPurchases=new ArrayList<>();
    private ArrayList<TypeOfProfit> typeOfProfits=new ArrayList<>();
    private ArrayList<String> types=new ArrayList<>();
    private float Balance;
    private String typeAdd;
    private SharedPreferences sPref;
    private ActionBar actionBar;

    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    String dateText = dateFormat.format(currentDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();



    }
    private void loadData(){

        if(typeAdd.equals("profit")) {
            typeOfProfits.clear();
            typeOfProfits.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());
        }

        if(typeAdd.equals("purchases")){
            typesOfPurchases.clear();
            typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settitle(){
        if(typeAdd.equals("profit")) {
            actionBar.setTitle("Добавление нового дохода");
        }

        if(typeAdd.equals("purchases")){
            actionBar.setTitle("Добавление нового расхода");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        TextView textView=findViewById(R.id.textView);
        EditText editText=findViewById(R.id.description_edit_text);

        if(arguments!=null) {
            typeAdd=arguments.getString("typeAdd");
        }

        settitle();
        loadData();

        if(typeAdd.equals("purchases")){
            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);

        }
        if(typeAdd.equals("profit")){
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);

        }
        convertToString();
        createSpinner();
        LoadBalance();

    }

    private void convertToString(){

        types.clear();
        if(typeAdd.equals("purchases")) {
            for (TypesOfPurchases typePurchases : typesOfPurchases) {
                types.add(typePurchases.getType_name_purchases());

            }
        }

        if(typeAdd.equals("profit")){
            for (TypeOfProfit typeProfit : typeOfProfits) {
                types.add(typeProfit.getType_profit_name());

            }
        }
    }

    private void addProfit(String date, float sum, String name, TypeOfProfit typeOfProfit){
        appDatabase.getPur_Pro_Dao().addProfit(new Profit(0,date,sum,name,typeOfProfit));
    }

    private void addPurchases(String name,String description,float sum,String date,TypesOfPurchases typesOfPurchases ){

      appDatabase.getPur_Pro_Dao().addPurchases(new Purchases(0,date,sum,typesOfPurchases,name,description));

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

    void createSpinner() {
        spinner = findViewById(R.id.type_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public void AddNew(View view) {
        float sum;
        String name;
        String description;

        EditText sum_editText=findViewById(R.id.sum_edit_text);
        EditText name_editText=findViewById(R.id.name_edit_text);

        spinner = findViewById(R.id.type_spinner);
        long id = spinner.getSelectedItemId();

        if (!TextUtils.isEmpty(sum_editText.getText())) {

            sum=Float.parseFloat(sum_editText.getText().toString());

            if(TextUtils.isEmpty(name_editText.getText())){
                name="покупка";
            }   else {
                name= String.valueOf(name_editText.getText());}

            if (typeAdd.equals("purchases")) {
                EditText description_editText = findViewById(R.id.description_edit_text);
                description = String.valueOf(description_editText.getText());
                if (TextUtils.isEmpty(description_editText.getText())) {
                    description = "Нет данных";
                }

                addPurchases(name, description, sum, dateText, typesOfPurchases.get((int) id));
                Balance -= sum;
                SaveBalance();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            if (typeAdd.equals("profit")) {
                if(TextUtils.isEmpty(name_editText.getText())){
                    name="зачисление";
                }   else {
                    name= String.valueOf(name_editText.getText());}

                addProfit(dateText, sum, name, typeOfProfits.get((int) id));
                Balance += sum;
                SaveBalance();

                Intent intent = new Intent(this, Profit_Activity.class);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(Add_new.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
        }

    }

    public void AddNewTypeOfPurchases(View view) {
        Intent intent = new Intent(this, Add_newType.class);
        intent.putExtra("typeOfactivity",typeAdd);
        startActivity(intent);
    }
}
