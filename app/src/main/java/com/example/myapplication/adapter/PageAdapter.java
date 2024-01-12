package com.example.myapplication.adapter;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listener.OnPageClickListener;




public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageViewHolder> {
    private int totalPage;
    private int pageCurrent;
    private OnPageClickListener onPageClickListener;

    public PageAdapter(int totalPage, int pageCurrent, OnPageClickListener onPageClickListener) {
        this.totalPage = totalPage;
        this.pageCurrent = pageCurrent;
        this.onPageClickListener = onPageClickListener;

    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageCurrent() {
        return pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public OnPageClickListener getOnPageClickListener() {
        return onPageClickListener;
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false));
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        int page = position + 1;
        Button btnPage=holder.btnPage;
        btnPage.setText(String.valueOf(page));
        btnPage.setOnClickListener(v -> {
            onPageClickListener.onPageClick(page);
            pageCurrent=page;
            notifyDataSetChanged();

        });


        if (page == pageCurrent) {
            btnPage.setBackgroundColor(ContextCompat.getColor(btnPage.getContext(), com.google.android.material.R.color.button_material_dark));
        }



    }



    @Override
    public int getItemCount() {
        return totalPage;
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton btnPage;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPage = itemView.findViewById(R.id.btnPage);

        }


    }
}
