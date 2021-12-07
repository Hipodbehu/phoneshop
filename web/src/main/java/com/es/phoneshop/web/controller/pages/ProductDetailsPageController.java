package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.model.product.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
  public static final String PRODUCT_DETAILS_PAGE = "productDetails";
  public static final String PHONE_ATTRIBUTE = "phone";

  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET, value = "/{phoneId}")
  public String showProductList(Model model,
                                @PathVariable Long phoneId) {
    Phone phone = productDao.find(phoneId).orElseThrow(RuntimeException::new);
    model.addAttribute(PHONE_ATTRIBUTE, phone);
    return PRODUCT_DETAILS_PAGE;
  }
}
