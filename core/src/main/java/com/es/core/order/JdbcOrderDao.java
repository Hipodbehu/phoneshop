package com.es.core.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JdbcOrderDao implements OrderDao {
  public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE secureId = ?";
  public static final String SELECT_ORDER_ITEM_BY_ID = "SELECT phones.*, orderItems.orderId, orderItems.quantity " +
          "FROM orderItems JOIN phones ON phones.id = orderItems.phoneId WHERE orderItems.orderId = ?";
  public static final String SELECT_PHONE_BY_ID = "SELECT phones.* FROM phones JOIN orderItems ON " +
          "phones.id = orderItems.phoneId WHERE orderItems.orderId = ?";
  public static final String INSERT_ORDER_ITEM = "INSERT INTO orderItems(phoneId, orderId, quantity) VALUES (?, ?, ?)";
  public static final String ORDERS_TABLE = "ORDERS";

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  @Resource
  private JdbcTemplate jdbcTemplate;

  private SimpleJdbcInsert simpleJdbcInsert;

  @PostConstruct
  private void init() {
    simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName(ORDERS_TABLE);
  }

  @Override
  public Optional<Order> find(String id) {
    readWriteLock.readLock().lock();
    Optional<Order> optionalOrder;
    try {
      List<Order> orderList = jdbcTemplate.query(SELECT_ORDER_BY_ID,
              new BeanPropertyRowMapper<>(Order.class), id);
      if (!orderList.isEmpty()) {
        Order order = orderList.get(0);
        order.setOrderItems(findItems(order));
        optionalOrder = Optional.of(order);
      } else {
        optionalOrder = Optional.empty();
      }
    } finally {
      readWriteLock.readLock().unlock();
    }
    return optionalOrder;
  }

  private List<OrderItem> findItems(Order order) {
    readWriteLock.readLock().lock();
    List<OrderItem> orderItems;
    try {
      orderItems = jdbcTemplate.query(SELECT_ORDER_ITEM_BY_ID,
              new BeanPropertyRowMapper<>(OrderItem.class), order.getSecureId());
      orderItems.stream()
              .forEach(orderItem -> {
                orderItem.setPhone(findPhone(order.getSecureId()));
                orderItem.setOrder(order);
              });
    } finally {
      readWriteLock.readLock().unlock();
    }
    return orderItems;
  }

  private Phone findPhone(String id) {
    readWriteLock.readLock().lock();
    Phone phone;
    try {
      List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_BY_ID,
              new BeanPropertyRowMapper<>(Phone.class), id);
      phone = phones.get(0);
    } finally {
      readWriteLock.readLock().unlock();
    }
    return phone;
  }

  @Override
  public void save(Order order) {
    readWriteLock.writeLock().lock();
    try {
      addOrder(order);
      addOrderItems(order.getOrderItems());
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private void addOrder(Order order) {
    SqlParameterSource source = new BeanPropertySqlParameterSource(order);
    simpleJdbcInsert.execute(source);
  }

  private void addOrderItems(List<OrderItem> orderItems) {
    readWriteLock.writeLock().lock();
    try {
      jdbcTemplate.batchUpdate(INSERT_ORDER_ITEM, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
          OrderItem orderItem = orderItems.get(i);
          preparedStatement.setLong(1, orderItem.getPhone().getId());
          preparedStatement.setString(2, orderItem.getOrder().getSecureId());
          preparedStatement.setInt(3, orderItem.getQuantity());
        }

        @Override
        public int getBatchSize() {
          return orderItems.size();
        }
      });
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }
}
