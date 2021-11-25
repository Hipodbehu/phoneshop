package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.dto.AddPhoneResponseDto;
import com.es.phoneshop.web.controller.dto.MiniCartDto;
import com.es.phoneshop.web.controller.validation.QuantityInputWrapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
  public static final String SUCCESS_MESSAGE = "Added to cart";
  public static final String DELIMITER = "\n";

  @Resource
  private CartService cartService;

  @Resource
  private Validator quantityValidator;

  @InitBinder
  private void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.setValidator(quantityValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  public MiniCartDto getCart(HttpSession session) {
    Cart cart = cartService.getCart(session);
    return createMiniCartDto(cart);
  }

  @RequestMapping(method = RequestMethod.POST)
  public AddPhoneResponseDto addPhone(@RequestParam Long phoneId,
                                      QuantityInputWrapper quantityInputWrapper,
                                      HttpSession session,
                                      BindingResult bindingResult) {
    Cart cart = cartService.getCart(session);
    quantityValidator.validate(quantityInputWrapper, bindingResult);
    boolean isSuccessful = true;
    if (bindingResult.hasErrors()) {
      isSuccessful = false;
    } else {
      try {
        cartService.addPhone(cart, phoneId, Integer.parseInt(quantityInputWrapper.getQuantity()));
      } catch (OutOfStockException exception) {
        isSuccessful = false;
        bindingResult.addError(new ObjectError(QuantityInputWrapper.class.toString(), exception.getMessage()));
      }
    }
    return createAddPhoneResponseDto(isSuccessful, cart, bindingResult);
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
