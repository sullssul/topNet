package com.example.vladkerasosi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class Profit_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> typesOfProfit=new ArrayList<String>();
    ArrayList<Purchases> profitArrayList=new ArrayList<Purchases>();
    HashMap<String,Float> piechartItem=new HashMap<String, Float>();
    float Balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
    }

    public void tap_add_profit(View view) {


    }
}