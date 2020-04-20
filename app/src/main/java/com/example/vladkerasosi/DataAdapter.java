package com.example.vladkerasosi;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends  RecyclerView.Adapter<DataAdapter.ViewHolder> {

    ArrayList<Purchases> purchasesArrayList;
    Context context;

    public DataAdapter(ArrayList<Purchases> purchasesArrayList, Context context) {
        this.purchasesArrayList = purchasesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Purchases purchases = purchasesArrayList.get(position);
        String title = purchases.getSum() + ";\t" + purchases.getName() + ";\tКатегория: " + purchases.getTypeOfPurchases() + ";\tДата: " + purchases.getData();
      //  holder.purcaheseImg.setImageResource(purchases.getImgRes());
        holder.title.setText(title);
        //holder.description.setText(purchases.getDescription());
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
         //   description = itemView.findViewById(R.id.description);
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
