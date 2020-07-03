package com.example.dat.drinkshopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat.drinkshopapp.Model.Drink;
import com.example.dat.drinkshopapp.R;
import com.example.dat.drinkshopapp.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.Holder> {
    Context context;
    List<Drink> optionsList;

    public MultiChoiceAdapter(Context context, List<Drink> optionsList) {
        this.context = context;
        this.optionsList = optionsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.multi_check_layout, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        Common.toppingAdded.clear();
        Common.toppingPrice = 0.0;
        Common.sizeOfCup = -1;
        Common.sugar = -1;
        Common.ice =-1;
        Picasso.with(context).load(optionsList.get(i).Link).into(holder.img_options_topping);
        holder.checkBox.setText(optionsList.get(i).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                        Common.toppingAdded.add(compoundButton.getText().toString());
                        Common.toppingPrice += Double.parseDouble(optionsList.get(i).Price);


                } else {

                        Common.toppingAdded.remove(compoundButton.getText().toString());
                        Common.toppingPrice -= Double.parseDouble(optionsList.get(i).Price);


                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView img_options_topping;

        public Holder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.ckb_topping);
            img_options_topping = itemView.findViewById(R.id.img_options_topping);

        }
    }
}
