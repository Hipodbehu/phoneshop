package com.es.core.order;

import com.es.core.model.order.Order;

import java.util.Optional;

public interface OrderDao {
  Optional<Order> find(String id);

  void save(Order order);
}
