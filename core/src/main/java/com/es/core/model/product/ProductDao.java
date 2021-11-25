package com.es.core.model.product;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
  Optional<Phone> find(Long key);

  List<Phone> findAll(String query, String order, String orderDirection, int offset, int limit);

  Integer getCount(String query);

  Integer getStock(Long id);

  void save(Phone phone);

  void update(Phone phone);

  void delete(Long key);
}
