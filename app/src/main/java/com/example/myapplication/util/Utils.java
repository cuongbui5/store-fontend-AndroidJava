package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.response.BaseResponse;

import java.io.IOException;
import java.util.logging.Handler;

import retrofit2.Response;

public class Utils {
    public static void handlerErrorResponse(Response response, Context context){
        try {
            BaseResponse errorResponse= GsonObject.instance().fromJson(response.errorBody().string(),BaseResponse.class);
            if (errorResponse != null && "fail".equals(errorResponse.getStatus())) {
                Message.showToast(context,errorResponse.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
