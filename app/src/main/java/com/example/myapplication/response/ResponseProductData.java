package com.example.myapplication.response;


public class ResponseProductData extends BaseResponse{
    private ProductResponse data;
    public ResponseProductData(String status, String message, ProductResponse productResponse) {
        super(status, message);
        this.data = productResponse;
    }

    public ProductResponse getData() {
        return data;
    }

    public void setData(ProductResponse data) {
        this.data = data;
    }
}
