package com.example.myapplication.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceControl;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.fragment.CartFragment;
import com.example.myapplication.activity.fragment.ProductFragment;
import com.example.myapplication.activity.fragment.UserFragment;
import com.example.myapplication.listener.OnItemProductClickListener;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.SharedPreferencesManager;
import com.example.myapplication.util.UserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity implements OnItemProductClickListener {
    private BottomNavigationView navigation;
    private Fragment fragmentCurrent;
    MenuItem mIProduct,mICart,mIUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initComponents();
        loadUser();
        initNavigationOnItemSelectedListener();
        navigation.setSelectedItemId(R.id.action_product);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @SuppressLint("RestrictedApi")
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    int size=fragmentManager.getFragments().size();
                    fragmentCurrent=fragmentManager.getFragments().get(size-2);
                    if(fragmentCurrent instanceof ProductFragment){
                        mIProduct.setChecked(true);
                    } else if (fragmentCurrent instanceof CartFragment) {
                        mICart.setChecked(true);
                    }else {
                        mIUser.setChecked(true);
                    }
                    Log.d("replaceFragment","size:"+size);
                    Log.d("replaceFragment","current sau pop:"+fragmentCurrent);


                    Log.d("replaceFragment","List:"+ fragmentManager.getFragments());

                } else {
                    finish();
                }
            }
        });


    }



    private void loadUser() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        LoginResponse loginResponse = sharedPreferencesManager.getUserData();
        if (loginResponse != null) {
            UserDetail.id= (long) loginResponse.getUserId();
            UserDetail.username=loginResponse.getUsername();
        }else {
            Intent newIntent = new Intent(AppActivity.this, MainActivity.class);
            startActivity(newIntent);
        }
    }


    private void initNavigationOnItemSelectedListener(){
        navigation.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            int id=item.getItemId();
            if(id==R.id.action_product){
                mIProduct=item;
                replaceFragment(new ProductFragment(this));
                return true;

            }else if(id==R.id.action_cart){
                mICart=item;
                replaceFragment(new CartFragment());
                return true;
            }
            else if(id==R.id.action_user){
                mIUser=item;
                replaceFragment(new UserFragment());
                return true;
            }else {
                return false;
            }
        });

    }


    public void initComponents() {
        navigation=findViewById(R.id.bottomNavigation);

    }

    public void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentContainer= getSupportFragmentManager()
                .beginTransaction();
        Log.d("replaceFragment", "current:"+fragmentCurrent);
        if(fragmentCurrent!=null){
            fragmentContainer.hide(fragmentCurrent);
        }
        if (!fragment.isAdded()) {
            fragmentContainer.add(R.id.frameContainer, fragment);

            fragmentContainer.show(fragment);

        }
        else {
            fragmentContainer.show(fragment);

        }


        fragmentContainer
                .addToBackStack(null)
                .commit();
        fragmentCurrent=fragment;
    }



    @Override
    public void onItemProductClick(Long productId) {
        Intent intent=new Intent(AppActivity.this,ProductDetailActivity.class);
        intent.putExtra("productId",productId);
        startActivity(intent);

    }
}
