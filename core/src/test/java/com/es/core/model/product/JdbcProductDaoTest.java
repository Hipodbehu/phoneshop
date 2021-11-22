package com.es.core.model.product;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ContextConfiguration("/applicationTestContext-core.xml")
public class JdbcProductDaoTest {
  public static final Long FIRST_PHONE_ID = 1001L;
  public static final Long LAST_PHONE_ID = 1009L;
  public static final String BRAND = "test_brand";
  public static final String MODEL = "test_model";
  public static final String PHONES_TABLE = "phones";
  public static final String COLOR_CODE = "Test-Color";
  public static final long NOT_EXISTED_ID = 1L;
  public static final String ARCHOS_BRAND = "ARCHOS";
  public static final String ARCHOS_MODEL = "ARCHOS 28 Internet Tablet";

  @Resource
  private JdbcProductDao jdbcProductDao;

  @Resource
  private JdbcTemplate jdbcTemplate;

  @After
  public void clean() {
    JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, PHONES_TABLE, "brand = ? AND model = ?", BRAND, MODEL);
  }

  @Test
  public void shouldSaveWhenSavePhoneValid() {
    Phone phone = new Phone();
    phone.setBrand(BRAND);
    phone.setModel(MODEL);
    Color color = new Color();
    color.setCode(COLOR_CODE);
    phone.setColors(Stream.of(color).collect(Collectors.toSet()));
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE) + 1;
    jdbcProductDao.save(phone);
    int actualPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    assertEquals(expectedPhonesCount, actualPhonesCount);
  }

  @Test(expected = InvalidProductException.class)
  public void shouldThrowInvalidProductExceptionWhenSavePhoneNull() {
    jdbcProductDao.save(null);
  }

  @Test(expected = InvalidProductException.class)
  public void shouldThrowInvalidProductExceptionWhenSavePhoneNoBrand() {
    Phone phone = new Phone();
    phone.setModel(MODEL);
    jdbcProductDao.save(null);
  }

  @Test(expected = InvalidProductException.class)
  public void shouldThrowInvalidProductExceptionWhenSavePhoneNoModel() {
    Phone phone = new Phone();
    phone.setBrand(BRAND);
    jdbcProductDao.save(null);
  }

  @Test
  public void shouldGetPhoneWhenFindPhoneExist() {
    Optional<Phone> actual = jdbcProductDao.find(FIRST_PHONE_ID);
    assertTrue(actual.isPresent());
  }

  @Test
  public void shouldGetEmptyWhenFindPhoneNotExist() {
    Optional<Phone> actual = jdbcProductDao.find(NOT_EXISTED_ID);
    assertFalse(actual.isPresent());
  }

  @Test
  public void shouldGetAllPhonesWhenFindAll() {
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    List<Phone> actual = jdbcProductDao.findAll(0, expectedPhonesCount);
    assertEquals(expectedPhonesCount, actual.size());
  }

  @Test
  public void shouldUpdatePhoneWhenUpdatePhoneExist() {
    Phone phone = new Phone();
    phone.setBrand(ARCHOS_BRAND);
    phone.setModel(ARCHOS_MODEL);
    jdbcProductDao.update(phone);
    Optional<Phone> actual = jdbcProductDao.find(LAST_PHONE_ID);
    assertTrue(actual.isPresent());
    assertNull(actual.get().getAnnounced());
  }

  @Test(expected = ProductNotFoundException.class)
  public void shouldThrowProductNotFoundExceptionWhenUpdatePhoneNotExist() {
    Phone phone = new Phone();
    phone.setId(NOT_EXISTED_ID);
    phone.setBrand(BRAND);
    phone.setModel(MODEL);
    jdbcProductDao.update(phone);
  }


  @Test(expected = InvalidProductException.class)
  public void shouldThrowInvalidProductExceptionWhenUpdatePhoneNotValid() {
    Phone phone = new Phone();
    phone.setId(FIRST_PHONE_ID);
    jdbcProductDao.update(phone);
  }

  @Test
  public void shouldDeletePhoneWhenDeletePhoneExist() {
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE) - 1;
    jdbcProductDao.delete(FIRST_PHONE_ID);
    int actualPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    assertEquals(expectedPhonesCount, actualPhonesCount);
  }
}