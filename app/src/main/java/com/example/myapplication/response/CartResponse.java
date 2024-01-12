package com.example.myapplication.response;

import com.example.myapplication.model.CartItemDto;

import java.util.List;

public class CartResponse {
    private Long id;
    private List<CartItemDto> cartItemDtos;
    private double total;

    public List<CartItemDto> getCartItems() {
        return cartItemDtos;
    }

    public double getTotal() {
        return total;
    }

    public Long getId() {
        return id;
    }

    public List<CartItemDto> getCartItemDtos() {
        return cartItemDtos;
    }
}
