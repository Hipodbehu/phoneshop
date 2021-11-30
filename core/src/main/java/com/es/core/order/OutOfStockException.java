package com.es.core.order;

public class OutOfStockException extends Exception {
  public static final String ERROR_MESSAGE = "Stock is less";

  public OutOfStockException() {
    super(ERROR_MESSAGE);
  }
}
