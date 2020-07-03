package com.example.dat.drinkshopapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.Interface.IItemClickListener;
import com.example.dat.drinkshopapp.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView img_product;
    TextView txt_drink_name, txt_price;

    IItemClickListener iTemClickListener;
    ImageView btn_add_to_cart, btn_favorite;

    public DrinkViewHolder(@NonNull View itemView, IItemClickListener iTemClickListener) {
        super(itemView);
        this.iTemClickListener = iTemClickListener;
    }

    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);
        img_product = itemView.findViewById(R.id.image_product);
        txt_drink_name = itemView.findViewById(R.id.txt_drink_name);
        txt_price = itemView.findViewById(R.id.txt_price);
        btn_add_to_cart = itemView.findViewById(R.id.btn_add_cart);
        btn_favorite = itemView.findViewById(R.id.btn_favorite);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        iTemClickListener.onClick(view);
    }
}

