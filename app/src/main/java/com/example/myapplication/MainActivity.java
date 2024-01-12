package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        btnLogin.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        });

        btnRegister.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

        });


    }


    public void initComponents() {
        btnLogin=findViewById(R.id.btnLoginPage);
        btnRegister=findViewById(R.id.btnRegisterNow);
    }




}