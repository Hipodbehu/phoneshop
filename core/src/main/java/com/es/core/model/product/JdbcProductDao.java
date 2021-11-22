package com.es.core.model.product;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JdbcProductDao implements ProductDao {
  public static final String INSERT_INTO_PHONE_2_COLOR = "INSERT INTO phone2color(phoneId, colorId) VALUES(?, ?)";
  public static final String UPDATE_PHONE = "UPDATE phones SET brand = ?, model = ?, price = ?, displaySizeInches = ?, " +
          "weightGr = ?, lengthMm = ?, widthMm = ?, heightMm = ?, announced = ?, deviceType = ?, os = ?, " +
          "displayResolution = ?, pixelDensity = ?, displayTechnology = ?, backCameraMegapixels = ?, " +
          "frontCameraMegapixels = ?, ramGb = ?, internalStorageGb = ?, batteryCapacityMah = ?, talkTimeHours = ?, " +
          "standByTimeHours = ?, bluetooth = ?, positioning = ?, imageUrl = ?, description = ? WHERE id = ?";
  public static final String DELETE_BY_PHONE_ID_FROM_PHONE_2_COLOR = "DELETE FROM phone2color WHERE phoneId = ?";
  public static final String DELETE_BY_ID_FROM_PHONES = "DELETE FROM phones WHERE id = ?";
  public static final String SELECT_PHONE_ID_BY_BRAND_AND_MODEL = "SELECT id FROM phones WHERE brand = ? AND model = ?";
  public static final String SELECT_PHONE_BY_ID_FROM_PHONES = "SELECT * FROM phones WHERE id = ?";
  public static final String SELECT_COLOR_BY_PHONE_ID_FROM_COLORS = "SELECT id, code FROM colors " +
          "JOIN phone2color ON phone2color.colorId = colors.id WHERE phone2color.phoneId = ?";
  public static final String SELECT_FROM_PHONES_OFFSET_LIMIT = "SELECT * FROM phones OFFSET ? LIMIT ?";
  public static final String PHONES_TABLE = "phones";
  public static final String PHONES_TABLE_ID_COLUMN = "id";

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  @Resource
  private JdbcTemplate jdbcTemplate;

  private SimpleJdbcInsert simpleJdbcInsert;

  @PostConstruct
  private void init() {
    simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName(PHONES_TABLE)
            .usingGeneratedKeyColumns(PHONES_TABLE_ID_COLUMN);
  }

  @Override
  public Optional<Phone> find(Long key) {
    readWriteLock.readLock().lock();
    Optional<Phone> phone;
    try {
      List<Phone> phones = jdbcTemplate.query(SELECT_PHONE_BY_ID_FROM_PHONES,
              new BeanPropertyRowMapper<>(Phone.class), key);
      phone = phones.isEmpty() ? Optional.empty() : Optional.ofNullable(phones.get(0));
      phone.ifPresent(this::setColors);
    } finally {
      readWriteLock.readLock().unlock();
    }
    return phone;
  }

  private void setColors(Phone phone) {
    List<Color> colorList = jdbcTemplate.query(SELECT_COLOR_BY_PHONE_ID_FROM_COLORS,
            new BeanPropertyRowMapper<>(Color.class), phone.getId());
    Set<Color> colors = new HashSet<>(colorList);
    phone.setColors(colors);
  }

  @Override
  public List<Phone> findAll(int offset, int limit) {
    readWriteLock.readLock().lock();
    List<Phone> phones;
    try {
      phones = jdbcTemplate.query(SELECT_FROM_PHONES_OFFSET_LIMIT,
              new BeanPropertyRowMapper<>(Phone.class), offset, limit);
      phones.forEach(this::setColors);
    } finally {
      readWriteLock.readLock().unlock();
    }
    return phones;
  }

  @Override
  public void save(Phone phone) throws InvalidProductException {
    readWriteLock.writeLock().lock();
    try {
      if (phone != null && phone.getBrand() != null && phone.getModel() != null) {
        Optional<Long> id = getPhoneId(phone);
        if (id.isPresent()) {
          update(phone);
        } else {
          addPhone(phone);
          addColors(phone);
        }
      } else {
        throw new InvalidProductException();
      }
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private Optional<Long> getPhoneId(Phone phone) {
    List<Long> ids = jdbcTemplate.queryForList(SELECT_PHONE_ID_BY_BRAND_AND_MODEL,
            Long.class, phone.getBrand(), phone.getModel());
    return ids.isEmpty() ? Optional.empty() : Optional.of(ids.get(0));
  }

  private void addPhone(Phone phone) {
    SqlParameterSource source = new BeanPropertySqlParameterSource(phone);
    Long id = (Long) simpleJdbcInsert.executeAndReturnKey(source);
    phone.setId(id);
  }

  private void addColors(Phone phone) {
    if (CollectionUtils.isEmpty(phone.getColors())) {
      List<Color> colors = new ArrayList<>(phone.getColors());
      jdbcTemplate.batchUpdate(INSERT_INTO_PHONE_2_COLOR, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
          preparedStatement.setLong(1, phone.getId());
          preparedStatement.setLong(2, colors.get(i).getId());
        }

        @Override
        public int getBatchSize() {
          return colors.size();
        }
      });
    }
  }

  @Override
  public void update(Phone phone) throws InvalidProductException, ProductNotFoundException {
    readWriteLock.writeLock().lock();
    try {
      if (phone != null && phone.getBrand() != null && phone.getModel() != null) {
        Optional<Long> id = getPhoneId(phone);
        if (id.isPresent()) {
          phone.setId(id.get());
          jdbcTemplate.update(UPDATE_PHONE, phone.getBrand(), phone.getModel(), phone.getPrice(),
                  phone.getDisplaySizeInches(), phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(),
                  phone.getHeightMm(), phone.getAnnounced(), phone.getDeviceType(), phone.getOs(),
                  phone.getDisplayResolution(), phone.getPixelDensity(), phone.getDisplayTechnology(),
                  phone.getBackCameraMegapixels(), phone.getFrontCameraMegapixels(), phone.getRamGb(),
                  phone.getInternalStorageGb(), phone.getBatteryCapacityMah(), phone.getTalkTimeHours(),
                  phone.getStandByTimeHours(), phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(),
                  phone.getDescription(), phone.getId());
          jdbcTemplate.update(DELETE_BY_PHONE_ID_FROM_PHONE_2_COLOR, phone.getId());
          addColors(phone);
        } else {
          throw new ProductNotFoundException();
        }
      } else {
        throw new InvalidProductException();
      }
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void delete(Long key) {
    readWriteLock.writeLock().lock();
    try {
      if (key != null) {
        jdbcTemplate.update(DELETE_BY_ID_FROM_PHONES, key);
        jdbcTemplate.update(DELETE_BY_PHONE_ID_FROM_PHONE_2_COLOR, key);
      }
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }
}
