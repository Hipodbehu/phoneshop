package com.es.phoneshop.web.controller.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddProductInputWrapper {
  @NotNull(message = "Id cannot be empty")
  private Long id;

  @NotNull(message = "Bad quantity")
  @Min(1)
  private Integer quantity;

  public AddProductInputWrapper() {
  }

  public AddProductInputWrapper(Long id, Integer quantity) {
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
