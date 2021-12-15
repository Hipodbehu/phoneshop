package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    public static final String ORDERS_PAGE = "orders";
    public static final String ORDER_PAGE = "orderOverview";
    public static final String ORDER_ATTRIBUTE = "order";
    public static final String ORDERS_ATTRIBUTE = "orders";
    public static final String UPDATE_MESSAGE_ATTRIBUTE = "updateMessage";
    public static final String SUCCESS_MESSAGE = "Updated successfully";
    public static final String ADMIN_ATTRIBUTE = "admin";

    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrders(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderService.getOrders());
        return ORDERS_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String getOrder(Model model, @PathVariable Long id) {
        model.addAttribute(ORDER_ATTRIBUTE, orderService.getOrder(id));
        model.addAttribute(ADMIN_ATTRIBUTE, true);
        return ORDER_PAGE;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public String getOrder(Model model, @PathVariable Long id,
                           @RequestParam String status) {
        orderService.updateOrderStatus(id, getStatus(status));
        model.addAttribute(UPDATE_MESSAGE_ATTRIBUTE, SUCCESS_MESSAGE);
        model.addAttribute(ORDER_ATTRIBUTE, orderService.getOrder(id));
        model.addAttribute(ADMIN_ATTRIBUTE, true);
        return ORDER_PAGE;
    }

    public OrderStatus getStatus(String status) {
        return OrderStatus.valueOf(status);
    }
}
