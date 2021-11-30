package com.es.phoneshop.web.controller.pages;

import com.es.core.model.product.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
  public static final String DEFAULT_PAGE_NUMBER = "1";
  public static final String PHONES_ATTRIBUTE = "phones";
  public static final String PHONES_PAGE_NUMBER_ATTRIBUTE = "phonesPageNumber";
  public static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
  public static final String PRODUCT_LIST_PAGE = "productList";
  public static final int PRODUCT_LIMIT = 10;

  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET)
  public String showProductList(Model model,
                                @RequestParam(required = false, defaultValue = DEFAULT_QUERY) String query,
                                @RequestParam(required = false, defaultValue = DEFAULT_ORDER) String order,
                                @RequestParam(required = false, defaultValue = DEFAULT_ORDER_DIRECTION) String orderDirection,
                                @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER) String pageNumber) {
    query = getQuery(query);
    order = getOrder(order);
    orderDirection = getOrderDirection(orderDirection);
    int countOfPhones = productDao.getCount(query);
    int countOfPages = countOfPhones / PRODUCT_LIMIT;
    int pageNum = getPageNumber(pageNumber, countOfPages);
    model.addAttribute(PHONES_PAGE_NUMBER_ATTRIBUTE, countOfPages);
    model.addAttribute(PAGE_NUMBER_ATTRIBUTE, pageNum);
    model.addAttribute(PHONES_ATTRIBUTE,
            productDao.findAll(query, order, orderDirection, (pageNum - 1) * 10, PRODUCT_LIMIT));
    return PRODUCT_LIST_PAGE;
  }

  private String getQuery(String query) {
    if (StringUtils.isEmpty(query)) {
      query = DEFAULT_QUERY;
    }
    return query;
  }

  private String getOrder(String order) {
    if (StringUtils.isEmpty(order)) {
      order = DEFAULT_ORDER;
    }
    return order;
  }

  private Integer getPageNumber(String pageNumber, int countOfPages) {
    int result = 1;
    if (StringUtils.isEmpty(pageNumber)) {
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

  private String getOrderDirection(String orderDirection) {
    if (StringUtils.isEmpty(orderDirection)) {
      orderDirection = DEFAULT_ORDER_DIRECTION;
    }
    return orderDirection;
  }
}
