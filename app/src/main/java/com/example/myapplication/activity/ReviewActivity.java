package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ReviewAdapter;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.response.ReviewResponse;
import com.example.myapplication.util.SharedPreferencesManager;
import com.example.myapplication.util.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView recyclerViewReview;
    private ProgressBar progressBar;
    private TextView message;
    private Long productId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent=getIntent();
        productId=intent.getLongExtra("productId",0);
        initComponents();
        loadReview();
    }

    private void loadReview() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientInstance.getInstance(this).getAllReviews(productId).enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    List<ReviewResponse> reviewResponses=response.body();
                    if(reviewResponses.isEmpty()){
                        message.setVisibility(View.VISIBLE);
                    }else {
                        message.setVisibility(View.GONE);
                        ReviewAdapter reviewAdapter=new ReviewAdapter(reviewResponses);
                        recyclerViewReview.setAdapter(reviewAdapter);
                    }

                }else {
                    Utils.handlerErrorResponse(response,ReviewActivity.this);
                }

            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {

            }
        });
    }

    private void initComponents(){
        recyclerViewReview=findViewById(R.id.recyclerViewReview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerViewReview.setLayoutManager(linearLayoutManager);
        progressBar=findViewById(R.id.progressBarLoadReview);
        message=findViewById(R.id.textViewNoComment);
    }
}
