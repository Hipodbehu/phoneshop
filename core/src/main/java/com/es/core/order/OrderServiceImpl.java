package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;
import com.es.core.model.product.ProductDao;
import com.es.core.model.product.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    public static final String LESS_STOCK_MESSAGE = "Stock is less";

    private final ReentrantLock lock = new ReentrantLock();

    @Resource
    private OrderDao orderDao;

    @Resource
    private ProductDao productDao;

    @Override
    public Order createOrder(Cart cart) {
        lock.lock();
        Order order = new Order();
        try {
            order.setOrderItems(cart.getItemList().stream()
                    .map(cartItem -> new OrderItem(cartItem.getPhone(), order, cartItem.getQuantity()))
                    .collect(Collectors.toList()));
            order.setSubtotal(cart.getTotalCost());
            order.setDeliveryPrice(cart.getDeliveryCost());
            order.setTotalPrice(cart.getTotalCost().add(cart.getDeliveryCost()));
        } finally {
            lock.unlock();
        }
        return order;
    }

    @Override
    public void placeOrder(Order order, Map<String, String> errors) {
        lock.lock();
        try {
            order.getOrderItems().forEach(orderItem -> {
                Stock stock = productDao.getStock(orderItem.getPhone().getId())
                        .orElseThrow(ProductNotFoundException::new);
                if (orderItem.getQuantity() > stock.getStock() - stock.getReserved()) {
                    errors.put(String.valueOf(orderItem.getPhone().getId()), LESS_STOCK_MESSAGE);
                }
            });
            if (errors.isEmpty()) {
                order.getOrderItems().forEach(orderItem -> {
                    Stock stock = productDao.getStock(orderItem.getPhone().getId())
                            .orElseThrow(ProductNotFoundException::new);
                    stock.setReserved(stock.getReserved() + orderItem.getQuantity());
                    productDao.updateStock(stock);
                });
                order.setSecureId(UUID.randomUUID().toString());
                orderDao.save(order);
                order.setStatus(OrderStatus.NEW);
            } else {
                order.setStatus(OrderStatus.REJECTED);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Order getOrder(String id) {
        Order order = orderDao.find(id).orElseThrow(OrderNotFoundException::new);
        return order;
    }
}
