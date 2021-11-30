package com.es.phoneshop.web.controller.dto;

import java.io.Serializable;

public class AddPhoneResponseDto implements Serializable {
  private MiniCartDto miniCart;
  private String message;
  private boolean isSuccessful;

  public MiniCartDto getMiniCart() {
    return miniCart;
  }

  public void setMiniCart(MiniCartDto miniCart) {
    this.miniCart = miniCart;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public void setSuccessful(boolean successful) {
    this.isSuccessful = successful;
  }
}
