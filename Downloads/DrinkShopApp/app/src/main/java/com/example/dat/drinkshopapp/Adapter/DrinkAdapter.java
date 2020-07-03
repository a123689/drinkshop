package com.example.dat.drinkshopapp.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dat.drinkshopapp.Database.ModelDB.Cart;
import com.example.dat.drinkshopapp.Database.ModelDB.Favorite;
import com.example.dat.drinkshopapp.Interface.IItemClickListener;
import com.example.dat.drinkshopapp.Model.Drink;
import com.example.dat.drinkshopapp.R;
import com.example.dat.drinkshopapp.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    Context context;
    List<Drink> drinkList;


    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;

    }
    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drink_item_layout, null, false);
       // RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  view.setLayoutParams(lp);
        return new DrinkViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {
        holder.txt_price.setText(new StringBuffer("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);
        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(holder.img_product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(position);
            }
        });


        if (Common.favoriteRespository.isFavorite(Integer.parseInt(drinkList.get(position).Id)) == 1)
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        else
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);

        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Common.favoriteRespository.isFavorite(Integer.parseInt(drinkList.get(position).Id)) != 1) {
                    addOrRemoveToFavorite(drinkList.get(position), true);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_white_24dp);

                } else {

                    addOrRemoveToFavorite(drinkList.get(position), false);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                }
            }
        });
    }

    private void addOrRemoveToFavorite(Drink drink, boolean isAdd) {

        Favorite favorite = new Favorite();
        favorite.id = drink.Id;
        favorite.link = drink.Link;
        favorite.name = drink.Name;
        favorite.price = drink.Price;
        favorite.menuId = drink.MenuId;

        if (isAdd){

            Common.favoriteRespository.insertFav(favorite);
        }
        else
        {

            Common.favoriteRespository.delete(favorite);
        }

    }

    @Override
    public int getItemCount() {
        return  drinkList.size();
    }

    private void showAddToCartDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_cart, null);



        ImageView img_product_dialog = itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = itemView.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeM = itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sizeOfCup = 0;
            }
        });
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sizeOfCup = 1;
            }
        });


        RadioButton rdi_sugar_100 = itemView.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = itemView.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = itemView.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = itemView.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = itemView.findViewById(R.id.rdi_sugar_free);


        rdi_sugar_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 30;
            }
        });
        rdi_sugar_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 50;
            }
        });
        rdi_sugar_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 70;
            }
        });

        rdi_sugar_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 100;
            }
        });

        rdi_sugar_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.sugar = 0;
            }
        });




        RadioButton rdi_ice_100 = itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 30;
            }
        });
        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 50;
            }
        });
        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 70;
            }
        });
        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 100;
            }
        });
        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.ice = 0;
            }
        });


        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_topping);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        MultiChoiceAdapter multiChoiceAdapter = new MultiChoiceAdapter(context, Common.toppinglist);
        recyclerView.setAdapter(multiChoiceAdapter);
        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialog.setText(drinkList.get(position).Name);
        builder.setView(itemView);

        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Common.sizeOfCup == -1) {
                    Toast.makeText(context, "Please choose size of cup", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.sugar == -1) {
                    Toast.makeText(context, "Please chose sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.ice == -1) {
                    Toast.makeText(context, "Please chose Ice", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialogInterface.dismiss();

                showConfirmDialog(position, txt_count.getNumber());

                dialogInterface.dismiss();


            }
        });

        builder.show();

    }

    private void showConfirmDialog(final int position, final String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout, null);

        //View
        ImageView img_product_dialog = itemView.findViewById(R.id.img_confirm_product);
        final TextView txt_cart_product_name = itemView.findViewById(R.id.txt_confirm_product_name);
        final TextView txt_cart_product_price = itemView.findViewById(R.id.txt_confirm_product_price);
        TextView txt_sugar = itemView.findViewById(R.id.txt_sugar);
        TextView txt_ice = itemView.findViewById(R.id.txt_ice);
        final TextView txt_topping_extra = itemView.findViewById(R.id.txt_topping_extra);

        //Log.e("clcmmn", i + " \n " + number + "\n " + sizeOfCup + "\n " + sugar + "\n " + ice);
        Picasso.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        txt_cart_product_name.setText(new StringBuilder(drinkList.get(position).Name).append(" x")
                        .append(number).append(Common.sizeOfCup == 0 ? " Size M" : " Size L"));

        txt_ice.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());


        double price = (Double.parseDouble(drinkList.get(position).Price) * Double.parseDouble(number)) + Common.toppingPrice;
        if (Common.sizeOfCup == 1)//size L
            price += (3.0 * Double.parseDouble(number));

        StringBuilder topping_final_commment = new StringBuilder("");



        for (String line : Common.toppingAdded){

            topping_final_commment.append(line).append("\n");

        }

        Toast.makeText(context, ""+Common.toppingAdded.size(), Toast.LENGTH_SHORT).show();


        txt_topping_extra.setText(topping_final_commment);





        final double finalPrice = Math.round(price);

        txt_cart_product_price.setText(new StringBuilder("$").append(finalPrice));

        builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                try {

                    Cart cartItem = new Cart();
                    cartItem.name = drinkList.get(position).Name;
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.price = finalPrice;
                    cartItem.size=Common.sizeOfCup;
                    cartItem.toppingExtras = txt_topping_extra.getText().toString();
                    cartItem.link = drinkList.get(position).Link;
                    Common.cartRespository.insertToCart(cartItem);
                    Log.d("JSON_RoomDB", new Gson().toJson(cartItem));
                    Toast.makeText(context, "Save item to cart success", Toast.LENGTH_SHORT).show();

                } catch (Exception ex) {
                    Log.d("ss",ex.getMessage());
                }


            }
        });
        builder.setView(itemView);
        builder.show();


    }


}
