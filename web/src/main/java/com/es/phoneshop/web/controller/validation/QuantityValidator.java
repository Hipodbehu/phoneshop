package com.es.phoneshop.web.controller.validation;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class QuantityValidator implements Validator {
  public static final String QUANTITY_FIELD = "quantity";
  public static final String FORMAT_ERROR_MESSAGE = "Not a number";
  public static final String BAD_QUANTITY_ERROR_MESSAGE = "Cannot be less than ";
  public static final int MINIMAL_QUANTITY = 1;

  @Override
  public boolean supports(Class<?> aClass) {
    return QuantityInputWrapper.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    QuantityInputWrapper quantityInputWrapper = (QuantityInputWrapper) o;
    try {
      int quantity = Integer.parseInt(quantityInputWrapper.getQuantity());
      if (quantity < MINIMAL_QUANTITY) {
        errors.rejectValue(QUANTITY_FIELD, BAD_QUANTITY_ERROR_MESSAGE, BAD_QUANTITY_ERROR_MESSAGE + MINIMAL_QUANTITY);
      }
    } catch (NumberFormatException e) {
      errors.rejectValue(QUANTITY_FIELD, FORMAT_ERROR_MESSAGE, FORMAT_ERROR_MESSAGE);
    }
  }
}
