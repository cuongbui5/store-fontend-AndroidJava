package com.example.myapplication.api;

import com.example.myapplication.model.AddressDto;
import com.example.myapplication.model.CartItemDto;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.ReviewDto;
import com.example.myapplication.request.CreateCartItem;
import com.example.myapplication.request.CreateOrderRequest;
import com.example.myapplication.request.LoginRequest;
import com.example.myapplication.request.RefreshTokenRequest;
import com.example.myapplication.request.RegisterRequest;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.CartResponse;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.response.ResponseCategoryData;
import com.example.myapplication.response.ResponseProductData;
import com.example.myapplication.response.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {
    @POST("auth/register")
    Call<BaseResponse> register(@Body RegisterRequest registerRequest);
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/token")
    Call<LoginResponse> refresh(@Body RefreshTokenRequest refreshTokenRequest);
    @GET("category")
    Call<ResponseCategoryData> getCategories();
    @GET("products")
    Call<ResponseProductData> getProducts(@Query("page")int page, @Query("size")int size);
    @GET("products/{id}")
    Call<Product> getProductsById(@Path("id")Long id);
    @GET("products")
    Call<ResponseProductData> getProductsBySort(@Query("page")int page, @Query("size")int size,@Query("sort") String sort);
    @GET("products")
    Call<ResponseProductData> getProductsBySortAndName(@Query("page")int page, @Query("size")int size,@Query("sort") String sort,@Query("name") String name);
    @GET("products")
    Call<ResponseProductData> getProductsBySortAndCategory(@Query("page")int page, @Query("size")int size,@Query("sort") String sort,@Query("categoryId") Long categoryId);
    @GET("products")
    Call<ResponseProductData> getProductsBySortAndNameAndCategory(@Query("page")int page, @Query("size")int size,@Query("sort") String sort,@Query("name") String name,@Query("categoryId") Long categoryId);
    @GET("products")
    Call<ResponseProductData> getProductByName(@Query("page")int page, @Query("size")int size, @Query("name") String name);
    @GET("products")
    Call<ResponseProductData> getProductByCategory(@Query("page")int page, @Query("size")int size, @Query("categoryId") Long categoryId);
    @GET("products")
    Call<ResponseProductData> getProductByCategoryAnName(@Query("page")int page, @Query("size")int size, @Query("categoryId") Long categoryId, @Query("name") String name);
    @POST("cartItem/addToCart")
    Call<BaseResponse> addToCart(@Body CreateCartItem createCartItem);
    @GET("cartItem/{userId}")
    Call<CartResponse> getCartItemByUserId(@Path("userId") Long userId);
    @PUT("cartItem/edit")
    Call<BaseResponse> updateCartItem(@Body CartItemDto cartItemDto);

    @DELETE("cartItem/remove/{id}")
    Call<BaseResponse> deleteCartItem(@Path("id") Long id);
    @GET("address/{userId}")
    Call<List<AddressDto>> getAllAddress(@Path("userId") Long userId);

    @POST("address/{userId}")
    Call<AddressDto> addNewAddress(@Path("userId") Long userId,@Body AddressDto addressDto);
    @POST("address/{id}/{userId}")
    Call<BaseResponse> setDefault(@Path("id") Long id,@Path("userId") Long userId);
    @DELETE("address/{id}")
    Call<BaseResponse> deleteAddress(@Path("id") Long id);
    @PUT("address")
    Call<BaseResponse> updateAddress(@Body AddressDto addressDto);

    @POST("order/payment")
    Call<BaseResponse> payment(@Body CreateOrderRequest createOrderRequest);

    @POST("review/add-review")
    Call<BaseResponse> addReview(@Body ReviewDto reviewDto);

    @GET("review/get/{userId}/{productId}")
    Call<ReviewDto> getReview(@Path("userId")Long userId,@Path("productId")Long productId);

    @GET("review/getAll/{productId}")
    Call<List<ReviewResponse>> getAllReviews(@Path("productId")Long productId);






}






