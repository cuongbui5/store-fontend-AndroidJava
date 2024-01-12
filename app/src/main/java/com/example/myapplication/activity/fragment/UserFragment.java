package com.example.myapplication.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.activity.AddressActivity;
import com.example.myapplication.activity.AppActivity;
import com.example.myapplication.util.UserDetail;

import java.util.Objects;

public class UserFragment extends Fragment implements FragmentScreen{
    private CardView btnOrders, btnAddressSetting,btnAccountSetting;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        initComponents(view);
        btnAddressSetting.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AddressActivity.class);
            startActivity(intent);

        });
        return view;
    }

    @Override
    public void initComponents(View view) {
        btnAccountSetting=view.findViewById(R.id.btnAccountSetting);
        btnOrders=view.findViewById(R.id.btnOrders);
        btnAddressSetting=view.findViewById(R.id.btnAddressSetting);




    }
}
