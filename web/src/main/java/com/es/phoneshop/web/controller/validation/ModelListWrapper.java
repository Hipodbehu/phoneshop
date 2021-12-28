package com.es.phoneshop.web.controller.validation;

import javax.validation.Valid;
import java.util.List;

public class ModelListWrapper {
  @Valid
  private List<ModelInputWrapper> list;

  public List<ModelInputWrapper> getList() {
    return list;
  }

  public void setList(List<ModelInputWrapper> list) {
    this.list = list;
  }
}
