package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.validation.ProductInputWrapper;
import com.es.phoneshop.web.controller.validation.ProductListWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
  public static final String CART_PAGE = "cart";
  public static final String CART_ATTRIBUTE = "cart";
  public static final String PRODUCT_LIST_WRAPPER_ATTRIBUTE = "productListWrapper";
  public static final String QUANTITY_ERROR_FIELD = "list[%d].quantity";

  @Resource
  private CartService cartService;

  @RequestMapping(method = RequestMethod.GET)
  public void getCart(HttpSession session, Model model) {
    Cart cart = cartService.getCart(session);
    model.addAttribute(CART_ATTRIBUTE, cart);
    ProductListWrapper productListWrapper = new ProductListWrapper();
    productListWrapper.setList(cart.getItemList().stream()
            .map(cartItem -> new ProductInputWrapper(cartItem.getPhone().getId(), cartItem.getQuantity()))
            .collect(Collectors.toList()));
    model.addAttribute(PRODUCT_LIST_WRAPPER_ATTRIBUTE, productListWrapper);
  }

  @RequestMapping(method = RequestMethod.PUT)
  public String updateCart(Model model, HttpSession session,
                           @ModelAttribute @Valid ProductListWrapper productListWrapper,
                           BindingResult bindingResult) {
    if (!bindingResult.hasErrors()) {
      Map<Long, Integer> productMap = productListWrapper.getList().stream()
              .collect(Collectors.toMap(ProductInputWrapper::getId, ProductInputWrapper::getQuantity));
      try {
        cartService.update(cartService.getCart(session), productMap);
      } catch (OutOfStockException exception) {
        List<Long> idList = exception.getIdList();
        IntStream.iterate(0, value -> value++)
                .limit(idList.size())
                .forEach(value -> {
                  bindingResult.addError(
                          new FieldError(PRODUCT_LIST_WRAPPER_ATTRIBUTE,
                                  String.format(QUANTITY_ERROR_FIELD, value), exception.getMessage()));
                });
      }
    }
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
