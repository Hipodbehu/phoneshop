package com.es.phoneshop.web.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MiniCartDto implements Serializable {
  private int totalQuantity;
  private BigDecimal totalCost;

  public int getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(int totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }
}
