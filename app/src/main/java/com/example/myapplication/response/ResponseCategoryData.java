package com.example.myapplication.response;


import com.example.myapplication.model.Category;

import java.util.List;

public class ResponseCategoryData extends BaseResponse{
    private List<Category> data;
    public ResponseCategoryData(String status, String message, List<Category> data) {
        super(status, message);
        this.data = data;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
