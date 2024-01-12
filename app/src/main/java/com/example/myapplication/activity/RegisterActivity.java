package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.api.RetrofitAuth;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.request.RegisterRequest;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{
    private Button btnLoginNow;
    private Button btnRegister;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        btnLoginNow.setOnClickListener(v->{
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v->{
            if(!password.getText().toString().trim().equals(passwordConfirm.getText().toString().trim())){
                Message.showToast(RegisterActivity.this,"Mật khẩu không khớp!");

            }else {
                RegisterRequest registerRequest=new RegisterRequest(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        passwordConfirm.getText().toString()

                );

                register(registerRequest);
            }
        });
    }
    public void register(RegisterRequest registerRequest){
        ApiClient apiClient= RetrofitAuth.getInstance().getRetrofit().create(ApiClient.class);
        apiClient.register(registerRequest).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    Message.showToast(RegisterActivity.this,response.body().getMessage());

                }else {
                    Utils.handlerErrorResponse(response,RegisterActivity.this);

                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                Message.showToast(RegisterActivity.this,"Error server!");
            }
        });

    }


    public void initComponents() {
        btnLoginNow=findViewById(R.id.btnLoginNow);
        btnRegister=findViewById(R.id.btnRegister);
        username=findViewById(R.id.editUsername);
        email=findViewById(R.id.editEmail);
        password=findViewById(R.id.editPassword);
        passwordConfirm=findViewById(R.id.editPasswordConfirm);
    }


}
