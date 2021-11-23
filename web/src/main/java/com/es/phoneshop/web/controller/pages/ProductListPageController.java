package com.es.phoneshop.web.controller.pages;

import com.es.core.model.product.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET)
  public String showProductList(Model model) {
    model.addAttribute("phones", productDao.findAll(0, 10));
    return "productList";
  }
}
