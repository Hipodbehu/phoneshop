package com.es.phoneshop.web.controller.pages;

import com.es.core.model.product.ProductDao;
import com.es.core.model.product.Sort;
import com.es.core.model.product.SortDirection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
  public static final String DEFAULT_SORT = "id";
  public static final String DEFAULT_SORT_DIRECTION = "desc";
  public static final String DEFAULT_QUERY = "";
  public static final String DEFAULT_PAGE_NUMBER = "1";
  public static final String PHONES_ATTRIBUTE = "phones";
  public static final String PHONES_PAGE_NUMBER_ATTRIBUTE = "phonesPageNumber";
  public static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
  public static final String PRODUCT_LIST_PAGE = "productList";
  public static final int PRODUCT_LIMIT = 10;
  public static final String DISPLAY_SIZE_INCHES = "displaySizeInches";

  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET)
  public String showProductList(Model model, Locale locale,
                                @RequestParam(required = false, defaultValue = DEFAULT_QUERY) String query,
                                @RequestParam(required = false, defaultValue = DEFAULT_SORT) String sort,
                                @RequestParam(required = false, defaultValue = DEFAULT_SORT_DIRECTION) String sortDirection,
                                @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER) String pageNumber) {
    Locale.setDefault(locale);
    query = getQuery(query);
    sort = getSort(sort);
    sortDirection = getSortDirection(sortDirection);
    int countOfPhones = productDao.getCount(query);
    int countOfPages = countOfPhones / PRODUCT_LIMIT;
    int pageNum = getPageNumber(pageNumber, countOfPages);
    model.addAttribute(PHONES_PAGE_NUMBER_ATTRIBUTE, countOfPages);
    model.addAttribute(PAGE_NUMBER_ATTRIBUTE, pageNum);
    model.addAttribute(PHONES_ATTRIBUTE,
            productDao.findAll(query, sort, sortDirection, (pageNum - 1) * PRODUCT_LIMIT, PRODUCT_LIMIT));
    return PRODUCT_LIST_PAGE;
  }

  private String getQuery(String query) {
    if (StringUtils.isEmpty(query)) {
      query = DEFAULT_QUERY;
    }
    return query;
  }

  private String getSort(String sort) {
    if (StringUtils.isEmpty(sort)) {
      sort = DEFAULT_SORT;
    } else {
      String finalSort = sort.toUpperCase(Locale.ROOT);
      if (Arrays.stream(Sort.values())
              .anyMatch(sort1 -> sort1.toString().
                      equals(finalSort))) {
        if (Sort.valueOf(finalSort).equals(Sort.DISPLAY_SIZE)) {
          sort = DISPLAY_SIZE_INCHES;
        }
      } else {
        sort = DEFAULT_SORT;
      }
    }
    return sort;
  }

  private Integer getPageNumber(String pageNumber, int countOfPages) {
    int result = 1;
    if (!StringUtils.isEmpty(pageNumber)) {
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

  private String getSortDirection(String sortDirection) {
    if (StringUtils.isEmpty(sortDirection)) {
      sortDirection = DEFAULT_SORT_DIRECTION;
    } else {
      String finalSortDirection = sortDirection.toUpperCase(Locale.ROOT);
      if (Arrays.stream(SortDirection.values())
              .noneMatch(sortDirection1 -> sortDirection1.toString()
                      .equals(finalSortDirection))) {
        sortDirection = DEFAULT_SORT_DIRECTION;
      }
    }
    return sortDirection;
  }
}
