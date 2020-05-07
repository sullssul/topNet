package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class Settings_Activity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    float Limit = 0;
    SharedPreferences sPref;
    Boolean NotifLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch sw = findViewById(R.id.limit_switch);
        sw.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Switch sw = findViewById(R.id.limit_switch);

//      Switch swDarkMode=findViewById(R.id.dark_mode);
//      swDarkMode.setChecked(true);
//        swDarkMode.setOnCheckedChangeListener(this);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enambledEditText(true);
                    NotifLimit = true;
                    SaveNotifLimit();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "NotifLimit = " + NotifLimit, Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (!isChecked) {
                    enambledEditText(false);
                    NotifLimit = false;
                    SaveNotifLimit();
                }
            }
        });
        EditText editText = findViewById(R.id.editText);
        LoadNotifLimit();
        if (NotifLimit) {
            sw.setChecked(true);
            editText.setEnabled(true);
        } else {
            sw.setChecked(false);
            editText.setEnabled(false);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "NotifLimit = " + NotifLimit, Toast.LENGTH_SHORT);
        toast.show();
        LoadLimit();
        setEditText();
    }

    public void setEditText() {
        EditText editText = findViewById(R.id.editText);
        editText.setText(Limit + "");
    }

    public void setLimit() {
        EditText editText = findViewById(R.id.editText);
        Limit = Float.parseFloat(String.valueOf(editText.getText()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setLimit();
        SaveLimit();

        //   SaveNotifLimit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        setLimit();
        SaveLimit();

        //    SaveNotifLimit();
    }

    public void SaveNotifLimit() {

        sPref = getSharedPreferences("NotifLimit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("NotifLimit", NotifLimit);
        editor.apply();
    }

    public void LoadNotifLimit() {
        sPref = getSharedPreferences("NotifLimit", MODE_PRIVATE);
        NotifLimit = sPref.getBoolean("NotifLimit", true);
    }

    public void SaveLimit() {

        sPref = getSharedPreferences("Limit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putFloat("Limit", Limit);
        editor.apply();
    }

    public void LoadLimit() {
        sPref = getSharedPreferences("Limit", MODE_PRIVATE);
        Limit = sPref.getFloat("Limit", 0);
    }

    public void enambledEditText(boolean lol) {
        EditText editText = findViewById(R.id.editText);
        editText.setEnabled(lol);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
