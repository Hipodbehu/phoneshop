package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
  public static final String CART_PAGE = "cart";
  public static final String CART_ATTRIBUTE = "cart";
  public static final String ERRORS_ATTRIBUTE = "errors";
  @Resource
  private CartService cartService;

  @RequestMapping(method = RequestMethod.GET)
  public void getCart(HttpSession session, Model model) {
    model.addAttribute(CART_ATTRIBUTE, cartService.getCart(session));
  }

  @RequestMapping(method = RequestMethod.PUT)
  public String updateCart(@RequestParam List<Long> idList,
                           @RequestParam @Min(1) List<Integer> quantityList, HttpSession session, Model model) {
    LinkedHashMap<Long, Integer> productMap = new LinkedHashMap<>();
    IntStream.iterate(0, value -> value + 1)
            .limit(idList.size()).forEach(value -> productMap.put(idList.get(value), quantityList.get(value)));
    Map<Long, String> errors = cartService.update(cartService.getCart(session), productMap);
    model.addAttribute(ERRORS_ATTRIBUTE, errors);
    model.addAttribute(CART_ATTRIBUTE, cartService.getCart(session));
    return CART_PAGE;
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{phoneId}")
  public String removeFromCart(@PathVariable Long phoneId, HttpSession session, Model model) {
    Cart cart = cartService.getCart(session);
    cartService.remove(cart, phoneId);
    model.addAttribute(CART_ATTRIBUTE, cart);
    return CART_PAGE;
  }
}
