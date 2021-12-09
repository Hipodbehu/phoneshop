package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order, Map<String, String> errors);

    Order getOrder(String id);
}
