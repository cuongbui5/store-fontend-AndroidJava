package com.example.myapplication.api.interceptor;



import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.MainActivity;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.api.RetrofitAuth;
import com.example.myapplication.request.RefreshTokenRequest;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.SharedPreferencesManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TokenInterceptor implements Interceptor {

    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;





    public TokenInterceptor(Context context) {
        this.context=context;
        sharedPreferencesManager=new SharedPreferencesManager(context);


    }

    private Response refreshToken(Response response, Request request, Chain chain, LoginResponse loginResponse) throws IOException {
        if(loginResponse==null||loginResponse.getRefreshToken().isEmpty()){
            logout();
            return response;
        }
        response.close();
        Log.d("token","refresh token ...");
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(loginResponse.getRefreshToken());
        ApiClient apiClient= RetrofitAuth.getInstance().getRetrofit().create(ApiClient.class);
        Call<LoginResponse> call=apiClient.refresh(refreshTokenRequest);
        retrofit2.Response<LoginResponse> res=call.execute();
        if(res.isSuccessful()){
            LoginResponse newLogin = res.body();
            Log.d("token","refresh token ok!:"+newLogin.getAccessToken());
            sharedPreferencesManager.saveUserData(newLogin);
            Request newRequest = request.newBuilder()
                    .header("Authorization", "Bearer " + newLogin.getAccessToken())
                    .build();

            return chain.proceed(newRequest);
        }else {
            Log.d("token","refresh token het han.");
            logout();
            return response;
        }



    }

    public void logout(){
        sharedPreferencesManager.clearData();
        Intent intent=new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }




    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LoginResponse loginResponse=sharedPreferencesManager.getUserData();
        if(loginResponse==null){
            logout();
        }else {
            String token = loginResponse.getAccessToken();
            Log.d("request",request.toString());
            if (!token.isEmpty()) {
                Request newRequest = request.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();

                Response response = chain.proceed(newRequest);
                if (response.code() == 401) {

                    Log.d("token",request.toString());
                    Log.d("token","access token het han");

                    return refreshToken(response,request,chain,loginResponse);
                } else {
                    return response;
                }
            }

        }
        return chain.proceed(request);


    }




}
