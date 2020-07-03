package com.example.dat.drinkshopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.Database.ModelDB.Favorite;
import com.example.dat.drinkshopapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavotiteAdapter extends RecyclerView.Adapter<FavotiteAdapter.Holder> {
    Context context;
    List<Favorite> favoriteList;

    public FavotiteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item_layout, null, false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Picasso.with(context).load(favoriteList.get(position).link).into(holder.img_product);
        holder.txt_price.setText(new StringBuilder("$").append(favoriteList.get(position).price).toString());
        holder.txt_product_name.setText(favoriteList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView img_product;
        TextView txt_product_name, txt_price;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public Holder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);

            view_background = itemView.findViewById(R.id.view_background);
            view_foreground = itemView.findViewById(R.id.view_foregound);

        }
    }

    public void removeItem(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorite item, int position) {
        favoriteList.add(position, item);
        notifyItemInserted(position);
    }
}
