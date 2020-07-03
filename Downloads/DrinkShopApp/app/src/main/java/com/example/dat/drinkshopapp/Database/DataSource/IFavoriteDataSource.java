package com.example.dat.drinkshopapp.Database.DataSource;

import com.example.dat.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {
    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);

    void insertFav(Favorite... favorites);

    void delete(Favorite favorite);
}
