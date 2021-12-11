package com.es.phoneshop.web.controller.validation;

import javax.validation.Valid;
import java.util.List;

public class ProductListWrapper {
  @Valid
  private List<ProductInputWrapper> list;

  public List<ProductInputWrapper> getList() {
    return list;
  }

  public void setList(List<ProductInputWrapper> list) {
    this.list = list;
  }
}
