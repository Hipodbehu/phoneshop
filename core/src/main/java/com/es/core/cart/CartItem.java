package com.es.core.cart;

import com.es.core.model.phone.Phone;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
  private Phone phone;
  private int quantity;

  public CartItem(Phone phone, int quantity) {
    this.phone = phone;
    this.quantity = quantity;
  }

  public Phone getPhone() {
    return phone;
  }

  public void setPhone(Phone phone) {
    this.phone = phone;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CartItem cartItem = (CartItem) o;

    return phone != null ? phone.equals(cartItem.phone) : cartItem.phone == null;
  }

  @Override
  public int hashCode() {
    int result = phone != null ? phone.hashCode() : 0;
    result = 31 * result + quantity;
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CartItem{");
    sb.append("phone=").append(phone);
    sb.append(", quantity=").append(quantity);
    sb.append('}');
    return sb.toString();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
