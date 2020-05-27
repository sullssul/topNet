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

import Model.Profit;

public class Data_adapter_profit extends  RecyclerView.Adapter<Data_adapter_profit.ViewHolder> {

    private ArrayList<Profit> profitArrayList;
    private Profit_Activity profit_activity;


    Data_adapter_profit(ArrayList<Profit> profitArrayList,Profit_Activity profit_activity) {
        this.profitArrayList = profitArrayList;
        this.profit_activity=profit_activity;
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
        final Profit profit = profitArrayList.get(position);
        holder.sum.setText(""+profit.getSum());
        holder.name.setText(""+profit.getName());
        holder.date.setText("Дата: "+profit.getDate());
        holder.type.setText("Категория: "+profit.getTypeOfProfitName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                profit_activity.editProfit(profit,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return profitArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder //implements
//            View.OnClickListener
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
