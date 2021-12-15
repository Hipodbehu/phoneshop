package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.validation.UserInfoWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    public static final String ORDER_PAGE = "order";
    public static final String ORDER_OVERVIEW_PAGE = "orderOverview";
    public static final String ORDER_ATTRIBUTE = "order";
    public static final String USER_INFO_ATTRIBUTE = "userInfoWrapper";
    public static final String ERROR_ATTRIBUTE = "error";
    public static final String REDIRECT = "redirect:";
    public static final String SLASH = "/";

    @Resource
    private OrderService orderService;

    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public void getOrder(Model model, HttpSession session) {
        model.addAttribute(ORDER_ATTRIBUTE, orderService.createOrder(cartService.getCart(session)));
        model.addAttribute(USER_INFO_ATTRIBUTE, new UserInfoWrapper());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(Model model, HttpSession session,
                             @Valid @ModelAttribute UserInfoWrapper userInfoWrapper,
                             BindingResult bindingResult) {
        Order order = orderService.createOrder(cartService.getCart(session));
        String page;
        if (bindingResult.hasErrors()) {
            model.addAttribute(ORDER_ATTRIBUTE, order);
            page = ORDER_PAGE;
        } else {
            setUserInfo(userInfoWrapper, order);
            try {
                orderService.placeOrder(order);
                    cartService.clear(session);
                    page = String.join(SLASH, REDIRECT, ORDER_OVERVIEW_PAGE, order.getSecureId());
            } catch (OutOfStockException exception) {
                model.addAttribute(ERROR_ATTRIBUTE, exception.getMessage());
                model.addAttribute(ORDER_ATTRIBUTE, order);
                page = ORDER_PAGE;
            }
        }
        return page;
    }

    private void setUserInfo(UserInfoWrapper userInfoWrapper, Order order) {
        order.setFirstName(userInfoWrapper.getFirstName());
        order.setLastName(userInfoWrapper.getLastName());
        order.setDeliveryAddress(userInfoWrapper.getAddress());
        order.setContactPhoneNo(userInfoWrapper.getPhone());
        order.setComment(userInfoWrapper.getComment());
    }
}
