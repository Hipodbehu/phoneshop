package com.es.core.model.product;

import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
  Optional<Phone> find(Long id);

  Optional<Phone> findByModel(String model);

  List<Phone> findAll(String query, String sort, String sortDirection, int offset, int limit);

  Integer getCount(String query);

  Optional<Stock> getStock(Long id);


  void save(Phone phone);

  void update(Phone phone);

  void updateStock(Stock stock);

  void delete(Long id);
}
