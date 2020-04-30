package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Add_new extends AppCompatActivity  {


    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
    float sum=0;
    String typeOfPurchases="";
    String name="";
    String description="";
    ArrayList<String> typesOfPurchases;
    ArrayList<Purchases> purchasesArrayList;
    float Balance;
    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    String dateText = dateFormat.format(currentDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            typesOfPurchases = (ArrayList<String>) getIntent().getSerializableExtra("typesOfPurchases");
            purchasesArrayList = (ArrayList<Purchases>) getIntent().getSerializableExtra("purchasesArrayList");
            Balance=arguments.getFloat("Balance");

        }
        createSpinner();
    }

    void createSpinner() {
        spinner = findViewById(R.id.type_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesOfPurchases);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }


    public void AddNewPurchases(View view) {

        EditText sum_editText=findViewById(R.id.sum_edit_text);
        sum=Integer.parseInt(String.valueOf(sum_editText.getText()));
        Balance-=sum;

        EditText name_editText=findViewById(R.id.name_edit_text);
        name= String.valueOf(name_editText.getText());
        if(name.equals("")){
            name="покупка_"+(purchasesArrayList.size()+1);
        }

        EditText description_editText=findViewById(R.id.description_edit_text);
        description= String.valueOf(description_editText.getText());


        spinner = findViewById(R.id.type_spinner);
        typeOfPurchases=spinner.getSelectedItem().toString();

        purchasesArrayList.add(new Purchases(dateText,sum,typeOfPurchases,name,description));

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("purchasesArrayList",purchasesArrayList);
        intent.putExtra("Balance",Balance);
        startActivity(intent);

    }

}
