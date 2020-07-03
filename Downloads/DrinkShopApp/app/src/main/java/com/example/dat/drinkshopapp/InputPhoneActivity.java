package com.example.dat.drinkshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class InputPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnnext;
    EditText edsodienthoai;
    TextView tvthongbaosodienthoai;
    String phone;

    int REQUEST_CODE =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone);
        findView();

    }

    private void findView(){
        btnnext = findViewById(R.id.btnnext);
        edsodienthoai = findViewById(R.id.edsodienthoai);
        tvthongbaosodienthoai = findViewById(R.id.tvthongbaonhapsodienthoai);
        btnnext.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnnext:{
                phone = edsodienthoai.getText().toString();
                if(phone.equals("") || phone.equals(null)){
                    tvthongbaosodienthoai.setText("Bạn chưa nhập số điện thoại");
                    tvthongbaosodienthoai.setVisibility(View.VISIBLE);
                }else {
                    if(phone.length() == 10 || phone.length() == 11){

                        SpotsDialog alertDialog = new SpotsDialog(InputPhoneActivity.this);
                        alertDialog.show();
                        alertDialog.setMessage("Đang xử lý ...");
                        Intent intent = new Intent(InputPhoneActivity.this,OutPutPhoneActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);

                    }else {
                        tvthongbaosodienthoai.setText("Số điện thoại không hợp lệ");
                        tvthongbaosodienthoai.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }
        }
    }
}
