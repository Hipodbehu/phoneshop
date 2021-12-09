package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    public static final String ORDER_ATTRIBUTE = "order";
    public static final String ORDER_PAGE = "order";
    public static final String ORDER_OVERVIEW_PAGE = "orderOverview";
    public static final String FIRST_NAME = "firstName";
    public static final String REQUIRED_MESSAGE = "Required";
    public static final String LAST_NAME = "lastName";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String ERRORS_ATTRIBUTE = "errors";
    public static final String SLASH = "/";

    @Resource
    private OrderService orderService;

    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model, HttpSession session) {
        model.addAttribute(ORDER_ATTRIBUTE, orderService.createOrder(cartService.getCart(session)));
        return ORDER_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(Model model, HttpSession session,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String address,
                             @RequestParam String phone) {
        Order order = orderService.createOrder(cartService.getCart(session));
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isEmpty(firstName)) {
            errors.put(FIRST_NAME, REQUIRED_MESSAGE);
        }
        if (StringUtils.isEmpty(lastName)) {
            errors.put(LAST_NAME, REQUIRED_MESSAGE);
        }
        if (StringUtils.isEmpty(address)) {
            errors.put(ADDRESS, REQUIRED_MESSAGE);
        }
        if (StringUtils.isEmpty(phone)) {
            errors.put(PHONE, REQUIRED_MESSAGE);
        }
        if (errors.isEmpty()) {
            order.setFirstName(firstName);
            order.setLastName(lastName);
            order.setDeliveryAddress(address);
            order.setContactPhoneNo(phone);
            orderService.placeOrder(order, errors);
            if (OrderStatus.NEW.equals(order.getStatus())) {
                cartService.clear(session);
                return String.join(SLASH, "redirect:", ORDER_OVERVIEW_PAGE, order.getSecureId());
            } else {
                model.addAttribute(ERRORS_ATTRIBUTE, errors);
                model.addAttribute(ORDER_ATTRIBUTE, order);
                return ORDER_PAGE;
            }
        } else {
            model.addAttribute(ERRORS_ATTRIBUTE, errors);
            model.addAttribute(ORDER_ATTRIBUTE, order);
            return ORDER_PAGE;
        }
    }
}
