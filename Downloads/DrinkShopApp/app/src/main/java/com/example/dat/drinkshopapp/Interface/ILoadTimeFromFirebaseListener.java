package com.example.dat.drinkshopapp.Interface;

import com.example.dat.drinkshopapp.Model.Oder;

public interface ILoadTimeFromFirebaseListener {
    void onLoadOnlyTimeTimeSuccess(long time);
    void onLoadTimeFailed(String message);
}
