package com.example.vladkerasosi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Data.AppDatabase;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

public class Add_newType extends AppCompatActivity {

    private String typeOfactivity;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Редактирование категорий");

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            typeOfactivity=arguments.getString("typeOfactivity");
        }
    }

    private void addNewTypeOfPurchases(String name){
        appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,name));
    }

    private void addNewTypeOfProfit(String name){
        appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,name));
    }


    public void CreateNewType(View view)  {

        EditText editText=findViewById(R.id.editText5);
        String newType = editText.getText().toString();

        if(!TextUtils.isEmpty(editText.getText())) {

        if(typeOfactivity.equals("purchases")){
            addNewTypeOfPurchases(newType);
        }

        if(typeOfactivity.equals("profit")){
            addNewTypeOfProfit(newType);

        }
            Intent intent = new Intent(this, Add_new.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(Add_newType.this, "Введите название новой категории!", Toast.LENGTH_SHORT).show();
        }

    }
}
