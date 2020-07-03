package com.example.dat.drinkshopapp.Database.DataSource;

import com.example.dat.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRespository implements IFavoriteDataSource {
    private IFavoriteDataSource favoriteDataSource;

    public FavoriteRespository(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    private static FavoriteRespository instance;

    public static FavoriteRespository getInstance(IFavoriteDataSource favoriteDataSource) {
        if (instance == null)
            instance = new FavoriteRespository(favoriteDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDataSource.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDataSource.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDataSource.delete(favorite);
    }
}
