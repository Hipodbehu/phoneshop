package com.es.phoneshop.web.controller.validation;

import javax.validation.constraints.Pattern;

public class ModelInputWrapper {
  private String model;

  @Pattern(regexp = "^[0-9]*$", message = "Not a number")
  private String quantity;

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }
}
