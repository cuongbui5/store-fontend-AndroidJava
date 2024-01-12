package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listener.CartItemClickListener;
import com.example.myapplication.model.CartItemDto;
import com.example.myapplication.util.Message;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItemDto> cartItemDtos;
    private CartItemClickListener cartItemClickListener;

    public CartItemAdapter(List<CartItemDto> cartItemDtos, CartItemClickListener cartItemClickListener) {
        this.cartItemDtos = cartItemDtos;
        this.cartItemClickListener=cartItemClickListener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItemDto cartItemDto = cartItemDtos.get(position);

        holder.name.setText(cartItemDto.getProduct().getName());
        holder.price.setText("Price :$"+ cartItemDto.getProduct().getPrice());
        holder.quantity.setText(cartItemDto.getQuantity()+"");
        holder.totalPrice.setText("Total :$"+ cartItemDto.getTotal());
        String imageUrl = cartItemDto.getProduct().getImageCover();
        Picasso.get().load(imageUrl).into(holder.image);
        holder.btnIn.setOnClickListener(v -> {

            try {
                cartItemClickListener.quantityUp(cartItemDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        holder.btnDes.setOnClickListener(v -> {

            try {
                cartItemClickListener.quantityDown(cartItemDto);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        });
        holder.btnDelete.setOnClickListener(v -> cartItemClickListener.deleteCartItem(cartItemDto.getId()));


    }

    @Override
    public int getItemCount() {
        if(cartItemDtos!=null){
            return cartItemDtos.size();
        }
        return 0;


    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image,btnIn,btnDes,btnDelete;
        TextView price,totalPrice,name,quantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.cartItemImage);
            btnIn=itemView.findViewById(R.id.btnIncrement);
            btnDes=itemView.findViewById(R.id.btnDes);
            btnDelete=itemView.findViewById(R.id.btnDeleteCartItem);
            price=itemView.findViewById(R.id.cartItemPrice);
            name=itemView.findViewById(R.id.cartItemName);
            totalPrice=itemView.findViewById(R.id.cartItemTotal);
            quantity=itemView.findViewById(R.id.cartItemQuantity);
        }
    }
}
