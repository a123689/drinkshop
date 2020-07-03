package com.example.dat.drinkshopapp.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.Interface.IItemClickListener;
import com.example.dat.drinkshopapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView img_product;
    TextView txt_menu_name;

    IItemClickListener iItemClickListener;
    public CategoryViewHolder(@NonNull View itemView, IItemClickListener iTemClickListener) {
        super(itemView);
        this.iItemClickListener = iTemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        img_product = itemView.findViewById(R.id.image_product);
        txt_menu_name = itemView.findViewById(R.id.txt_menu_name);

        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        iItemClickListener.onClick(v);
    }
}
