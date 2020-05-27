package com.prikolastochka.finanser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Purchases;

public class DataAdapter extends  RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Purchases> purchasesArrayList;
    private MainActivity mainActivity;


    DataAdapter(ArrayList<Purchases> purchasesArrayList, MainActivity mainActivity) {
        this.purchasesArrayList = purchasesArrayList;
        this.mainActivity=mainActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Purchases purchases = purchasesArrayList.get(position);
            holder.sum.setText(""+purchases.getSum());
            holder.name.setText(""+purchases.getName());
            holder.date.setText("Дата: "+purchases.getData());
            holder.type.setText("Категория: "+purchases.getTypesOfPurchasesName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                    mainActivity.editPurchases(purchases,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return purchasesArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder //implements
//
    {


      TextView sum;
      TextView name;
      TextView type;
      TextView date;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            sum = itemView.findViewById(R.id.sum);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
        }


    }
}
