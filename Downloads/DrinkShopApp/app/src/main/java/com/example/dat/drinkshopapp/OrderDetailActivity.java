package com.example.dat.drinkshopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dat.drinkshopapp.Adapter.OrderDetailAdapter;
import com.example.dat.drinkshopapp.Database.ModelDB.Cart;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    TextView txt_order_id, txt_order_price, txt_order_address, txt_order_status, txt_order_coment;
    Button btn_cancel;
    RecyclerView recycler_order_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        recycler_order_detail = findViewById(R.id.recycler_order_detail);
        recycler_order_detail.setLayoutManager(new LinearLayoutManager(this));
        recycler_order_detail.setHasFixedSize(true);

        txt_order_id = findViewById(R.id.txt_order_id);
        txt_order_price = findViewById(R.id.txt_order_price);
        txt_order_address = findViewById(R.id.txt_order_address);
        txt_order_status = findViewById(R.id.txt_order_status);
        txt_order_coment = findViewById(R.id.txt_order_comment);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });


        txt_order_id.setText(new StringBuilder("#").append(Common.currentOrder.getOderId()));
        txt_order_price.setText(new StringBuilder("$").append(Common.currentOrder.getOderPrice()));
        txt_order_address.setText(Common.currentOrder.getOderAddress());
        txt_order_status.setText(new StringBuilder("Order Status: ")
                .append(Common.convertCodeToStatus(Common.currentOrder.getOderStatus())));
        txt_order_coment.setText(new StringBuilder("Comment: ").append(Common.currentOrder.getOderComment()));

        displayOrderDetail();
    }

    private void cancelOrder() {
        DinkShopAPI  iDrinkShopAPI = Common.getAPI();
        iDrinkShopAPI.cancelOrder(String.valueOf(Common.currentOrder.getOderId()),
                Common.curenuser.getPhone())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(OrderDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();

                        if (response.body().contains("Order has been cancelled"))
                            finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("DEBUG", t.getMessage());
                    }
                });
    }

    private void displayOrderDetail() {
        List<Cart> orderDetail = new Gson().fromJson(Common.currentOrder.getOderDetail(),
                new TypeToken<List<Cart>>() {
                }.getType());

        recycler_order_detail.setAdapter(new OrderDetailAdapter(this, orderDetail));

    }
}
