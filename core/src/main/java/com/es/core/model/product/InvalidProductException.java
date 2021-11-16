package com.es.core.model.product;

public class InvalidProductException extends RuntimeException {
  public InvalidProductException() {
  }

  public InvalidProductException(String message) {
    super(message);
  }

  public InvalidProductException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidProductException(Throwable cause) {
    super(cause);
  }
}
