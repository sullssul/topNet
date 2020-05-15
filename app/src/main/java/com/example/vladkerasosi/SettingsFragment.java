package com.example.vladkerasosi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Objects;

import Data.AppDatabase;
import Model.Profit;
import Model.Purchases;
import Model.TypeOfProfit;
import Model.TypesOfPurchases;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener   {

    private ArrayList<TypesOfPurchases> typesOfPurchases=new ArrayList<TypesOfPurchases>();
    private ArrayList<Purchases> purchasesArrayList=new ArrayList<Purchases>();
    private ArrayList<TypeOfProfit> typesOfProfit=new ArrayList<>();
    private ArrayList<Profit> profitArrayList=new ArrayList<Profit>();
    private AppDatabase appDatabase;
    private SharedPreferences sPref;
    private Context context;
    private Settings_Activity settings_activity;

    @SuppressLint("ValidFragment")
    public SettingsFragment(Context context,Settings_Activity settings_activity){
        this.context=context;
        this.settings_activity=settings_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDB")
                .allowMainThreadQueries()
                .build();
        purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
        profitArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllProfit());
        typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());


    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 300, 20, 20);
        v.setLayoutParams(params);

        Button btn = new Button(getActivity().getApplicationContext());
        btn.setText("Удалить все данные");
        Button btn3 = new Button(getActivity().getApplicationContext());
        btn3.setText("Сбросить все категории");
        btn.setLayoutParams(params);
        params.setMargins(20, 20, 20, 20);
        btn3.setLayoutParams(params);
        btn.setBackgroundColor(R.color.colorPrimary);

        btn3.setBackgroundColor(R.color.colorPrimary);

        v.addView(btn);
        v.addView(btn3);

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view =layoutInflaterAndroid.inflate(R.layout.edit_item, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(settings_activity);
        alertDialogBuilderUserInput.setView(view);

        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText=view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText=view.findViewById(R.id.dateEditText);
        final Spinner spinner=view.findViewById(R.id.spinerEdit);
        TextView titleTV = view.findViewById(R.id.titleTV);

        priceEditText.setVisibility(View.GONE);
        decriptionEditText.setVisibility(View.GONE);
        dateEditText.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        nameEditText.setText("Вы действительно хотите удалить данные?");
        nameEditText.setEnabled(false);
        titleTV.setText("Подтвердите действие");


        btn.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.KITKAT)
             @Override
             public void onClick(View v) {

                 alertDialogBuilderUserInput.setCancelable(true)
                         .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                                 delBalance();

                                 for(Profit profit:profitArrayList){
                                     appDatabase.getPur_Pro_Dao().deleteProfit(profit);
                                 }


                                 for(Purchases purchases:purchasesArrayList){
                                     appDatabase.getPur_Pro_Dao().deletePurchases(purchases);
                                 }

                             }
                         })

                         .setNeutralButton("Отмена",new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog,
                                                 int id) {
                                 dialog.cancel();
                             }
                         });
                 final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
                 Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.backgroundDialog);
                 alertDialog.show();

             }
         });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialogBuilderUserInput.setCancelable(true)
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                for(TypeOfProfit tpProf: typesOfProfit){
                                    appDatabase.getPur_Pro_Dao().deleteTypeOfProfit(tpProf);
                                }

                                for(TypesOfPurchases tpPur: typesOfPurchases){
                                    appDatabase.getPur_Pro_Dao().deleteTypeOfPurchases(tpPur);
                                }

                            }
                        })

                        .setNeutralButton("Отмена",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.backgroundDialog);
                alertDialog.show();

            }
        });

        return v;
    }

    private void delBalance(){
        sPref = context.getSharedPreferences("Balance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat("Balance", 0);
        editor.apply();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
