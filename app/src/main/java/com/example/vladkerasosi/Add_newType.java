package com.example.vladkerasosi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import java.util.Objects;

import Data.AppDatabase;
import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

public class Add_newType extends AppCompatActivity {

    private String typeOfactivity;
    private AppDatabase appDatabase;
    private ArrayList<TypesOfPurchases> typesOfPurchases= new ArrayList<>();
    private ArrayList<Purchases> purchasesArrayList= new ArrayList<>();
    private ArrayList<TypeOfProfit> typesOfProfit=new ArrayList<>();
    private ArrayList<Profit> profitArrayList= new ArrayList<>();
    private ArrayList<String> types=new ArrayList<>();
    private ListView listView;
    private  ArrayAdapter<String> adapter;
    private TypeOfProfit defTypeProfit;
    private TypesOfPurchases defTypePurchases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Редактирование категорий");


        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"AppDB")
                .allowMainThreadQueries()
                .build();


    }
    private void setDefType(){

        if(typeOfactivity.equals("profit")) {
            for(TypeOfProfit typeOfProfit: typesOfProfit){
                if(typeOfProfit.getType_profit_name().equals("Другое")){
                    defTypeProfit=typeOfProfit;
                }
            }

        }

        if(typeOfactivity.equals("purchases")){
            for(TypesOfPurchases typeOfPur: typesOfPurchases){
                if(typeOfPur.getType_name_purchases().equals("Другое")){
                    defTypePurchases=typeOfPur;
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editType(final int position, final Boolean isUpdate){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Add_newType.this);
        alertDialogBuilderUserInput.setView(view);



        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);

        if((!types.isEmpty())&&isUpdate)  nameEditText.setText(types.get(position));
        priceEditText.setVisibility(View.GONE);
        decriptionEditText.setVisibility(View.GONE);
        dateEditText.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        if((!types.isEmpty())&&isUpdate){
            if(types.get(position).equals("Другое")){
                nameEditText.setEnabled(false);
            }
        }


        if(!types.isEmpty()){
            if(!(nameEditText.getText().toString().equals("Другое")))
            alertDialogBuilderUserInput.setCancelable(true)
                    .setPositiveButton(isUpdate ? "Обновить" : "Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alertDialogBuilderUserInput.setNegativeButton(isUpdate&& !(nameEditText.getText().toString().equals("Другое")) ? "Удалить" : "Отмена", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isUpdate&& !(nameEditText.getText().toString().equals("Другое"))) {

                                deleteType(position);

                            } else{

                                dialog.cancel();

                            }

                        }
                    } );

            if(isUpdate && !(nameEditText.getText().toString().equals("Другое"))) {
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
                    } else
                    if (nameEditText.getText().toString().equals("Другое")) {
                        Toast.makeText(Add_newType.this, "Введите другое название категории!", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        alertDialog.dismiss();
                    }

                    if (!types.isEmpty()) {

                        if(isUpdate){
                            updateType(nameEditText.getText().toString(),position);
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
            profitArrayList.clear();
            profitArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllProfit());
            typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());
        }

        if(typeOfactivity.equals("purchases")){
            typesOfPurchases.clear();
            purchasesArrayList.clear();
            purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
            typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
         }
    }

    private void setListView(){
        listView=findViewById(R.id.ListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
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
        setDefType();
        setListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editType(position, true);

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

            for(Profit profit: profitArrayList){

                if(typeOfProfit.getType_profit_name().equals(profit.getTypeOfProfitName())){
                    profit.setTypeOfProfit(defTypeProfit);
                    appDatabase.getPur_Pro_Dao().updateProfit(profit);
                }

            }

            appDatabase.getPur_Pro_Dao().deleteTypeOfProfit(typeOfProfit);
            typesOfProfit.remove(position);

        }
        if(typeOfactivity.equals("purchases")){
            TypesOfPurchases typeOfPur=typesOfPurchases.get(position);

            for(Purchases purchases: purchasesArrayList){

                if(typeOfPur.getType_name_purchases().equals(purchases.getTypesOfPurchasesName())){
                    purchases.setTypesOfPurchases(defTypePurchases);
                    appDatabase.getPur_Pro_Dao().updatePurchases(purchases);
                }

            }
            appDatabase.getPur_Pro_Dao().deleteTypeOfPurchases(typeOfPur);
            typesOfPurchases.remove(position);

        }
        adapter.notifyDataSetChanged();
        types.remove(position);

    }

    private void updateType(String name, int position){


        if(typeOfactivity.equals("profit")){
            TypeOfProfit typeOfProfit= typesOfProfit.get(position);
            TypeOfProfit oldTypeOfProfit= typesOfProfit.get(position);

            typeOfProfit.setType_profit_name(name);
            appDatabase.getPur_Pro_Dao().updateTypeOfProfit(typeOfProfit);
            typesOfProfit.set(position,typeOfProfit);
            types.set(position,typeOfProfit.getType_profit_name());

            for(Profit profit:profitArrayList){
                if(profit.getTypeOfProfitName().equals(oldTypeOfProfit.getType_profit_name())){
                    profit.setTypeOfProfit(typeOfProfit);
                    appDatabase.getPur_Pro_Dao().updateProfit(profit);
                }
            }



        }

        if(typeOfactivity.equals("purchases")){

            TypesOfPurchases typeOfPur= typesOfPurchases.get(position);
            TypesOfPurchases oldTypeOfPur= typesOfPurchases.get(position);

            typeOfPur.setType_name_purchases(name);
            appDatabase.getPur_Pro_Dao().updateTypeOfPurchases(typeOfPur);
            typesOfPurchases.set(position,typeOfPur);
            types.set(position,typeOfPur.getType_name_purchases());

            for(Purchases purchases:purchasesArrayList){
                if(purchases.getTypesOfPurchasesName().equals(oldTypeOfPur.getType_name_purchases())){
                    purchases.setTypesOfPurchases(typeOfPur);
                    appDatabase.getPur_Pro_Dao().updatePurchases(purchases);
                }
            }





        }

        adapter.notifyDataSetChanged();



    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreateNewType(View view)  {
        editType(-1, false);

    }
}
