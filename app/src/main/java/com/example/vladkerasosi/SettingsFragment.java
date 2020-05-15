package com.example.vladkerasosi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;


import androidx.core.content.ContextCompat;
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
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener
         {

    private ArrayList<TypesOfPurchases> typesOfPurchases = new ArrayList<>();
    private ArrayList<Purchases> purchasesArrayList = new ArrayList<>();
    private ArrayList<TypeOfProfit> typesOfProfit = new ArrayList<>();
    private ArrayList<Profit> profitArrayList = new ArrayList<>();
    private AppDatabase appDatabase;
    private Context context;
    private Settings_Activity settings_activity;


             @SuppressLint("ValidFragment")
    public SettingsFragment(Context context, Settings_Activity settings_activity) {
        this.context = context;
        this.settings_activity = settings_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDB")
                .allowMainThreadQueries()
                .build();

//        SharedPreferences sPrefSettings = PreferenceManager.getDefaultSharedPreferences(context);

        purchasesArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllPurchases());
        typesOfPurchases.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfPurchases());
        profitArrayList.addAll(appDatabase.getPur_Pro_Dao().getAllProfit());
        typesOfProfit.addAll(appDatabase.getPur_Pro_Dao().getAllTypeOfProfit());

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            if (!(preference instanceof SwitchPreference)) {
                String value = sharedPreferences.getString(preference.getKey(),
                        "");
                setPreferenceLabel(preference, value);
            }
        }

        Preference preference = findPreference("Limit");
        preference.setOnPreferenceChangeListener(this);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
   }

    private void setPreferenceLabel(Preference preference, String value) {
      if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }

    }


    @SuppressLint("ShowToast")
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        Toast toast = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            toast = Toast.makeText(getContext(), "Введите число!", Toast.LENGTH_LONG);
        }

        if (preference.getKey().equals("Limit")) {
            String LimitString =(String) o;

            try {
              Float.parseFloat(LimitString);
            } catch (NumberFormatException nef) {
                assert toast != null;
                toast.show();
                return false;
            }
        }

        return true;
    }




    @SuppressLint("ShowToast")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        if (!(preference instanceof SwitchPreference)) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceLabel(preference, value);
        }
        if (preference.getKey().equals("DarkMode")) {
            Toast toast = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                toast = Toast.makeText(getContext(), "Тема изменится после перезапуска \tприложения", Toast.LENGTH_LONG);
            }
            toast.show();


        }
    }





    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 300, 20, 20);
        assert v != null;
        v.setLayoutParams(params);

        Button btn = new Button(getActivity().getApplicationContext());
        btn.setText("Удалить все данные");
        Button btn3 = new Button(getActivity().getApplicationContext());
        btn3.setText("Сбросить все категории");

        btn.setLayoutParams(params);
        params.setMargins(20, 20, 20, 20);
        btn3.setLayoutParams(params);
        int backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        btn.setBackgroundColor(backgroundColor);

        btn3.setBackgroundColor(backgroundColor);

        v.addView(btn);
        v.addView(btn3);

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = layoutInflaterAndroid.inflate(R.layout.edit_item, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(settings_activity);
        alertDialogBuilderUserInput.setView(view);

        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);
        final EditText decriptionEditText = view.findViewById(R.id.descriptiontEdit);
        final EditText dateEditText = view.findViewById(R.id.dateEditText);
        final Spinner spinner = view.findViewById(R.id.spinerEdit);
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

                                for (Profit profit : profitArrayList) {
                                    appDatabase.getPur_Pro_Dao().deleteProfit(profit);
                                }


                                for (Purchases purchases : purchasesArrayList) {
                                    appDatabase.getPur_Pro_Dao().deletePurchases(purchases);
                                }

                            }
                        })

                        .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                alertDialogBuilderUserInput.setCancelable(true)
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                for (TypeOfProfit tpProf : typesOfProfit) {
                                    appDatabase.getPur_Pro_Dao().deleteTypeOfProfit(tpProf);
                                }

                                for (TypesOfPurchases tpPur : typesOfPurchases) {
                                    appDatabase.getPur_Pro_Dao().deleteTypeOfPurchases(tpPur);
                                }

                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Зарплата"));
                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Пенсия"));
                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Стипендия"));
                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Вклад"));
                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Продажа вещей"));
                                appDatabase.getPur_Pro_Dao().addTypeOfProfit(new TypeOfProfit(0, "Другое"));


                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Авто"));
                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Еда"));
                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Медицина"));
                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Развлечения"));
                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Связь"));
                                appDatabase.getPur_Pro_Dao().addTypeOfPurchases(new TypesOfPurchases(0, "Другое"));

                            }
                        })

                        .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
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

        return v;
    }

    private void delBalance() {
        SharedPreferences sPref = context.getSharedPreferences("Balance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat("Balance", 0);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}


