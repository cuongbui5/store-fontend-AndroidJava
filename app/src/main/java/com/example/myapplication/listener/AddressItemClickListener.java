package com.example.myapplication.listener;

import com.example.myapplication.model.AddressDto;

public interface AddressItemClickListener {
    void setDefault(Long id);
    void deleteAddress(Long id);
    void updateAddress(AddressDto addressDto);
}
