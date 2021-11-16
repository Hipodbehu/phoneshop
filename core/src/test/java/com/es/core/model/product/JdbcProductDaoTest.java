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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration("/applicationTestContext-core.xml")
public class JdbcProductDaoTest {
  public static final Long FIRST_PHONE_ID = 1001L;
  public static final Long LAST_PHONE_ID = 1009L;
  public static final String BRAND = "test_brand";
  public static final String MODEL = "test_model";
  public static final String PHONES_TABLE = "phones";
  @Resource
  private JdbcProductDao jdbcProductDao;
  @Resource
  private JdbcTemplate jdbcTemplate;

  @After
  public void tearDown() {
    JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, PHONES_TABLE, "brand = ? AND model = ?", BRAND, MODEL);
  }

  @Test
  public void testSave() {
    Phone phone = new Phone();
    phone.setBrand(BRAND);
    phone.setModel(MODEL);
    Color color = new Color();
    color.setCode("Test-Color");
    phone.setColors(Stream.of(color).collect(Collectors.toSet()));
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE) + 1;
    jdbcProductDao.save(phone);
    int actualPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    assertEquals(expectedPhonesCount, actualPhonesCount);
  }

  @Test(expected = InvalidProductException.class)
  public void testSaveNull() {
    jdbcProductDao.save(null);
  }

  @Test(expected = InvalidProductException.class)
  public void testSaveNoBrand() {
    Phone phone = new Phone();
    phone.setBrand(BRAND);
    jdbcProductDao.save(null);
  }

  @Test(expected = InvalidProductException.class)
  public void testSaveWithNoModel() {
    Phone phone = new Phone();
    phone.setModel(MODEL);
    jdbcProductDao.save(null);
  }

  @Test
  public void testFind() {
    Optional<Phone> actual = jdbcProductDao.find(FIRST_PHONE_ID);
    assertTrue(actual.isPresent());
  }

  @Test
  public void testFindNotExist() {
    Optional<Phone> actual = jdbcProductDao.find(1L);
    assertFalse(actual.isPresent());
  }

  @Test
  public void testFindAll() {
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    List<Phone> actual = jdbcProductDao.findAll(0, expectedPhonesCount);
    assertEquals(expectedPhonesCount, actual.size());
  }

  @Test
  public void testUpdate() {
    Phone phone = new Phone();
    phone.setBrand("ARCHOS");
    phone.setModel("ARCHOS 28 Internet Tablet");
    jdbcProductDao.update(phone);
    Optional<Phone> actual = jdbcProductDao.find(LAST_PHONE_ID);
    assertTrue(actual.isPresent());
    assertNull(actual.get().getAnnounced());
  }

  @Test(expected = ProductNotFoundException.class)
  public void testUpdateNotExist() {
    Phone phone = new Phone();
    phone.setId(1L);
    phone.setBrand(BRAND);
    phone.setModel(MODEL);
    jdbcProductDao.update(phone);
  }


  @Test(expected = InvalidProductException.class)
  public void testUpdateNotValid() {
    Phone phone = new Phone();
    phone.setId(FIRST_PHONE_ID);
    jdbcProductDao.update(phone);
  }

  @Test
  public void testDelete() {
    int a = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    int expectedPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE) - 1;
    jdbcProductDao.delete(FIRST_PHONE_ID);
    int actualPhonesCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, PHONES_TABLE);
    assertEquals(expectedPhonesCount, actualPhonesCount);
  }
}