package com.example.vladkerasosi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Purchases;

public class DataAdapter extends  RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Purchases> purchasesArrayList;
    private Context context;
    private MainActivity mainActivity;

    public DataAdapter(ArrayList<Purchases> purchasesArrayList, Context context,MainActivity mainActivity) {
        this.purchasesArrayList = purchasesArrayList;
        this.context = context;
        this.mainActivity=mainActivity;
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
        final Purchases purchases = purchasesArrayList.get(position);
        String title = purchases.getSum() + ";\t"
                + purchases.getName()
                + ";\tКатегория: "
                + purchases.getTypesOfPurchasesName()
                + ";\tДата: " + purchases.getData();

        holder.title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    class ViewHolder extends RecyclerView.ViewHolder //implements
//            View.OnClickListener
    {

      //  public ImageView purcaheseImg;
        public TextView title;
    //    public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // itemView.setOnClickListener(this);

           // purcaheseImg = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
         //
            //
            //    description = itemView.findViewById(R.id.description);
        }



//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//            Purchases purchases = purchasesArrayList.get(position);
//
//
//            Intent intent = new Intent(context, RecipeActivity.class);
//            intent.putExtra("imageResource", pizzaRecipeItem.getImageResource());
//            intent.putExtra("title", pizzaRecipeItem.getTitle());
//            intent.putExtra("description", pizzaRecipeItem.getDescription());
//            intent.putExtra("recipe", pizzaRecipeItem.getRecipe());
//            context.startActivity(intent);
//        }
    }
}
