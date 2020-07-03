package com.example.dat.drinkshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dat.drinkshopapp.Adapter.OrderAdapter;
import com.example.dat.drinkshopapp.Model.Oder;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowOderActivity extends AppCompatActivity {

    DinkShopAPI mService;
    RecyclerView recycler_orders;
    CompositeDisposable  compositeDisposable = new CompositeDisposable();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_oder);

        mService = Common.getAPI();


        recycler_orders = findViewById(R.id.recycler_orders);
        recycler_orders.setLayoutManager(new LinearLayoutManager(this));
        recycler_orders.setHasFixedSize(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.order_new) {
                    loadOrder("0");
                } else if (menuItem.getItemId() == R.id.order_cancel) {
                    loadOrder("-1");
                } else if (menuItem.getItemId() == R.id.order_processing) {
                    loadOrder("1");
                } else if (menuItem.getItemId() == R.id.order_shipping) {
                    loadOrder("2");
                } else if (menuItem.getItemId() == R.id.order_shipped) {
                    loadOrder("3");
                }
                return true;
            }
        });





    }


    private void loadOrder(String statusCode) {
        if (Common.curenuser.getPhone() != null) {

            compositeDisposable.add(mService.getOrder(
                    statusCode,
                    Common.curenuser.getPhone()
            ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Oder>>() {
                        @Override
                        public void accept(List<Oder> orders) throws Exception {
                            displayOrder(orders);
                        }
                    }));
        } else {
            Toast.makeText(this, "Please logging again", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void displayOrder(List<Oder> orders) {
        OrderAdapter adapter = new OrderAdapter(this, orders);
        recycler_orders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrder("0");
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
