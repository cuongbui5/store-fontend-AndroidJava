package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AddressAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.listener.AddressItemClickListener;
import com.example.myapplication.model.AddressDto;
import com.example.myapplication.response.BaseResponse;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.SharedPreferencesManager;
import com.example.myapplication.util.UserDetail;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity implements AddressItemClickListener {
    private Button btnOpen,btnAddAddress;
    private LinearLayout bottomSheet;

    private TextInputEditText number,street,city;
    private RecyclerView recyclerViewAddress;


    private Long idUpdate;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initComponents();
        loadAddress();

        btnOpen.setOnClickListener(v->{
            btnAddAddress.setText("Add");
            refreshEditText();
            if(bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        });



        btnAddAddress.setOnClickListener(v->{
            if(btnAddAddress.getText()=="Add"){
                AddressDto addressDto=new AddressDto();
                addressDto.setCity(city.getText().toString());
                addressDto.setApartmentNumber(Integer.valueOf(number.getText().toString()));
                addressDto.setStreet(street.getText().toString());
                ApiClientInstance.getInstance(this).addNewAddress(UserDetail.id,addressDto).enqueue(new Callback<AddressDto>() {
                    @Override
                    public void onResponse(@NonNull Call<AddressDto> call, @NonNull Response<AddressDto> response) {
                        if(response.isSuccessful()){
                            Message.showToast(AddressActivity.this,"Add ok!");
                            loadAddress();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddressDto> call, @NonNull Throwable t) {
                        Message.showToast(AddressActivity.this,t.getMessage());

                    }
                });

            }else {
                AddressDto addressDto=new AddressDto();
                addressDto.setCity(city.getText().toString());
                addressDto.setApartmentNumber(Integer.valueOf(number.getText().toString()));
                addressDto.setStreet(street.getText().toString());
                addressDto.setId(idUpdate);
                ApiClientInstance.getInstance(this).updateAddress(addressDto).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if(response.isSuccessful()){
                            Message.showToast(AddressActivity.this,response.body().getMessage());
                            loadAddress();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });


            }



        });


    }

    private void loadAddress(){
        ApiClientInstance.getInstance(this).getAllAddress(UserDetail.id).enqueue(new Callback<List<AddressDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AddressDto>> call, @NonNull Response<List<AddressDto>> response) {
                if(response.isSuccessful()){

                    List<AddressDto> list=response.body();
                    Message.showToast(AddressActivity.this,list.size()+"");
                    Log.d("AddressActivity",list.toString());
                    AddressAdapter adapter=new AddressAdapter(list,AddressActivity.this);
                    recyclerViewAddress.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<AddressDto>> call, Throwable t) {

            }
        });

    }

    private void initComponents(){
        btnOpen=findViewById(R.id.btnOpenBottomSheetAddress);
        bottomSheet=findViewById(R.id.bottomSheetAddAddress);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        btnAddAddress=findViewById(R.id.btnAddNewAddress);
        number=findViewById(R.id.editTextApartmentNumber);
        city=findViewById(R.id.editTextCity);
        recyclerViewAddress=findViewById(R.id.recyclerViewAddress);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AddressActivity.this);
        recyclerViewAddress.setLayoutManager(linearLayoutManager);
        street=findViewById(R.id.editTextStreet);

    }


    @Override
    public void setDefault(Long id) {
        ApiClientInstance.getInstance(this).setDefault(id,UserDetail.id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    Message.showToast(AddressActivity.this,response.body().getMessage());
                    loadAddress();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteAddress(Long id) {
        ApiClientInstance.getInstance(this).deleteAddress(id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    Message.showToast(AddressActivity.this,response.body().getMessage());
                    loadAddress();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    private void refreshEditText(){
        city.setText("");
        street.setText("");
        number.setText("");

    }

    @Override
    public void updateAddress(AddressDto addressDto) {
        city.setText(addressDto.getCity());
        street.setText(addressDto.getStreet());
        number.setText(addressDto.getApartmentNumber()+"");
        idUpdate=addressDto.getId();
        if(bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        btnAddAddress.setText("Update");

    }
}
