package com.example.myapplication.model;




public class CartItemDto {

  private Long id;

  private Product product;
  private Integer quantity;
  private Double total;

  public Long getId() {
    return id;
  }

  public Product getProduct() {
    return product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public Double getTotal() {
    return total;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public void setTotal(Double total) {
    this.total = total;
  }
}
