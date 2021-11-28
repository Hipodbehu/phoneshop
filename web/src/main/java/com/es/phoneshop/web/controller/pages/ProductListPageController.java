package com.es.phoneshop.web.controller.pages;

import com.es.core.model.product.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
  public static final String DEFAULT_ORDER = "id";
  public static final String DEFAULT_ORDER_DIRECTION = "desc";
  public static final String DEFAULT_QUERY = "";
  public static final String PHONES_ATTRIBUTE = "phones";
  public static final String PHONES_PAGE_NUMBER_ATTRIBUTE = "phonesPageNumber";
  public static final String PRODUCT_LIST_PAGE = "productList";
  public static final String DEFAULT_PAGE_NUMBER = "1";
  public static final int PRODUCT_LIMIT = 10;
  public static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";

  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET)
  public String showProductList(Model model,
                                @RequestParam(required = false, defaultValue = DEFAULT_QUERY) String query,
                                @RequestParam(required = false, defaultValue = DEFAULT_ORDER) String order,
                                @RequestParam(required = false, defaultValue = DEFAULT_ORDER_DIRECTION) String orderDirection,
                                @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER) String pageNumber) {
    query = checkQuery(query);
    order = checkOrder(order);
    orderDirection = checkOrderDirection(orderDirection);
    int countOfPhones = productDao.getCount(query);
    int countOfPages = countOfPhones / PRODUCT_LIMIT;
    int pageNum = checkPageNumber(pageNumber, countOfPages);
    model.addAttribute(PHONES_PAGE_NUMBER_ATTRIBUTE, countOfPages);
    model.addAttribute(PAGE_NUMBER_ATTRIBUTE, pageNum);
    model.addAttribute(PHONES_ATTRIBUTE,
            productDao.findAll(query, order, orderDirection, (pageNum - 1) * 10, PRODUCT_LIMIT));
    return PRODUCT_LIST_PAGE;
  }

  private String checkQuery(String query) {
    if (query == null) {
      query = DEFAULT_QUERY;
    }
    return query;
  }

  private String checkOrder(String order) {
    if (order == null || order.isEmpty()) {
      order = DEFAULT_ORDER;
    }
    return order;
  }

  private Integer checkPageNumber(String pageNumber, int countOfPages) {
    int result = 1;
    if (pageNumber != null && !pageNumber.isEmpty()) {
      try {
        result = Integer.parseInt(pageNumber);
        if (result > countOfPages) {
          result = countOfPages;
        } else if (result < 1) {
          result = 1;
        }
      } catch (NumberFormatException exception) {
        exception.printStackTrace();
      }
    }
    return result;
  }

  private String checkOrderDirection(String orderDirection) {
    if (orderDirection == null || orderDirection.isEmpty()) {
      orderDirection = DEFAULT_ORDER_DIRECTION;
    }
    return orderDirection;
  }
}
