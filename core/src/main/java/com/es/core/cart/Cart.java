package com.es.core.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart implements Serializable {
  private List<CartItem> itemList;
  private int totalQuantity;
  private BigDecimal totalCost;
  private BigDecimal deliveryCost;

  public Cart() {
    this.itemList = new ArrayList<>();
    this.totalQuantity = 0;
    this.totalCost = BigDecimal.ZERO;
  }

  public List<CartItem> getItemList() {
    return itemList;
  }

  public void addItem(CartItem item) {
    this.itemList.add(item);
  }

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

  public BigDecimal getDeliveryCost() {
    return deliveryCost;
  }

  public void setDeliveryCost(BigDecimal deliveryCost) {
    this.deliveryCost = deliveryCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cart cart = (Cart) o;
    return totalQuantity == cart.totalQuantity
            && Objects.equals(itemList, cart.itemList)
            && Objects.equals(totalCost, cart.totalCost)
            && Objects.equals(deliveryCost, cart.deliveryCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemList, totalQuantity, totalCost, deliveryCost);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Cart{");
    sb.append("itemList=").append(itemList);
    sb.append(", totalQuantity=").append(totalQuantity);
    sb.append(", totalCost=").append(totalCost);
    sb.append(", deliveryCost=").append(deliveryCost);
    sb.append('}');
    return sb.toString();
  }
}
