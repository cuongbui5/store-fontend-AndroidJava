package com.example.myapplication.listener;

import com.example.myapplication.model.CartItemDto;

import java.io.IOException;

public interface CartItemClickListener {
    void quantityUp(CartItemDto cartItemDto) throws IOException;
    void quantityDown(CartItemDto cartItemDto) throws IOException;
    void deleteCartItem(Long id);
}
