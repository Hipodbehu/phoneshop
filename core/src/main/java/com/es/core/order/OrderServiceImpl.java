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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
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
    public void placeOrder(Order order) throws OutOfStockException {
        lock.lock();
        try {
            for (OrderItem orderItem : order.getOrderItems()) {
                Stock stock = productDao.getStock(orderItem.getPhone().getId())
                        .orElseThrow(ProductNotFoundException::new);
                if (orderItem.getQuantity() > stock.getStock() - stock.getReserved()) {
                    throw new OutOfStockException();
                }
            }
            order.getOrderItems().forEach(orderItem -> {
                Stock stock = productDao.getStock(orderItem.getPhone().getId())
                        .orElseThrow(ProductNotFoundException::new);
                stock.setReserved(stock.getReserved() + orderItem.getQuantity());
                productDao.updateStock(stock);
            });
            LocalDateTime localDateTime = LocalDateTime.now();
            order.setDate(String.join(" ", localDateTime.toLocalDate().toString(),
                    localDateTime.toLocalTime().toString()));
            order.setStatus(OrderStatus.NEW);
            order.setSecureId(UUID.randomUUID().toString());
            orderDao.save(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Order getOrder(Long id) {
        Order order = orderDao.find(id).orElseThrow(OrderNotFoundException::new);
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        Order order = orderDao.findBySecureId(secureId).orElseThrow(OrderNotFoundException::new);
        order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));
        return order;
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orderList = orderDao.findAll();
        orderList.forEach(order -> order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice())));
        return orderList;
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        orderDao.updateStatus(id, orderStatus);
    }
}
