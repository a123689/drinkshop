package com.example.dat.drinkshopapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.dat.drinkshopapp.Adapter.CartAdapter;
import com.example.dat.drinkshopapp.Database.ModelDB.Cart;
import com.example.dat.drinkshopapp.Model.DataMessage;
import com.example.dat.drinkshopapp.Model.MyResponse;
import com.example.dat.drinkshopapp.Model.OrderResult;
import com.example.dat.drinkshopapp.Model.Token;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Retrofit.IFCMService;
import com.example.dat.drinkshopapp.Utils.Common;
import com.example.dat.drinkshopapp.Utils.RecyclerItemTouchHelper;
import com.example.dat.drinkshopapp.Utils.RecyclerItemTouchHelperListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_cart;
    Button btn_place_order;
    CartAdapter adapter;
    RelativeLayout rootLayout;
    List<Cart> cartList  = new ArrayList<Cart>();
    CompositeDisposable compositeDisposable;
    DinkShopAPI mService;
    String token, amount, orderAddress, orderComment;
    HashMap<String, String> params;
    int  PAYMENT_REQUEST_CODE =1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        compositeDisposable = new CompositeDisposable();
        mService = Common.getAPI();
        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);
        btn_place_order = findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                placeOrder();
            }
        });
        rootLayout = findViewById(R.id.rootLayout);
        loadCartItems();
      //  loadToken();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        if(Common.cartRespository.sumPrice() > 0){
            btn_place_order.setVisibility(View.VISIBLE);
        }else {
            btn_place_order.setVisibility(View.GONE);
        }

    }

    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRespository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItems(carts);
                            }
                        })
        );
    }



    private void displayCartItems(List<Cart> carts) {
        cartList = carts;
        adapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(adapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.Holder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deleteItem = cartList.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            //deleteItem from adapter
            adapter.removeItem(deleteIndex);
            //deleteItem from RoomDataBase
            Common.cartRespository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" removed from Favorites List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    Common.cartRespository.insertToCart(deleteItem);
                    btn_place_order.setVisibility(View.GONE);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            btn_place_order.setVisibility(View.GONE);
        }

    }

    private void placeOrder() {


        if (Common.curenuser.getName() != null) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submit order");

            View view = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

            final EditText edt_comment = view.findViewById(R.id.edt_comment);
            final EditText edt_orther_address = view.findViewById(R.id.edt_other_address);

            final RadioButton rdi_user_address = view.findViewById(R.id.rdi_user_address);
            final RadioButton rdi_other_address = view.findViewById(R.id.rdi_other_address);

            //event
            rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        edt_orther_address.setEnabled(false);
                }
            });

            rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        edt_orther_address.setEnabled(true);
                }
            });

            builder.setView(view);
            builder.setNegativeButton("Cacel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setPositiveButton("submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    orderComment = edt_comment.getText().toString();

                    if (rdi_user_address.isChecked())
                        orderAddress = Common.curenuser.getAddress();
                    else if (rdi_other_address.isChecked())
                        orderAddress = edt_orther_address.getText().toString();
                    else
                        orderAddress = "";

                    compositeDisposable.add(Common.cartRespository.getCartItems()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<List<Cart>>() {
                                @Override
                                public void accept(List<Cart> carts) throws Exception {

                                    if(!TextUtils.isEmpty(orderAddress)){

                                        sendOderToServer(Common.cartRespository.sumPrice(),carts,orderComment,orderAddress);
                                    }else {
                                        Toast.makeText(CartActivity.this, "Oder Address can't null", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }));



                }
            });
            builder.show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("NOT LOGIN?");
            builder.setMessage("Please login or register account to submit order");
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    startActivity(new Intent(CartActivity.this, MainActivity.class));

                }
            });
            builder.show();
        }

    }

    private void sendOderToServer(float price,List<Cart> carts,String orderComment,String orderAddress){
        if(carts.size() >0){
            String orderDetail = new Gson().toJson(cartList);
            mService.submitOrder(price, orderDetail, orderComment, orderAddress, Common.curenuser.getPhone())
                    .enqueue(new Callback<OrderResult>() {
                        @Override
                        public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                            sendNotificationToServer(response.body());
                        }

                        @Override
                        public void onFailure(Call<OrderResult> call, Throwable t) {
                            Toast.makeText(CartActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }
    }


    private void sendNotificationToServer(final OrderResult orderResult) {
        //Get Server to Token


        mService.getToken("server_app_01", "1")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {

                        Map<String, String> contentSend = new HashMap<>();
                        contentSend.put("title", "Drink Shop");
                        contentSend.put("message", "You have new order: #" + orderResult.getOrderId());
                        DataMessage dataMessage = new DataMessage();
                        if (response.body().getToken() != null) {
                            dataMessage.setTo(response.body().getToken());
                            dataMessage.setData(contentSend);

                            String test = new Gson().toJson(dataMessage);
                            Log.d("dat1234",test);
                            IFCMService ifcmService = Common.getFCMService();
                            ifcmService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {

                                                    Toast.makeText(CartActivity.this, "Thank you, Order Place", Toast.LENGTH_SHORT).show();

                                                    Common.cartRespository.emptyCart();
                                                    finish();
                                                } else {
                                                    Toast.makeText(CartActivity.this, "Send notification failed! ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNone = nonce.getNonce();
                if (Common.cartRespository.sumPrice() > 0) {
                    amount = String.valueOf(Common.cartRespository.sumPrice());
                    params = new HashMap<>();
                    params.put("amount", amount);
                    params.put("nonce", strNone);

                    sendPayment();
                } else {
                    Toast.makeText(this, "Payment amount is 0", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cacelled", Toast.LENGTH_SHORT).show();
            } else {

                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("ERROR", error.getMessage().toString());
            }
        }
    }

    private void sendPayment() {

        mService.payment(params.get("nonce"), params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().toString().contains("Successful")) {
                            Toast.makeText(CartActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(CartActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }

                        Log.e("INFO", response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("INFO", t.getMessage());
                    }
                });
    }

    private void loadToken() {

        final SpotsDialog alertDialog = new SpotsDialog(CartActivity.this);
        alertDialog.show();
        alertDialog.setMessage("Đang xử lý ...");


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                alertDialog.dismiss();
                btn_place_order.setEnabled(false);
                Toast.makeText(CartActivity.this, throwable.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                alertDialog.dismiss();
                token = responseString;
                btn_place_order.setEnabled(true);
            }
        });
    }
}
