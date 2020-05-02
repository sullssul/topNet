package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Add_new_profit extends AppCompatActivity {

    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
    float sum=0;
    String typeOfProfit="";
    String name="";
    ArrayList<String> typesOfProfit=new ArrayList<String>();

    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    String dateText = dateFormat.format(currentDate);
    float Balance;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profit);

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            typesOfProfit = (ArrayList<String>) getIntent().getSerializableExtra("typesOfProfit");
           Balance=arguments.getFloat("Balance");

        }
        createSpinner();

    }


    void createSpinner() {
        spinner = findViewById(R.id.profit_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesOfProfit);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }


    public void AddNewProfit(View view) {

        EditText sum_editText=findViewById(R.id.sum_profit);
        sum=Float.parseFloat(String.valueOf(sum_editText.getText()));
        Balance+=sum;

        EditText name_editText=findViewById(R.id.name_profit);
        name= String.valueOf(name_editText.getText());

        if(name.equals("")){
            name="Поступление";
        }

        spinner = findViewById(R.id.profit_spinner);
        typeOfProfit=spinner.getSelectedItem().toString();

        Profit profit=new Profit(dateText,sum,name,typeOfProfit);

        Intent intent = new Intent(this, Profit_Activity.class);
        intent.putExtra("profit",profit);

       intent.putExtra("Balance",Balance);
        startActivity(intent);
    }
}
