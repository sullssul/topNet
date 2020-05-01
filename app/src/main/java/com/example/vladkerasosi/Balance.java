package com.example.vladkerasosi;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Balance {
    private float balance;
    SharedPreferences sPref;


    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

}
