package com.example.myapplication.activity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartItemAdapter;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.listener.CartItemClickListener;
import com.example.myapplication.model.CartItemDto;
import com.example.myapplication.model.enums.OrderStatus;
import com.example.myapplication.request.CreateOrderRequest;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.CartResponse;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.UserDetail;
import com.example.myapplication.util.Utils;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartItemClickListener,FragmentScreen {
    private RecyclerView recyclerViewCart;
    private TextView message,totalPrice;
    private ProgressBar progressBar;
    private Button btnPay;
    private double payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart,container,false);
        initComponents(view);
        loadCartItem();

        btnPay.setOnClickListener(v -> {
            CreateOrderRequest createOrderRequest=new CreateOrderRequest();
            createOrderRequest.setUserId(UserDetail.id);
            createOrderRequest.setStatus(OrderStatus.PENDING);
            createOrderRequest.setShippingCost(10.0);
            createOrderRequest.setTotal(payment);
            ApiClientInstance.getInstance(getContext()).payment(createOrderRequest).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        Message.showToast(getContext(),response.body().getMessage());
                        loadCartItem();
                    }else {
                        Utils.handlerErrorResponse(response,getContext());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {

                }
            });
        });
        return view;
    }



    public void showCart(List<CartItemDto> cartItemDtos){
        if(cartItemDtos.isEmpty()){
            message.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
        }else {
            message.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            CartItemAdapter adapter = new CartItemAdapter(cartItemDtos, this);
            recyclerViewCart.setAdapter(adapter);
        }

    }

    public void loadCartItem() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientInstance.getInstance(getContext()).getCartItemByUserId(UserDetail.id).enqueue(new Callback<CartResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                        if(response.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            CartResponse cartResponse=response.body();
                            if(cartResponse!=null){
                                showCart(cartResponse.getCartItems());
                                if(cartResponse.getTotal()>0){
                                    payment=cartResponse.getTotal()+10;
                                }else {
                                    payment=0;
                                }

                                totalPrice.setText("Payment: $"+payment);
                            }
                            
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                        Log.d("Cart Fragment", Objects.requireNonNull(t.getMessage()));
                    }
                });



    }


    public void updateCartItem(CartItemDto cartItemDto) {

        ApiClientInstance.getInstance(getContext()).updateCartItem(cartItemDto).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                
                if(response.isSuccessful()){
                    BaseResponse baseResponse=response.body();
                    Message.showToast(getContext(),baseResponse.getMessage());
                    loadCartItem();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {

            }
        });


    }




    @Override
    public void quantityUp(CartItemDto cartItemDto) {
        cartItemDto.setQuantity(cartItemDto.getQuantity()+1);
        updateCartItem(cartItemDto);


    }

    @Override
    public void quantityDown(CartItemDto cartItemDto) {

        int quantity= cartItemDto.getQuantity();
        if(quantity==1){
            deleteCartItem(cartItemDto.getId());
        }else {
            quantity=quantity-1;
            cartItemDto.setQuantity(quantity);
            updateCartItem(cartItemDto);
        }

    }

    @Override
    public void deleteCartItem(Long id) {
        ApiClientInstance.getInstance(getContext()).deleteCartItem(id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    Message.showToast(getContext(),response.body().getMessage());
                    loadCartItem();
                }else {
                    Utils.handlerErrorResponse(response,getContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {

            }
        });

    }


    @Override
    public void initComponents(View view) {
        recyclerViewCart=view.findViewById(R.id.recyclerViewCart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerViewCart.setLayoutManager(layoutManager);

        btnPay=view.findViewById(R.id.btnPay);
        message=view.findViewById(R.id.txtMessageLoadCart);
        progressBar=view.findViewById(R.id.progressBarCart);
        totalPrice=view.findViewById(R.id.textViewTotalPrice);
    }
}
