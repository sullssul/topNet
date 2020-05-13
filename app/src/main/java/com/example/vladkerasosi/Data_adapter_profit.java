package com.example.vladkerasosi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Profit;

public class Data_adapter_profit extends  RecyclerView.Adapter<Data_adapter_profit.ViewHolder> {

    private ArrayList<Profit> profitArrayList;
    private Context context;
    private Profit_Activity profit_activity;


    Data_adapter_profit(ArrayList<Profit> profitArrayList, Context context,Profit_Activity profit_activity) {
        this.profitArrayList = profitArrayList;
        this.context = context;
        this.profit_activity=profit_activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Profit profit = profitArrayList.get(position);
        String title = profit.getSum() + ";\t" + profit.getName() + ";\tКатегория: " + profit.getTypeOfProfitName() + ";\tДата: " + profit.getDate();

        holder.title.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    class ViewHolder extends RecyclerView.ViewHolder //implements
//            View.OnClickListener
    {

        //  public ImageView purcaheseImg;
        public TextView title;
        //    public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // itemView.setOnClickListener(this);

            // purcaheseImg =
            title = itemView.findViewById(R.id.title);

        }




    }
}
