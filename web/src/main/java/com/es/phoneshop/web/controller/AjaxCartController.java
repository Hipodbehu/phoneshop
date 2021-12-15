package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.dto.AddPhoneResponseDto;
import com.es.phoneshop.web.controller.dto.MiniCartDto;
import com.es.phoneshop.web.controller.validation.ProductInputWrapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "**/ajaxCart")
public class AjaxCartController {
  public static final String DELIMITER = "\n";
  public static final String SUCCESS_MESSAGE = "Added to cart";
  public static final String BAD_QUANTITY_MESSAGE = "Bad quantity";

  @Resource
  private CartService cartService;

  @RequestMapping(method = RequestMethod.GET)
  public MiniCartDto getCart(HttpSession session) {
    Cart cart = cartService.getCart(session);
    return createMiniCartDto(cart);
  }

  @RequestMapping(method = RequestMethod.POST)
  public AddPhoneResponseDto addPhone(@RequestBody @Valid ProductInputWrapper productInputWrapper,
                                      BindingResult bindingResult,
                                      HttpSession session) {
    Cart cart = cartService.getCart(session);
    boolean isSuccessful = true;
    if (bindingResult.hasErrors()) {
      isSuccessful = false;
    } else {
      try {
        cartService.addPhone(cart, productInputWrapper.getId(), productInputWrapper.getQuantity());
      } catch (OutOfStockException exception) {
        isSuccessful = false;
        bindingResult.addError(new ObjectError(ProductInputWrapper.class.toString(), exception.getMessage()));
      }
    }
    return createAddPhoneResponseDto(isSuccessful, cart, bindingResult);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public AddPhoneResponseDto handleException(HttpSession session) {
    BindException bindException = new BindException(HttpMessageNotReadableException.class,
            HttpMessageNotReadableException.class.toString());
    bindException.addError(new ObjectError(ProductInputWrapper.class.toString(), BAD_QUANTITY_MESSAGE));
    return createAddPhoneResponseDto(false, cartService.getCart(session), bindException.getBindingResult());
  }

  private AddPhoneResponseDto createAddPhoneResponseDto(boolean successful, Cart cart, BindingResult bindingResult) {
    AddPhoneResponseDto addPhoneResponseDto = new AddPhoneResponseDto();
    addPhoneResponseDto.setMiniCart(createMiniCartDto(cart));
    addPhoneResponseDto.setSuccessful(successful);
    String message;
    if (successful) {
      message = SUCCESS_MESSAGE;
    } else {
      message = bindingResult.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.joining(DELIMITER));
    }
    addPhoneResponseDto.setMessage(message);
    return addPhoneResponseDto;
  }

  private MiniCartDto createMiniCartDto(Cart cart) {
    MiniCartDto miniCartDto = new MiniCartDto();
    miniCartDto.setTotalCost(cart.getTotalCost());
    miniCartDto.setTotalQuantity(cart.getTotalQuantity());
    return miniCartDto;
  }
}
