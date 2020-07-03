package com.example.dat.drinkshopapp.Retrofit;

import com.example.dat.drinkshopapp.Model.Banner;
import com.example.dat.drinkshopapp.Model.Category;
import com.example.dat.drinkshopapp.Model.CheckUserRespone;
import com.example.dat.drinkshopapp.Model.Drink;
import com.example.dat.drinkshopapp.Model.Oder;
import com.example.dat.drinkshopapp.Model.OrderResult;
import com.example.dat.drinkshopapp.Model.Token;
import com.example.dat.drinkshopapp.Model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DinkShopAPI {

    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserRespone> checkUserExists(@Field("phone") String phone);
    @FormUrlEncoded
    @POST("registerNewUser.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);
    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUser(@Field("phone") String phone);

    @GET("getBanner.php")
    Observable<List<Banner>> getBanners();

    @GET("getMenu.php")
    Observable<List<Category>> getMenu();

    @FormUrlEncoded
    @POST("getDrink.php")
    Observable<List<Drink>> getDrink(@Field("menuid") String menuID);

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part("phone") MultipartBody.Part phone, @Part MultipartBody.Part file);

    @GET("getAllDrink.php")
    Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("getOderSubmit.php")
    Call<OrderResult> submitOrder(@Field("price") float orderPrice,
                                  @Field("orderDetail") String orderDetail,
                                  @Field("comment") String comment,
                                  @Field("address") String address,
                                  @Field("phone") String phone
    );


    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<String> payment(@Field("nonce") String nonce,
                         @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("getOder.php")
    Observable<List<Oder>> getOrder(
            @Field("status") String status,
            @Field("userPhone") String userPhone
    );

    @FormUrlEncoded
    @POST("cancelorder.php")
    Call<String> cancelOrder(@Field("orderId") String orderId,
                             @Field("userPhone") String userPhone
    );

    @FormUrlEncoded
    @POST("Sever/updatetoken.php")
    Call<String> updatetoken(@Field("phone") String phone,
                             @Field("token") String token,
                              @Field("isServerToken") String isServerToken
    );

    @FormUrlEncoded
    @POST("getToken.php")
    Call<Token> getToken(@Field("phone") String phone,
                         @Field("isServerToken") String isServerToken
    );
}
