package com.es.core.cart;

import com.es.core.order.OutOfStockException;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface CartService {

  Cart getCart(HttpSession session);

  void addPhone(Cart cart, Long phoneId, Integer quantity) throws OutOfStockException;

  /**
   * @param items key: {@link com.es.core.model.phone.Phone#id}
   *              value: quantity
   */
  void update(Cart cart, Map<Long, Integer> items) throws OutOfStockException;

  void clear(HttpSession session);

  void remove(Cart cart, Long phoneId);
}
