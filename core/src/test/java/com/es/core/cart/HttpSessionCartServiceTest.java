package com.es.core.cart;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.product.JdbcProductDao;
import com.es.core.order.OutOfStockException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HttpSessionCartServiceTest {
  public static final int TEST_VALID_QUANTITY = 1;
  public static final int TEST_UPDATE_VALID_QUANTITY = 2;
  public static final int TEST_TOO_BIG_QUANTITY = 100;
  public static final int TEST_RESERVED = 13;
  public static final int TEST_STOCK = 30;
  public static final long TEST_PHONE_IN_CART_ID = 1010L;
  public static final long TEST_PHONE_ID = 1011L;
  public static final long TEST_PRICE = 200L;

  private Cart cart;

  @Mock
  private Phone phone;

  @Mock
  private Stock stock;

  @Mock
  private ReentrantLock lock;

  @Mock
  private JdbcProductDao jdbcProductDao;

  @InjectMocks
  private HttpSessionCartService cartService = new HttpSessionCartService();

  @Before
  public void setup() {
    initMocks(this);
    cart = new Cart();
    Phone phoneToSave = new Phone();
    phoneToSave.setId(TEST_PHONE_IN_CART_ID);
    phoneToSave.setPrice(BigDecimal.valueOf(TEST_PRICE));
    cart.addItem(new CartItem(phoneToSave, TEST_VALID_QUANTITY));
    cart.setTotalCost(BigDecimal.valueOf(TEST_PRICE));
    cart.setTotalQuantity(TEST_VALID_QUANTITY);
    when(jdbcProductDao.getStock(anyLong())).thenReturn(Optional.of(stock));
    when(stock.getStock()).thenReturn(TEST_STOCK);
    when(stock.getReserved()).thenReturn(TEST_RESERVED);
    when(stock.getPhone()).thenReturn(phone);
    when(phone.getPrice()).thenReturn(BigDecimal.valueOf(TEST_PRICE));
  }

  @Test
  public void shouldAddWhenAddItemValid() throws OutOfStockException {
    int expected = cart.getItemList().size() + TEST_VALID_QUANTITY;
    cartService.addPhone(cart, TEST_PHONE_ID, TEST_VALID_QUANTITY);
    int actual = cart.getItemList().size();
    assertEquals(expected, actual);
  }

  @Test(expected = OutOfStockException.class)
  public void shouldThrowOutOfStockExceptionWhenAddItemInvalid() throws OutOfStockException {
    cartService.addPhone(cart, TEST_PHONE_ID, TEST_TOO_BIG_QUANTITY);
  }

  @Test
  public void shouldRecalculateTotalCostWhenAddItemValid() throws OutOfStockException {
    BigDecimal expected = cart.getTotalCost().add(BigDecimal.valueOf(TEST_PRICE));
    cartService.addPhone(cart, TEST_PHONE_ID, TEST_VALID_QUANTITY);
    BigDecimal actual = cart.getTotalCost();
    assertEquals(expected, actual);
  }

  @Test
  public void shouldRecalculateTotalCostWhenUpdateItemValid() throws OutOfStockException {
    HashMap<Long, Integer> products = new HashMap<>();
    products.put(TEST_PHONE_IN_CART_ID, TEST_UPDATE_VALID_QUANTITY);
    BigDecimal expected = cart.getTotalCost().add(BigDecimal.valueOf(TEST_PRICE *
            (TEST_UPDATE_VALID_QUANTITY - TEST_VALID_QUANTITY)));
    cartService.update(cart, products);
    BigDecimal actual = cart.getTotalCost();
    assertEquals(expected, actual);
  }

  @Test(expected = OutOfStockException.class)
  public void shouldThrowExceptionWhenUpdateItemInvalid() throws OutOfStockException {
    HashMap<Long, Integer> products = new HashMap<>();
    products.put(TEST_PHONE_IN_CART_ID, TEST_TOO_BIG_QUANTITY);
    Map<Long, String> errors = new HashMap<>();
    cartService.update(cart, products);
    assertFalse(errors.isEmpty());
  }

  @Test
  public void shouldRemoveWhenRemoveItemValid() {
    int expected = cart.getItemList().size() - TEST_VALID_QUANTITY;
    cartService.remove(cart, TEST_PHONE_IN_CART_ID);
    int actual = cart.getItemList().size();
    assertEquals(expected, actual);
  }
}