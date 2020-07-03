package com.example.dat.drinkshopapp.Utils;

import com.example.dat.drinkshopapp.Database.DataSource.CartRepository;
import com.example.dat.drinkshopapp.Database.DataSource.FavoriteRespository;
import com.example.dat.drinkshopapp.Database.Local.DRoomDatabase;
import com.example.dat.drinkshopapp.Model.Category;
import com.example.dat.drinkshopapp.Model.Drink;
import com.example.dat.drinkshopapp.Model.Oder;
import com.example.dat.drinkshopapp.Model.User;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Retrofit.FCMClient;
import com.example.dat.drinkshopapp.Retrofit.IFCMService;
import com.example.dat.drinkshopapp.Retrofit.RetrofitClient;
import com.example.dat.drinkshopapp.Retrofit.RetrofitScalarsClient;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static  final String BASE_URL = "https://phandat123689.000webhostapp.com/";
    public static  final String API_TOKEN_URL = "http://192.168.64.2/drinkshop/braintree/main.php";
    public static final String TOPPING_MENU_ID = "7";
    public static User curenuser = null;
    public static Category category =null;
    public static Oder currentOrder = null;
    public static final String DRINK_REF = "drinkshop";
    public static final String CHAT_REF = "Chat";
    public static List<Drink> toppinglist = new ArrayList<>();
    public static double toppingPrice = 0.0;
    public static List<String> toppingAdded = new ArrayList<>();

    public static int sizeOfCup = -1;
    public static int sugar = -1; //
    public static int ice = -1;

    public static DRoomDatabase cartDatabase;
    public static CartRepository cartRespository;
    public static FavoriteRespository favoriteRespository;
    public static int dem = 0;
    public static    StringBuilder topping_final_commment = new StringBuilder("");
   // public static FavoriteRespository favoriteRespository;

    public static DinkShopAPI getAPI(){
        return RetrofitClient.getclient(BASE_URL).create(DinkShopAPI.class);
    }


    public static  DinkShopAPI getScalarsAPI() {

        return RetrofitScalarsClient.getScalarsClient(BASE_URL).create( DinkShopAPI.class);
    }

    public static  final String FCM_URl="https://fcm.googleapis.com/";


    public static IFCMService getFCMService(){
        return FCMClient.getClient(FCM_URl).create(IFCMService.class);
    }
    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Placed";
            case 1:
                return "Processing";
            case 2:
                return "Shipping";
            case 3:
                return "Shipped";
            case -1:
                return "Cacelled";
            default:
                return "Order Error";
        }
    }

}
