package com.example.dat.drinkshopapp.Database.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dat.drinkshopapp.Database.ModelDB.Cart;
import com.example.dat.drinkshopapp.Database.ModelDB.Favorite;

@Database(entities = {Cart.class,Favorite.class},version = 1,exportSchema = false)
public abstract class DRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();
    private static DRoomDatabase instance;
    public static DRoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, DRoomDatabase.class,"DAT_Drink")
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }


}
