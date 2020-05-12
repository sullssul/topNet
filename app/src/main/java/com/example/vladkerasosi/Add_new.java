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
import java.util.Date;
import java.util.Locale;

import Model.Purchases;
import Model.TypesOfPurchases;

public class Add_new extends AppCompatActivity  {


    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
   // SharedPreferences sPref;
    float sum=0;
    String typeOfPurchases="";
    String name="";
    String description="";
  //  ArrayList<String> typesOfPurchases;
    TypesOfPurchases typesOfPurchases =new TypesOfPurchases(this);

    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    String dateText = dateFormat.format(currentDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
    }

    @Override
    protected void onStart() {
        super.onStart();
        typesOfPurchases.load();
//        LoadTypesOfPurchases();
        createSpinner();

    }


//    public void SaveTypesOfPurchases(){
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




    void createSpinner() {
        spinner = findViewById(R.id.type_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typesOfPurchases.getTypesOfPurchases());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }





    public void AddNewPurchases(View view) {


        EditText sum_editText=findViewById(R.id.sum_edit_text);
        sum=Integer.parseInt(String.valueOf(sum_editText.getText()));


        EditText name_editText=findViewById(R.id.name_edit_text);
        name= String.valueOf(name_editText.getText());
        if(name.equals("")){
            name="покупка";
        }

        EditText description_editText=findViewById(R.id.description_edit_text);
        description= String.valueOf(description_editText.getText());


        spinner = findViewById(R.id.type_spinner);
        typeOfPurchases=spinner.getSelectedItem().toString();

        Purchases purchases=new Purchases(dateText,sum,typeOfPurchases,name,description);

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("purchases",purchases);
        typesOfPurchases.save();
        startActivity(intent);

    }

    public void AddNewTypeOfPurchases(View view) {
        Intent intent = new Intent(this, Add_newType.class);
        intent.putExtra("typeOfactivity","Purchases");
        typesOfPurchases.save();
        startActivity(intent);
    }
}
