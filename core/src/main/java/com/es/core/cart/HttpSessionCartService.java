package com.es.core.cart;

import com.es.core.model.phone.Stock;
import com.es.core.model.product.ProductDao;
import com.es.core.model.product.ProductNotFoundException;
import com.es.core.order.OutOfStockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class HttpSessionCartService implements CartService {
  public static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";

  private final ReentrantLock lock = new ReentrantLock();

  @Resource
  private ProductDao productDao;

  @Value("${delivery.price}")
  private String deliveryPrice;

  @Override
  public Cart getCart(HttpSession session) {
    lock.lock();
    Cart cart;
    try {
      cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
      if (cart == null) {
        cart = new Cart();
        cart.setDeliveryCost(BigDecimal.valueOf(Double.parseDouble(deliveryPrice)));
        session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
      }
    } finally {
      lock.unlock();
    }
    return cart;
  }

  @Override
  public void addPhone(Cart cart, Long phoneId, Integer quantity) throws OutOfStockException {
    lock.lock();
    try {
      Optional<CartItem> optionalCartItem = getCartItem(cart, phoneId);
      if (optionalCartItem.isPresent()) {
        HashMap<Long, Integer> toUpdateMap = new HashMap<>();
        CartItem cartItem = optionalCartItem.get();
        toUpdateMap.put(cartItem.getPhone().getId(), cartItem.getQuantity() + quantity);
        update(cart, toUpdateMap);
      } else {
        Stock stock = productDao.getStock(phoneId).orElseThrow(ProductNotFoundException::new);
        if (quantity > stock.getStock() - stock.getReserved()) {
          throw new OutOfStockException();
        } else {
          CartItem cartItem = new CartItem(stock.getPhone(), quantity);
          cart.addItem(cartItem);
          recalculateCart(cart);
        }
      }
    } finally {
      lock.unlock();
    }
  }

  private Optional<CartItem> getCartItem(Cart cart, Long phoneId) {
    return cart.getItemList().stream()
            .filter(cartItem -> cartItem.getPhone().getId().equals(phoneId))
            .findAny();
  }

  private void recalculateCart(Cart cart) {
    List<CartItem> itemList = cart.getItemList();
    cart.setTotalQuantity(calculateQuantity(itemList));
    cart.setTotalCost(calculateCost(itemList));
  }

  private int calculateQuantity(List<CartItem> itemList) {
    return itemList.stream()
            .map(CartItem::getQuantity)
            .reduce(Integer::sum)
            .orElse(0);
  }

  private BigDecimal calculateCost(List<CartItem> itemList) {
    return itemList.stream()
            .map(cartItem -> cartItem.getPhone().getPrice()
                    .multiply(new BigDecimal(cartItem.getQuantity())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
  }

  @Override
  public void update(Cart cart, Map<Long, Integer> items) throws OutOfStockException {
    lock.lock();
    try {
      List<CartItem> itemList = items.keySet().stream()
              .map(phoneId -> getCartItem(cart, phoneId).orElseThrow(ProductNotFoundException::new))
              .collect(Collectors.toList());
      List<Stock> stocks = items.keySet().stream()
              .map(phoneId -> productDao.getStock(phoneId).orElseThrow(ProductNotFoundException::new))
              .collect(Collectors.toList());
      List<Long> errorIds = new ArrayList<>();
      for (int i = 0; i < stocks.size(); i++) {
        CartItem cartItem = itemList.get(i);
        Stock stock = stocks.get(i);
        Integer quantity = items.get(cartItem.getPhone().getId());
        if (quantity > stock.getStock() - stock.getReserved()) {
          errorIds.add(cartItem.getPhone().getId());
        } else {
          cartItem.setQuantity(quantity);
          recalculateCart(cart);
        }
      }
      if (!errorIds.isEmpty()) {
        throw new OutOfStockException(errorIds);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void clear(HttpSession session) {
    session.removeAttribute(CART_SESSION_ATTRIBUTE);
  }

  @Override
  public void remove(Cart cart, Long phoneId) {
    lock.lock();
    try {
      Optional<CartItem> cartItem = getCartItem(cart, phoneId);
      if (cartItem.isPresent()) {
        cart.getItemList().remove(cartItem.get());
      }
      recalculateCart(cart);
    } finally {
      lock.unlock();
    }
  }
}
