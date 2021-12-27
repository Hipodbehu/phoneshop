package com.es.core.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
  Optional<Order> find(Long id);

  Optional<Order> findBySecureId(String secureId);

  List<Order> findAll();

  void save(Order order);

  void updateStatus(Long id, OrderStatus orderStatus);
}
