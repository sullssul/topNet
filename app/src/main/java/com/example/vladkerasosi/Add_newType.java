package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import Model.TypesOfPurchases;

public class Add_newType extends AppCompatActivity {
    String typeOfactivity;
    String newType="";
    SharedPreferences sPref;
    TypesOfPurchases typesOfPurchases=new TypesOfPurchases(this);
//    ArrayList<String> typesOfPurchases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type);

    }

    @Override
    protected void onStart() {
        super.onStart();
        typesOfPurchases.load();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            typeOfactivity=arguments.getString("typeOfactivity");
        }
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



    public void CreateNewType(View view)  {

        EditText editText=findViewById(R.id.editText5);
        newType=editText.getText().toString();

        if(typeOfactivity.equals("Purchases")){
            Intent intent = new Intent(this, Add_new.class);
            typesOfPurchases.add(newType);
            typesOfPurchases.save();
            startActivity(intent);
        }

        if(typeOfactivity.equals("Profit")){
            Intent intent = new Intent(this, Add_new_profit.class);
//            intent.putExtra("newType",newType);
            startActivity(intent);
        }

    }
}
