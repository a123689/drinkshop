package com.example.dat.drinkshopapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.dat.drinkshopapp.Adapter.CategoryAdapter;
import com.example.dat.drinkshopapp.Database.DataSource.CartRepository;
import com.example.dat.drinkshopapp.Database.DataSource.FavoriteRespository;
import com.example.dat.drinkshopapp.Database.Local.CarDataSource;
import com.example.dat.drinkshopapp.Database.Local.DRoomDatabase;
import com.example.dat.drinkshopapp.Database.Local.FavoriteDataSource;
import com.example.dat.drinkshopapp.Model.Banner;
import com.example.dat.drinkshopapp.Model.Category;
import com.example.dat.drinkshopapp.Model.Drink;
import com.example.dat.drinkshopapp.Model.User;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Utils.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nex3z.notificationbadge.NotificationBadge;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tv_name,tv_phone;
    DinkShopAPI mService;
    SliderLayout sliderLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView lst_menu;
    NotificationBadge badge;
    ImageView  cart_icon;
    CircleImageView circleImageView;
    int  PICK_FILE_REQUEST =1222;
    Uri selectedFileUri;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findView();
        loadAPI();
        getBannerImage();
        getMenu();
        getToppingList();
        initDB();
        updateCartCount();
        updateTokenToFirebase("+84365789306");
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()){

                }else {
                    Toast.makeText(HomeActivity.this, "You must accept all permisstion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                /* ... */}
        }).check();
    }

    private void findView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitleTextColor(Color.WHITE);
        sliderLayout = findViewById(R.id.slider);
        FloatingActionButton fab = findViewById(R.id.fab);
        lst_menu = findViewById(R.id.lst_menu);
        lst_menu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mService = Common.getAPI();
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser!= null){
                    startActivity(new Intent(HomeActivity.this,ChatActivity.class));
                }else {
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View headview = navigationView.getHeaderView(0);
        tv_name = headview.findViewById(R.id.tv_name_header);
        tv_phone = headview.findViewById(R.id.tv_phone_header);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                getMenu();
                getToppingList();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getBannerImage();
                getMenu();

                getToppingList();
            }
        });


    }


    private void updateTokenToFirebase(String phone) {

        DinkShopAPI mService = Common.getAPI();
        mService.updatetoken(phone,
                FirebaseInstanceId.getInstance().getToken(),"0")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("DEBUG", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("LOI", t.getMessage().toString());

                    }
                });
    }


    private void loadAPI(){
        String phone = "+84365789306";

        mService.getUser(phone).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               Common.curenuser = response.body();
                if(Common.curenuser != null){

                    tv_name.setText(Common.curenuser.getName());
                    tv_phone.setText(Common.curenuser.getPhone());
                }

             //   Log.d("sss",Common.curenuser.getError_msg());


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d("ddd",t.getMessage());
            }
        });
    }

    boolean isBackButtonClicked = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = view.findViewById(R.id.badge);
        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    @Override
    protected void onResume() {
        updateCartCount();
        super.onResume();
        isBackButtonClicked =false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart_menu) {
            return true;
        }else if(id == R.id.search_menu){
            startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you want to exit application?");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    // change this code beacuse your app will crash
                    startActivity(new Intent(HomeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });
            builder.setPositiveButton("CANCEl", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();
        }else if(id == R.id.nav_favorite){
            if(Common.curenuser.getPhone() != null){
                startActivity(new Intent(HomeActivity.this,FavoriteActivity.class));
            }else {
                Toast.makeText(this, "Please logging again", Toast.LENGTH_SHORT).show();
            }

        }else if(id == R.id.nav_show_orders){
            startActivity(new Intent(HomeActivity.this,ShowOderActivity.class));
        }else if(id == R.id.nav_nearby_store){
            startActivity(new Intent(HomeActivity.this,NearbyStore.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getBannerImage() {
        compositeDisposable.add(mService.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
                }));
    }

    private void displayImage(List<Banner> banners) {

        HashMap<String, String> bannerMap = new HashMap<>();
        for (Banner items : banners){

            bannerMap.put(items.getName(), items.getLink());
        }


        for (String name : bannerMap.keySet()) {

            TextSliderView textSliderView = new TextSliderView(HomeActivity.this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);

        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
                }));
    }

    private void displayMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        lst_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.TOPPING_MENU_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {

                        Common.toppinglist = drinks;
                    }
                }));
    }

    private void initDB() {
        Common.cartDatabase = DRoomDatabase.getInstance(this);
        Common.cartRespository = CartRepository.getInstance(CarDataSource.getInstance(Common.cartDatabase.cartDAO()));
        Common.favoriteRespository = FavoriteRespository.getInstance(FavoriteDataSource.getInstance(Common.cartDatabase.favoriteDAO()));

    }



    private void updateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRespository.countCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRespository.countCartItems()));
                }
            }
        });

    }





}
