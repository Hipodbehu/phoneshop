package com.es.phoneshop.web.controller.pages;

import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
  public static final String ORDER_OVERVIEW_PAGE = "orderOverview";
  public static final String ORDER_ATTRIBUTE = "order";

  @Resource
  private OrderService orderService;

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public String getOrder(Model model, @PathVariable String id) {
    model.addAttribute(ORDER_ATTRIBUTE, orderService.getOrder(id));
    return ORDER_OVERVIEW_PAGE;
  }
}
