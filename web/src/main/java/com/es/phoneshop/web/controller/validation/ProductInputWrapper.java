package com.es.phoneshop.web.controller.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductInputWrapper {
  @NotNull(message = "Id cannot be empty")
  private Long id;

  @NotNull(message = "Bad quantity")
  @Min(1)
  private Integer quantity;

  public ProductInputWrapper() {
  }

  public ProductInputWrapper(Long id, Integer quantity) {
    this.id = id;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
