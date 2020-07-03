package com.example.dat.drinkshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dat.drinkshopapp.Model.CheckUserRespone;
import com.example.dat.drinkshopapp.Model.User;
import com.example.dat.drinkshopapp.Retrofit.DinkShopAPI;
import com.example.dat.drinkshopapp.Utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonArray;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutPutPhoneActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    CodeInputView codeInputView;
    Handler handler = new Handler();
    String codeSent;
    TextView tvsodienthoaixascthuc;
    MaterialEditText edname,edaddress,edbirthdate;
    Button btnregister;
    DinkShopAPI mService;
    StringBuilder stringBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_put_phone);
        anhxa();

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        stringBuilder = new StringBuilder(phone);
        if (stringBuilder.charAt(0) == '0') {
            stringBuilder.deleteCharAt(0);
        }
        final String sodienthoai = "+84"+stringBuilder;

        sendVerificationCode("+84" + stringBuilder.toString());


        tvsodienthoaixascthuc.setText(sodienthoai);
        codeInputView.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(final String code) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verifySignInCode(code);

                    }
                }, 1000);
            }
        });


    }

    private void anhxa(){
        mAuth = FirebaseAuth.getInstance();
        codeInputView = findViewById(R.id.codeinputview);
        tvsodienthoaixascthuc = findViewById(R.id.tvsodienthoaixacthuc);
        mService = Common.getAPI();

    }
    private void verifySignInCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode(String phone){




        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

               codeSent = s;
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


//                            mService.checkUserExists("+84"+stringBuilder.toString()).enqueue(new Callback<CheckUserRespone>() {
//                                @Override
//                                public void onResponse(Call<CheckUserRespone> call, Response<CheckUserRespone> response) {
//                                    CheckUserRespone checkUserRespone = response.body();
//                                    if(checkUserRespone.isExsits()){
//                                        alertDialog.dismiss();
//
//                                    }else {
//
//                                        alertDialog.dismiss();
//                                        dialogregister();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<CheckUserRespone> call, Throwable t) {
//
//                                }
//                            });

                            //volley("+84"+stringBuilder.toString());

                            updateTokenToFirebase("+84"+stringBuilder.toString());
                            // updateTokenToServer();
                            Intent intent = new Intent(OutPutPhoneActivity.this,HomeActivity.class);
                            intent.putExtra("phone","+84"+stringBuilder.toString());
                            startActivity(intent);

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                codeInputView.setEditable(true);
                                codeInputView.setError("Mã xác thực không chính xác");
                            }
                        }
                    }
                });
    }

    private void dialogregister(){
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(OutPutPhoneActivity.this);
        alertDialog2.setTitle("Đăng kí");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.register_dialog,null);
        edname = view.findViewById(R.id.ed_name);
        edaddress = view.findViewById(R.id.ed_address);
        edbirthdate  =view.findViewById(R.id.ed_birthdate);
        btnregister = view.findViewById(R.id.btn_continueoutput);

        edbirthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.create().dismiss();

                if(TextUtils.isEmpty(edname.getText().toString())){

                    Toast.makeText(OutPutPhoneActivity.this, "Bạn chưa nhập tài khoản", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edaddress.getText().toString())){

                    Toast.makeText(OutPutPhoneActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edbirthdate.getText().toString())){

                    Toast.makeText(OutPutPhoneActivity.this, "Bạn chưa nhập ngày sinh", Toast.LENGTH_SHORT).show();
                    return;
                }

                mService.registerNewUser("+84"+stringBuilder.toString(),edname.getText().toString(),edbirthdate.getText().toString(),edaddress.getText().toString()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User user = response.body();
                        if(TextUtils.isEmpty(user.getError_msg())){
                            Common.curenuser = response.body();
                            //updateTokenToFirebase();
                            updateTokenToServer(Common.curenuser.getPhone());
                            Toast.makeText(OutPutPhoneActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OutPutPhoneActivity.this,HomeActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("sss",t.toString());
                    }
                });
            }
        });
        alertDialog2.setView(view);
        alertDialog2.show();

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

    private void updateTokenToServer(final String phone) {

        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        DinkShopAPI  mService = Common.getAPI();
                        mService.updatetoken(phone,
                                instanceIdResult.getToken(), "0")
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Log.d("DEBUG", response.body());
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("DEBUG", t.getMessage().toString());

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OutPutPhoneActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void volley (final String phone){

       final SpotsDialog alertDialog = new SpotsDialog(OutPutPhoneActivity.this);
                            alertDialog.show();
                            alertDialog.setMessage("Đang xử lý ...");
        String url ="http://phandat123689.000webhostapp.com/checkuser.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             if(response.contains("true")){
                 alertDialog.dismiss();
                 updateTokenToFirebase(phone);
                // updateTokenToServer();
                 Intent intent = new Intent(OutPutPhoneActivity.this,HomeActivity.class);
                 intent.putExtra("phone",phone);
                 startActivity(intent);
             }else{
                 alertDialog.dismiss();
                 dialogregister();
             }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dat123","loi");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  map = new HashMap<>();
                map.put("phone",phone);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


}
