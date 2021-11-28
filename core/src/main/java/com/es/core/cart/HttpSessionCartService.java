package com.es.core.cart;

import com.es.core.model.phone.Stock;
import com.es.core.model.product.ProductDao;
import com.es.core.model.product.ProductNotFoundException;
import com.es.core.order.OutOfStockException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class HttpSessionCartService implements CartService {
  public static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";

  @Resource
  private ProductDao productDao;

  private ReentrantLock lock;

  @PostConstruct
  private void init() {
    lock = new ReentrantLock();
  }

  @Override
  public Cart getCart(HttpSession session) {
    lock.lock();
    Cart cart;
    try {
      cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
      if (cart == null) {
        cart = new Cart();
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
      Stock stock = productDao.getStock(phoneId).orElseThrow(ProductNotFoundException::new);
      if (quantity > stock.getStock() - stock.getReserved()) {
        throw new OutOfStockException();
      } else {
        CartItem cartItem = new CartItem(stock.getPhone(), quantity);
        cart.addItem(cartItem);
        recalculateCart(cart);
      }
    } finally {
      lock.unlock();
    }
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
  public void update(Map<Long, Long> items) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public void remove(Long phoneId) {
    throw new UnsupportedOperationException("TODO");
  }
}
