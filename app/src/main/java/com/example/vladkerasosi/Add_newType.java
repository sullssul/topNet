package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Data.AppDatabase;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

public class Add_newType extends AppCompatActivity {

    private String typeOfactivity;
    private AppDatabase appDatabase;
    private ArrayList<TypesOfPurchases> typesOfPurchases= new ArrayList<>();
    private ArrayList<TypeOfProfit> typesOfProfit=new ArrayList<>();
    private ArrayList<String> types=new ArrayList<>();
    private ListView listView;
    private  ArrayAdapter<String> adapter;
    private AdapterView<?> parent;

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

    private void editType(AdapterView<?> parent, View viewww, final int position, long id, final Boolean isUpdate){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Add_newType.this);
        alertDialogBuilderUserInput.setView(view);



        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);

        if((!types.isEmpty())&&isUpdate)
        nameEditText.setText(types.get(position));
        priceEditText.setVisibility(View.GONE);
        decriptionEditText.setVisibility(View.GONE);
        dateEditText.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);


        if(!types.isEmpty()){
            alertDialogBuilderUserInput.setCancelable(true)
                    .setPositiveButton(isUpdate ? "Обновить" : "Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(isUpdate ? "Удалить" : "Отмена", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isUpdate) {

                                deleteType(position);

                            } else{

                                dialog.cancel();

                            }

                        }
                    } );

            if(isUpdate) {
                alertDialogBuilderUserInput.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
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
                        Toast.makeText(Add_newType.this, "Введите название Категории!", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        alertDialog.dismiss();
                    }

                    if (!types.isEmpty()) {

                        if(isUpdate){
                            updateType(0,nameEditText.getText().toString(),position);
                        } else {
                            addNewType(nameEditText.getText().toString());
                        }

                    }

                }
            });

        }

    }

    private void loadData(){

        if(typeOfactivity.equals("profit")) {
            typesOfProfit.clear();
            typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());
        }

        if(typeOfactivity.equals("purchases")){
            typesOfPurchases.clear();
            typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
         }
    }

    private void setListView(){
        listView=findViewById(R.id.ListView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,types);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void convertToString(){
        types.clear();
        if(typeOfactivity.equals("purchases")) {
            for (TypesOfPurchases typePurchases : typesOfPurchases) {
                types.add(typePurchases.getType_name_purchases());

            }
        }

        if(typeOfactivity.equals("profit")){
            for (TypeOfProfit typeProfit : typesOfProfit) {
                types.add(typeProfit.getType_profit_name());

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            typeOfactivity=arguments.getString("typeOfactivity");
        }

        loadData();
        convertToString();
        setListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editType(parent,view,position,id,true);

            }
        });

    }

    private void addNewType(String name){

        if(typeOfactivity.equals("profit"))  {
            appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0,name));

        }

        if(typeOfactivity.equals("purchases")) {
            appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0,name));
        }
        loadData();
        convertToString();
        adapter.notifyDataSetChanged();


    }

    private void deleteType(int position){
        if(typeOfactivity.equals("profit")) {
            TypeOfProfit typeOfProfit=typesOfProfit.get(position);
            appDatabase.getPur_Pro_Dao().deleteTypeOfProfit(typeOfProfit);
            typesOfProfit.remove(position);

        }
        if(typeOfactivity.equals("purchases")){
            TypesOfPurchases typeOfPur=typesOfPurchases.get(position);
            appDatabase.getPur_Pro_Dao().deleteTypeOfPurchases(typeOfPur);
            typesOfPurchases.remove(position);

        }
        adapter.notifyDataSetChanged();
        types.remove(position);

    }

    private void updateType(long id,String name,int position){


        if(typeOfactivity.equals("profit")){
            TypeOfProfit typeOfProfit= typesOfProfit.get(position);
            typeOfProfit.setType_profit_name(name);
            appDatabase.getPur_Pro_Dao().updateTypeOfProfit(typeOfProfit);
            typesOfProfit.set(position,typeOfProfit);
            types.set(position,typeOfProfit.getType_profit_name());

        }

        if(typeOfactivity.equals("purchases")){
            TypeOfProfit typeOfProfit= typesOfProfit.get(position);
            typeOfProfit.setType_profit_name(name);
            appDatabase.getPur_Pro_Dao().updateTypeOfProfit(typeOfProfit);
            typesOfProfit.set(position,typeOfProfit);
            types.set(position,typeOfProfit.getType_profit_name());

        }

        adapter.notifyDataSetChanged();



    }



    public void CreateNewType(View view)  {
        editType(null,null,-1,-1,false);

    }
}
