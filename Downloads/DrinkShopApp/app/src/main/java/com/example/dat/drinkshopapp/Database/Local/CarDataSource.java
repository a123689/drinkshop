package com.example.dat.drinkshopapp.Database.Local;

import com.example.dat.drinkshopapp.Database.DataSource.ICartDataSoure;
import com.example.dat.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CarDataSource implements ICartDataSoure {

    private CartDAO cartDAO;
    private static CarDataSource instance;

    public CarDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CarDataSource getInstance(CartDAO cartDAO) {
        if (instance == null)
            instance = new CarDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public float sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();

    }

    @Override
    public void insertToCart(Cart... carts) {

        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);

    }

    @Override
    public void deleteCartItem(Cart Cart) {
        cartDAO.deleteCartItem(Cart);

    }
}
