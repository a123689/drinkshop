package com.example.dat.drinkshopapp.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dat.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;


    @Dao
    public interface CartDAO {
        @Query("SELECT * FROM Cart")
        Flowable<List<Cart>> getCartItems();

        @Query("SELECT * FROM Cart WHERE id=:cartItemId")
        Flowable<List<Cart>> getCartItemById(int cartItemId);

        @Query("SELECT COUNT(*) FROM Cart")
        int countCartItems();

        @Query("SELECT SUM(Price) FROM Cart")
        float sumPrice();

        @Query("DELETE FROM Cart")
        void emptyCart();

        @Insert
        void insertToCart(Cart... carts);

        @Update
        void updateCart(Cart... carts);

        @Delete
        void deleteCartItem(Cart Cart);

    }

