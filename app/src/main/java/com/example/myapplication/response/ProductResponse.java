package com.example.myapplication.response;


import com.example.myapplication.model.Product;

import java.util.List;

public class ProductResponse {
    private List<Product> products;
    private int totalPage;
    private int pageIndex;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
