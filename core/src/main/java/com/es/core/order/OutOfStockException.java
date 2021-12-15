package com.es.core.order;

import java.util.ArrayList;
import java.util.List;

public class OutOfStockException extends Exception {
  public static final String ERROR_MESSAGE = "Stock is less";
  private List<Long> idList;

  public OutOfStockException() {
    super(ERROR_MESSAGE);
  }

  public OutOfStockException(List<Long> idList) {
    super(ERROR_MESSAGE);
    this.idList = idList;
  }

  public List<Long> getIdList() {
    return new ArrayList<>(idList);
  }
}
