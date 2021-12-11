package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OutOfStockException;

    Order getOrder(String id);
}
