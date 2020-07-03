package com.example.dat.drinkshopapp.Database.DataSource;

import com.example.dat.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSoure  {
    Flowable<List<Cart>> getCartItems();

    Flowable<List<Cart>> getCartItemById(int cartItemId);

    int countCartItems();
    float sumPrice();
    void emptyCart();

    void insertToCart(Cart... carts);

    void updateCart(Cart... carts);

    void deleteCartItem(Cart Cart);

}
