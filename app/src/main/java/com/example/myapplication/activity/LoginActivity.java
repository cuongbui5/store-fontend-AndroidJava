package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.api.RetrofitAuth;
import com.example.myapplication.request.RefreshTokenRequest;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.request.LoginRequest;
import com.example.myapplication.util.SharedPreferencesManager;
import com.example.myapplication.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
    private Button btnRegisterNow;
    private Button btnLogin;
    private EditText username;
    private EditText password;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        btnRegisterNow.setOnClickListener(v ->{
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v->{
            LoginRequest loginRequest=new LoginRequest(username.getText().toString(),password.getText().toString());
            login(loginRequest);

        });


    }

    public void login(LoginRequest loginRequest){

        ApiClient apiClient= RetrofitAuth.getInstance().getRetrofit().create(ApiClient.class);
        apiClient.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        LoginResponse loginResponse=response.body();
                        if(response.isSuccessful()&&loginResponse!=null){
                            SharedPreferencesManager sharedPreferencesManager=new SharedPreferencesManager(LoginActivity.this);
                            sharedPreferencesManager.saveUserData(loginResponse);
                            Log.d("token","login ok:"+loginResponse.getAccessToken());
                            Intent intent=new Intent(LoginActivity.this, AppActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                                Utils.handlerErrorResponse(response,LoginActivity.this);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

                    }
                });


    }


    public void initComponents() {
        btnRegisterNow=findViewById(R.id.btnRegisterNow);
        btnLogin=findViewById(R.id.btnLogin);
        username=findViewById(R.id.editUsernameLogin);
        password=findViewById(R.id.editPasswordLogin);
    }




}
