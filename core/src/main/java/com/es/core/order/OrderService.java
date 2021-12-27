package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order) throws OutOfStockException;

    Order getOrder(Long id);

    Order getOrderBySecureId(String secureId);

    List<Order> getOrders();

    void updateOrderStatus(Long id, OrderStatus orderStatus);
}
