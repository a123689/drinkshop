package com.example.dat.drinkshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView tvRegister;
    EditText edUsername,edEmail,edPassword;
    Button btnRigister;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        auth = FirebaseAuth.getInstance();
    }

    private void findView(){
        edUsername = findViewById(R.id.username);
        edPassword = findViewById(R.id.password);
        edEmail = findViewById(R.id.email);
        toolbar = findViewById(R.id.toolbarlayout);
        tvRegister = findViewById(R.id.tvRegister);
        btnRigister = findViewById(R.id.btn_register);
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnRigister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:{
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                String email = edEmail.getText().toString();

                if(username.isEmpty()){
                    edUsername.setError("You must enter username");
                }else if(email.isEmpty()){
                    edEmail.setError("You must enter email");
                }else if(password.isEmpty()){
                    edPassword.setError("You must enter password");
                }else {

                    String EMAIL_PATTERN =
                            "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
                    if(!Pattern.matches(EMAIL_PATTERN,email)){
                        edEmail.setError("The email is not valid");
                    }else {

                        if(password.length() < 6 ){
                            edPassword.setError("Password must be at least 6 characters");
                        }else {
                            register(username,email,password);
                        }

                    }
                }
                break;
            }
        }
    }

    private void register(final String username, String email, String password){
        final SpotsDialog alertDialog = new SpotsDialog(this);
        alertDialog.show();
        alertDialog.setMessage("Loading...");
        alertDialog.setCanceledOnTouchOutside(false);
        if(!alertDialog.isShowing()){
            alertDialog.show();
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        alertDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}