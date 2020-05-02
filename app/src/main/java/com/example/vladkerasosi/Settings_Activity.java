package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class Settings_Activity extends AppCompatActivity {
    float Limit=0;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadLimit();
        setEditText();
    }

    public void setEditText(){
        EditText editText=findViewById(R.id.editText);
        editText.setText(Limit+"");
    }

    public void setLimit(){
        EditText editText=findViewById(R.id.editText);
        Limit=Float.parseFloat(String.valueOf(editText.getText()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setLimit();
        SaveLimit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setLimit();
        SaveLimit();
    }

    public void SaveLimit(){

            sPref = getSharedPreferences("Limit",MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putFloat("Limit",Limit);
            editor.apply();
    }

    public void LoadLimit(){
        sPref = getSharedPreferences("Limit",MODE_PRIVATE);
        Limit = sPref.getFloat("Limit", 0);
    }

}
