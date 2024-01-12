package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.ReviewDto;
import com.example.myapplication.request.CreateCartItem;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.UserDetail;
import com.example.myapplication.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImageCover;
    private TextView name,price,quantity,description;
    private RatingBar rating,bottomSheetRatingBar;
    private Button btnAddCart,btnOpenRating,btnRating,btnLoadReview;
    private TextInputEditText editTextBottomSheet;
    private Product product;
    private LinearLayout bottomSheetRating;
    private BottomSheetBehavior bottomSheetBehavior;
    private Long productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initComponents();
        Intent intent=getIntent();
        productId=intent.getLongExtra("productId",0);
        loadProduct();
        btnOpenRating.setOnClickListener(v->{

            if(bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                getReview();

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btnAddCart.setOnClickListener(v -> {
            CreateCartItem createCartItem=new CreateCartItem(productId, UserDetail.id,1);
            addToCart(createCartItem);
        });

        btnLoadReview.setOnClickListener(v -> {
            Intent newIntent=new Intent(ProductDetailActivity.this, ReviewActivity.class);
            newIntent.putExtra("productId",productId);
            startActivity(newIntent);
        });

        btnRating.setOnClickListener(v->{
            ReviewDto reviewDto=new ReviewDto();
            reviewDto.setProductId(productId);
            reviewDto.setUserId(UserDetail.id);
            reviewDto.setComment(editTextBottomSheet.getText().toString());
            Log.d("rating","s:"+bottomSheetRatingBar.getRating());
            reviewDto.setRating((int) bottomSheetRatingBar.getRating());
            ApiClientInstance.getInstance(this).addReview(reviewDto).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if(response.isSuccessful()){
                        Message.showToast(ProductDetailActivity.this,response.body().getMessage());
                        loadProduct();
                    }else {
                        Utils.handlerErrorResponse(response,ProductDetailActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        });

    }

    private void getReview(){
        ApiClientInstance.getInstance(this).getReview(UserDetail.id,productId).enqueue(new Callback<ReviewDto>() {
            @Override
            public void onResponse(Call<ReviewDto> call, Response<ReviewDto> response) {
                if(response.isSuccessful()){
                    ReviewDto reviewDto=response.body();
                    bottomSheetRatingBar.setRating(reviewDto.getRating());
                    editTextBottomSheet.setText(reviewDto.getComment());
                }else {
                    Utils.handlerErrorResponse(response,ProductDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ReviewDto> call, Throwable t) {

            }
        });

    }


    private void loadProduct(){
        ApiClientInstance.getInstance(this).getProductsById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if(response.isSuccessful()){
                    product=response.body();
                    name.setText(product.getName());
                    price.setText("Price: $"+product.getPrice());
                    quantity.setText("Quantity: "+product.getQuantity());
                    description.setText("Description:\n"+product.getDescription());
                    String imageUrl = product.getImageCover();
                    Picasso.get().load(imageUrl).into(productImageCover);
                    if(product.getReviewCount()==null){
                        rating.setRating(0);
                    }else {
                        float r= (float) (product.getReviewTotal()/product.getReviewCount());
                        Log.d("rating","product:"+r);
                        rating.setRating(r);

                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });

    }



    private void addToCart(CreateCartItem createCartItem) {

        ApiClientInstance.getInstance(this).addToCart(createCartItem).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    BaseResponse res=response.body();
                    Message.showToast(ProductDetailActivity.this,res.getMessage());
                }else {
                    Utils.handlerErrorResponse(response,ProductDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Message.showToast(ProductDetailActivity.this,t.getMessage());

            }
        });
    }



    public void initComponents() {
        name=findViewById(R.id.productName);
        price=findViewById(R.id.productPrice);
        quantity=findViewById(R.id.productQuantity);
        description=findViewById(R.id.productDescription);
        rating=findViewById(R.id.productRating);
        productImageCover=findViewById(R.id.productImage);
        btnAddCart=findViewById(R.id.btnAddToCart);
        btnOpenRating=findViewById(R.id.btnOpenRating);
        bottomSheetRating=findViewById(R.id.bottomSheetRating);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheetRating);
        editTextBottomSheet=findViewById(R.id.bottomSheetReviewEditText);
        btnRating=findViewById(R.id.bottomSheetSubmitButton);
        bottomSheetRatingBar=findViewById(R.id.bottomSheetRatingBar);
        btnLoadReview=findViewById(R.id.btnLoadReview);
    }
}
